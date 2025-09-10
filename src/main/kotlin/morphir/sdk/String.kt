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
 * Utilities to extend the basic String operations.
 * 
 * Constraints provide a way to add value level constraints to types that are stored as Strings.
 * While the Kotlin compiler doesn't support checking such constraints statically, we can use the 
 * information in Morphir backends to generate more specific types. For example:
 * 
 * ```kotlin
 * data class Trade(
 *     val productID: Cusip,
 *     val comment: Comment?
 * )
 * 
 * data class Cusip(val value: String)
 * 
 * val cusip = { value: String -> String.ofLength(9, ::Cusip, value) }
 * 
 * data class Comment(val value: String)
 * 
 * val comment = { value: String -> String.ofMaxLength(100, ::Comment, value) }
 * ```
 */
object String {
    
    /**
     * Checks the exact length of a string and wraps it using the specified constructor.
     * 
     * ```kotlin
     * data class Currency(val value: String)
     * val currency = { value: String -> String.ofLength(3, ::Currency, value) }
     * 
     * currency("USD") == Currency("USD")
     * currency("US") == null
     * currency("LONG") == null
     * ```
     */
    fun <T> ofLength(length: Int, constructor: (String) -> T, value: String): T? {
        return if (value.length == length) {
            constructor(value)
        } else {
            null
        }
    }
    
    /**
     * Checks the max length of a string and wraps it using the specified constructor.
     * 
     * ```kotlin
     * data class Name(val value: String)
     * val name = { value: String -> String.ofMaxLength(15, ::Name, value) }
     * 
     * name("") == Name("")
     * name("A name") == Name("A name")
     * name("A very long name") == null
     * ```
     */
    fun <T> ofMaxLength(maxLength: Int, constructor: (String) -> T, value: String): T? {
        return if (value.length <= maxLength) {
            constructor(value)
        } else {
            null
        }
    }
    
    /**
     * Checks to see if two strings are equal ignoring case.
     * 
     * ```kotlin
     * val isEqual = String.equalIgnoreCase("HeLlO", "Hello")
     * isEqual == true
     * ```
     */
    fun equalIgnoreCase(str1: String, str2: String): Boolean {
        return if (str1.length != str2.length) {
            false
        } else {
            str1.lowercase() == str2.lowercase()
        }
    }
}