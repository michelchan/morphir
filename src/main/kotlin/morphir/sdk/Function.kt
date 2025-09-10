/*
Copyright 2020 Morgan Stanley

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package morphir.sdk

/**
 * Function helpers for Morphir.
 */
object Function {
    
    /**
     * Function composition, passing results along in the suggested direction. For
     * example, the following code checks if the square root of a number is odd:
     * 
     * ```kotlin
     * val checkOddSqrt = Function.composeLeft(::not) { Function.composeLeft(::isEven, ::sqrt)(it) }
     * ```
     * 
     * You can think of this function as equivalent to the following:
     * 
     * ```kotlin
     * val composed = { x -> g(f(x)) }
     * ```
     * 
     * So our example expands out to something like this:
     * 
     * ```kotlin
     * val result = { n -> not(isEven(sqrt(n))) }
     * ```
     */
    fun <A, B, C> composeLeft(g: (B) -> C, f: (A) -> B): (A) -> C = { x -> g(f(x)) }
    
    /**
     * Function composition, passing results along in the suggested direction. For
     * example, the following code checks if the square root of a number is odd:
     * 
     * ```kotlin
     * val checkOddSqrt = Function.composeRight(::sqrt) { Function.composeRight(::isEven, ::not)(it) }
     * ```
     */
    fun <A, B, C> composeRight(f: (A) -> B, g: (B) -> C): (A) -> C = { x -> g(f(x)) }
    
    /**
     * Given a value, returns exactly the same value. This is called
     * [the identity function](https://en.wikipedia.org/wiki/Identity_function).
     */
    fun <T> identity(x: T): T = x
    
    /**
     * Create a function that _always_ returns the same value. Useful with
     * functions like `map`:
     * 
     * ```kotlin
     * listOf(1, 2, 3, 4, 5).map(Function.always(0)) == listOf(0, 0, 0, 0, 0)
     * ```
     */
    fun <A, B> always(a: A): (B) -> A = { _ -> a }
}