module Morphir.Visual.ViewValue exposing (viewDefinition)

import Dict exposing (Dict)
import Element exposing (Element, column, el, fill, rgb, spacing, text, width)
import Element.Background as Background
import Element.Border as Border
import Element.Events exposing (onClick)
import Element.Font as Font exposing (..)
import Morphir.IR.FQName exposing (FQName)
import Morphir.IR.Name exposing (Name)
import Morphir.IR.SDK.Basics as Basics
import Morphir.IR.Type as Type exposing (Type)
import Morphir.IR.Value as Value exposing (RawValue, TypedValue, Value)
import Morphir.Visual.BoolOperatorTree as BoolOperatorTree exposing (BoolOperatorTree)
import Morphir.Visual.Common exposing (definition, nameToText)
import Morphir.Visual.Components.AritmeticExpressions as ArithmeticOperatorTree exposing (ArithmeticOperatorTree)
import Morphir.Visual.Config exposing (Config)
import Morphir.Visual.ViewApply as ViewApply
import Morphir.Visual.ViewArithmetic as ViewArithmetic
import Morphir.Visual.ViewBoolOperatorTree as ViewBoolOperatorTree
import Morphir.Visual.ViewField as ViewField
import Morphir.Visual.ViewIfThenElse as ViewIfThenElse
import Morphir.Visual.ViewList as ViewList
import Morphir.Visual.ViewLiteral as ViewLiteral
import Morphir.Visual.ViewReference as ViewReference
import Morphir.Visual.ViewTuple as ViewTuple
import Morphir.Visual.XRayView as XRayView
import Morphir.Web.Theme.Light exposing (gray)


viewDefinition : Config msg -> FQName -> Value.Definition () (Type ()) -> Element msg
viewDefinition config ( _, _, valueName ) valueDef =
    let
        _ =
            Debug.log "variables" config.state.variables
    in
    Element.column [ spacing 20 ]
        [ definition
            (nameToText valueName)
            (viewValue config valueDef.body)
        , if Dict.isEmpty config.state.expandedFunctions then
            Element.none

          else
            Element.column
                [ spacing 20 ]
                [ Element.column
                    [ spacing 20
                    ]
                    (config.state.expandedFunctions
                        |> Dict.toList
                        |> List.reverse
                        |> List.map
                            (\( ( _, _, localName ) as fqName, valDef ) ->
                                Element.column
                                    [ spacing 10
                                    ]
                                    [ definition (nameToText localName)
                                        (viewValue config valDef.body)
                                    , Element.column
                                        [ Font.bold
                                        , Border.solid
                                        , Border.rounded 5
                                        , Background.color gray
                                        , Element.padding 10
                                        , onClick (config.handlers.onReferenceClicked fqName True)
                                        ]
                                        [ Element.text "Close" ]
                                    ]
                            )
                    )
                ]
        ]


viewValue : Config msg -> TypedValue -> Element msg
viewValue config value =
    viewValueByValueType config value


viewValueByValueType : Config msg -> TypedValue -> Element msg
viewValueByValueType config typedValue =
    let
        valueType : Type ()
        valueType =
            Value.valueAttribute typedValue
    in
    if valueType == Basics.boolType () then
        let
            boolOperatorTree : BoolOperatorTree
            boolOperatorTree =
                BoolOperatorTree.fromTypedValue typedValue
        in
        ViewBoolOperatorTree.view (viewValueByLanguageFeature config) boolOperatorTree

    else if Basics.isNumber valueType then
        let
            arithmeticOperatorTree : ArithmeticOperatorTree
            arithmeticOperatorTree =
                ArithmeticOperatorTree.fromArithmeticTypedValue typedValue
        in
        ViewArithmetic.view (viewValueByLanguageFeature config) arithmeticOperatorTree

    else
        viewValueByLanguageFeature config typedValue


viewValueByLanguageFeature : Config msg -> TypedValue -> Element msg
viewValueByLanguageFeature config value =
    let
        valueElem : Element msg
        valueElem =
            case value of
                Value.Literal literalType literal ->
                    ViewLiteral.view literal

                Value.Constructor tpe fQName ->
                    ViewReference.view config (viewValue config) fQName

                Value.Tuple tpe elems ->
                    ViewTuple.view (viewValue config) elems

                Value.List (Type.Reference _ ( [ [ "morphir" ], [ "s", "d", "k" ] ], [ [ "list" ] ], [ "list" ] ) [ itemType ]) items ->
                    ViewList.view config (viewValue config) itemType items

                Value.Variable tpe name ->
                    el []
                        (text (nameToText name))

                Value.Reference tpe fQName ->
                    ViewReference.view config (viewValue config) fQName

                Value.Field tpe subjectValue fieldName ->
                    ViewField.view (viewValue config) subjectValue fieldName

                Value.Apply _ fun arg ->
                    let
                        ( function, args ) =
                            Value.uncurryApply fun arg
                    in
                    ViewApply.view (viewValue config) function args

                Value.LetDefinition tpe _ _ _ ->
                    let
                        unnest : Value ta (Type ta) -> ( List ( Name, Value.Definition ta (Type ta) ), Value ta (Type ta) )
                        unnest v =
                            case v of
                                Value.LetDefinition _ defName def inVal ->
                                    let
                                        ( defs, bottomIn ) =
                                            unnest inVal
                                    in
                                    ( ( defName, def ) :: defs, bottomIn )

                                notLet ->
                                    ( [], notLet )

                        ( definitions, inValue ) =
                            unnest value
                    in
                    column
                        [ spacing 20 ]
                        [ viewValue config inValue
                        , column
                            [ spacing 20
                            ]
                            (definitions
                                |> List.map
                                    (\( defName, def ) ->
                                        column
                                            [ spacing 10
                                            ]
                                            [ definition (nameToText defName)
                                                (viewValue config def.body)
                                            ]
                                    )
                            )
                        ]

                Value.IfThenElse _ _ _ _ ->
                    ViewIfThenElse.view config (viewValue config) value

                other ->
                    Element.column
                        [ Background.color (rgb 1 0.6 0.6)
                        , Element.padding 5
                        , Border.rounded 3
                        ]
                        [ Element.el
                            [ Element.padding 5
                            , Font.bold

                            --, Font.color (rgb 1 1 1)
                            ]
                            (Element.text "No visual mapping found for:")
                        , Element.el
                            [ Background.color (rgb 1 1 1)
                            , Element.padding 5
                            , Border.rounded 3
                            , width fill
                            ]
                            (XRayView.viewValue XRayView.viewType other)
                        ]
    in
    valueElem
