module Morphir.SDK.UUIDTests exposing (..)

import Json.Encode as JE exposing (..)
import Morphir.SDK.Json.Encode as J
import Test exposing (..)
import Expect


encodeTests : Test
encodeTests =
    describe "encode json"
        [ test "empty string to string" <|
            \_ ->
                Expect.equal (U.nilString) (UUID.nilString)
        , test "test is nil string" <|
            \_ ->
                Expect.equal (U.nilString |> U.isNilString) (UUID.nilString |> UUID.isNilString)
        ]


stringTests : Test
stringTests =
    describe "string to json"
        [ test "empty string to json" <|
            \_ ->
                Expect.equal (J.string "") (JE.string "")
        , test "test regular string" <|
            \_ ->
                Expect.equal (J.string "Hello world") (JE.string "Hello world")
        ]


intTests : Test
intTests =
    describe "int to json"
        [ test "negative int to json" <|
            \_ ->
                Expect.equal (J.int -9999) (JE.int -9999)
        , test "zero to json" <|
            \_ ->
                Expect.equal (J.int 0) (JE.int 0)
        , test "positive int to json" <|
            \_ ->
                Expect.equal (J.int 1234) (JE.int 1234)


floatTests : Test
floatTests =
    describe "float to json"
        [ test "negative float to json" <|
            \_ ->
                Expect.equal (J.float -0.124) (JE.float -0.124)
        , test "zero to json" <|
            \_ ->
                Expect.equal (J.float 0) (JE.float 0)
        , test "positive float to json" <|
            \_ ->
                Expect.equal (J.float 99.9) (JE.float 99.9)


boolTests : Test
boolTests =
    describe "bool to json"
        [ test "true to json" <|
            \_ ->
                Expect.equal (J.bool True) (JE.bool True)
        , test "false to json" <|
            \_ ->
                Expect.equal (J.bool False) (JE.bool False)
        ]