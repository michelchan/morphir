module Morphir.IR.SDK.SDKNativeFunctions exposing (..)

import Morphir.IR.Literal exposing (Literal(..))
import Morphir.Value.Native as Native exposing (..)


nativeFunctions : List ( String, String, Native.Function )
nativeFunctions =
    [ ( "Basics", "acos", eval1 Basics.acos (decodeLiteral floatLiteral) (encodeLiteral FloatLiteral) )
    , ( "Basics", "and", eval2 (&&) (decodeLiteral boolLiteral) (decodeLiteral boolLiteral) (encodeLiteral BoolLiteral) )
    , ( "Basics", "asin", eval1 Basics.asin (decodeLiteral floatLiteral) (encodeLiteral FloatLiteral) )
    , ( "Basics", "atan", eval1 Basics.atan (decodeLiteral floatLiteral) (encodeLiteral FloatLiteral) )
    , ( "Basics", "atan2", eval2 Basics.atan2 (decodeLiteral floatLiteral) (decodeLiteral floatLiteral) (encodeLiteral FloatLiteral) )
    , ( "Basics", "ceiling", eval1 Basics.ceiling (decodeLiteral floatLiteral) (encodeLiteral WholeNumberLiteral) )
    , ( "Basics", "cos", eval1 Basics.cos (decodeLiteral floatLiteral) (encodeLiteral FloatLiteral) )
    , ( "Basics", "degrees", eval1 Basics.degrees (decodeLiteral floatLiteral) (encodeLiteral FloatLiteral) )
    , ( "Basics", "divide", eval2 (/) (decodeLiteral floatLiteral) (decodeLiteral floatLiteral) (encodeLiteral FloatLiteral) )
    , ( "Basics", "e", eval0 Basics.e (encodeLiteral FloatLiteral) )
    , ( "Basics", "floor", eval1 Basics.floor (decodeLiteral floatLiteral) (encodeLiteral WholeNumberLiteral) )
    , ( "Basics", "fromPolar", eval1 Basics.fromPolar (decodeTuple2 ( decodeLiteral floatLiteral, decodeLiteral floatLiteral )) (encodeTuple2 ( encodeLiteral FloatLiteral, encodeLiteral FloatLiteral )) )
    , ( "Basics", "integerDivide", eval2 (//) (decodeLiteral intLiteral) (decodeLiteral intLiteral) (encodeLiteral WholeNumberLiteral) )
    , ( "Basics", "isInfinite", eval1 Basics.isInfinite (decodeLiteral floatLiteral) (encodeLiteral BoolLiteral) )
    , ( "Basics", "isNaN", eval1 Basics.isNaN (decodeLiteral floatLiteral) (encodeLiteral BoolLiteral) )
    , ( "Basics", "logBase", eval2 Basics.logBase (decodeLiteral floatLiteral) (decodeLiteral floatLiteral) (encodeLiteral FloatLiteral) )
    , ( "Basics", "modBy", eval2 Basics.modBy (decodeLiteral intLiteral) (decodeLiteral intLiteral) (encodeLiteral WholeNumberLiteral) )
    , ( "Basics", "not", eval1 Basics.not (decodeLiteral boolLiteral) (encodeLiteral BoolLiteral) )
    , ( "Basics", "or", eval2 (||) (decodeLiteral boolLiteral) (decodeLiteral boolLiteral) (encodeLiteral BoolLiteral) )
    , ( "Basics", "pi", eval0 Basics.pi (encodeLiteral FloatLiteral) )
    , ( "Basics", "radians", eval1 Basics.radians (decodeLiteral floatLiteral) (encodeLiteral FloatLiteral) )
    , ( "Basics", "remainderBy", eval2 Basics.remainderBy (decodeLiteral intLiteral) (decodeLiteral intLiteral) (encodeLiteral WholeNumberLiteral) )
    , ( "Basics", "round", eval1 Basics.round (decodeLiteral floatLiteral) (encodeLiteral WholeNumberLiteral) )
    , ( "Basics", "sin", eval1 Basics.sin (decodeLiteral floatLiteral) (encodeLiteral FloatLiteral) )
    , ( "Basics", "sqrt", eval1 Basics.sqrt (decodeLiteral floatLiteral) (encodeLiteral FloatLiteral) )
    , ( "Basics", "tan", eval1 Basics.tan (decodeLiteral floatLiteral) (encodeLiteral FloatLiteral) )
    , ( "Basics", "toFloat", eval1 Basics.toFloat (decodeLiteral intLiteral) (encodeLiteral FloatLiteral) )
    , ( "Basics", "toPolar", eval1 Basics.toPolar (decodeTuple2 ( decodeLiteral floatLiteral, decodeLiteral floatLiteral )) (encodeTuple2 ( encodeLiteral FloatLiteral, encodeLiteral FloatLiteral )) )
    , ( "Basics", "truncate", eval1 Basics.truncate (decodeLiteral floatLiteral) (encodeLiteral WholeNumberLiteral) )
    , ( "Basics", "turns", eval1 Basics.turns (decodeLiteral floatLiteral) (encodeLiteral FloatLiteral) )
    , ( "Basics", "xor", eval2 Basics.xor (decodeLiteral boolLiteral) (decodeLiteral boolLiteral) (encodeLiteral BoolLiteral) )
    , ( "Char", "fromCode", eval1 Char.fromCode (decodeLiteral intLiteral) (encodeLiteral CharLiteral) )
    , ( "Char", "isAlpha", eval1 Char.isAlpha (decodeLiteral charLiteral) (encodeLiteral BoolLiteral) )
    , ( "Char", "isAlphaNum", eval1 Char.isAlphaNum (decodeLiteral charLiteral) (encodeLiteral BoolLiteral) )
    , ( "Char", "isDigit", eval1 Char.isDigit (decodeLiteral charLiteral) (encodeLiteral BoolLiteral) )
    , ( "Char", "isHexDigit", eval1 Char.isHexDigit (decodeLiteral charLiteral) (encodeLiteral BoolLiteral) )
    , ( "Char", "isLower", eval1 Char.isLower (decodeLiteral charLiteral) (encodeLiteral BoolLiteral) )
    , ( "Char", "isOctDigit", eval1 Char.isOctDigit (decodeLiteral charLiteral) (encodeLiteral BoolLiteral) )
    , ( "Char", "isUpper", eval1 Char.isUpper (decodeLiteral charLiteral) (encodeLiteral BoolLiteral) )
    , ( "Char", "toCode", eval1 Char.toCode (decodeLiteral charLiteral) (encodeLiteral WholeNumberLiteral) )
    , ( "Char", "toLocaleLower", eval1 Char.toLocaleLower (decodeLiteral charLiteral) (encodeLiteral CharLiteral) )
    , ( "Char", "toLocaleUpper", eval1 Char.toLocaleUpper (decodeLiteral charLiteral) (encodeLiteral CharLiteral) )
    , ( "Char", "toLower", eval1 Char.toLower (decodeLiteral charLiteral) (encodeLiteral CharLiteral) )
    , ( "Char", "toUpper", eval1 Char.toUpper (decodeLiteral charLiteral) (encodeLiteral CharLiteral) )
    , ( "List", "range", eval2 List.range (decodeLiteral intLiteral) (decodeLiteral intLiteral) (encodeList (encodeLiteral WholeNumberLiteral)) )
    , ( "String", "append", eval2 String.append (decodeLiteral stringLiteral) (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "concat", eval1 String.concat (decodeList (decodeLiteral stringLiteral)) (encodeLiteral StringLiteral) )
    , ( "String", "cons", eval2 String.cons (decodeLiteral charLiteral) (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "contains", eval2 String.contains (decodeLiteral stringLiteral) (decodeLiteral stringLiteral) (encodeLiteral BoolLiteral) )
    , ( "String", "dropLeft", eval2 String.dropLeft (decodeLiteral intLiteral) (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "dropRight", eval2 String.dropRight (decodeLiteral intLiteral) (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "endsWith", eval2 String.endsWith (decodeLiteral stringLiteral) (decodeLiteral stringLiteral) (encodeLiteral BoolLiteral) )
    , ( "String", "fromChar", eval1 String.fromChar (decodeLiteral charLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "fromFloat", eval1 String.fromFloat (decodeLiteral floatLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "fromInt", eval1 String.fromInt (decodeLiteral intLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "fromList", eval1 String.fromList (decodeList (decodeLiteral charLiteral)) (encodeLiteral StringLiteral) )
    , ( "String", "indexes", eval2 String.indexes (decodeLiteral stringLiteral) (decodeLiteral stringLiteral) (encodeList (encodeLiteral WholeNumberLiteral)) )
    , ( "String", "indices", eval2 String.indices (decodeLiteral stringLiteral) (decodeLiteral stringLiteral) (encodeList (encodeLiteral WholeNumberLiteral)) )
    , ( "String", "isEmpty", eval1 String.isEmpty (decodeLiteral stringLiteral) (encodeLiteral BoolLiteral) )
    , ( "String", "join", eval2 String.join (decodeLiteral stringLiteral) (decodeList (decodeLiteral stringLiteral)) (encodeLiteral StringLiteral) )
    , ( "String", "left", eval2 String.left (decodeLiteral intLiteral) (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "length", eval1 String.length (decodeLiteral stringLiteral) (encodeLiteral WholeNumberLiteral) )
    , ( "String", "lines", eval1 String.lines (decodeLiteral stringLiteral) (encodeList (encodeLiteral StringLiteral)) )
    , ( "String", "pad", eval3 String.pad (decodeLiteral intLiteral) (decodeLiteral charLiteral) (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "padLeft", eval3 String.padLeft (decodeLiteral intLiteral) (decodeLiteral charLiteral) (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "padRight", eval3 String.padRight (decodeLiteral intLiteral) (decodeLiteral charLiteral) (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "repeat", eval2 String.repeat (decodeLiteral intLiteral) (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "replace", eval3 String.replace (decodeLiteral stringLiteral) (decodeLiteral stringLiteral) (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "reverse", eval1 String.reverse (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "right", eval2 String.right (decodeLiteral intLiteral) (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "slice", eval3 String.slice (decodeLiteral intLiteral) (decodeLiteral intLiteral) (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "split", eval2 String.split (decodeLiteral stringLiteral) (decodeLiteral stringLiteral) (encodeList (encodeLiteral StringLiteral)) )
    , ( "String", "startsWith", eval2 String.startsWith (decodeLiteral stringLiteral) (decodeLiteral stringLiteral) (encodeLiteral BoolLiteral) )
    , ( "String", "toFloat", eval1 String.toFloat (decodeLiteral stringLiteral) (encodeMaybe (encodeLiteral FloatLiteral)) )
    , ( "String", "toInt", eval1 String.toInt (decodeLiteral stringLiteral) (encodeMaybe (encodeLiteral WholeNumberLiteral)) )
    , ( "String", "toList", eval1 String.toList (decodeLiteral stringLiteral) (encodeList (encodeLiteral CharLiteral)) )
    , ( "String", "toLower", eval1 String.toLower (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "toUpper", eval1 String.toUpper (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "trim", eval1 String.trim (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "trimLeft", eval1 String.trimLeft (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "trimRight", eval1 String.trimRight (decodeLiteral stringLiteral) (encodeLiteral StringLiteral) )
    , ( "String", "uncons", eval1 String.uncons (decodeLiteral stringLiteral) (encodeMaybe (encodeTuple2 ( encodeLiteral CharLiteral, encodeLiteral StringLiteral ))) )
    , ( "String", "words", eval1 String.words (decodeLiteral stringLiteral) (encodeList (encodeLiteral StringLiteral)) )
    ]
