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
 * This module provides convenient but unsafe operations that the tooling will translate into safe operation in the
 * runtime environment.
 * 
 * Example usage:
 * ```kotlin
 * data class Input(
 *     val name: String?,
 *     val age: String
 * )
 * 
 * data class Validated(
 *     val name: String,
 *     val age: Int
 * )
 * 
 * fun validate(input: Input): Validated = Validated(
 *     name = Validate.required(input.name),
 *     age = Validate.parse(input.age)
 * )
 * ```
 */
object Validate {
    
    /**
     * Indicate that a value is required without handling missing values.
     * 
     * ```kotlin
     * Validate.required("value") == "value"
     * Validate.required(null) // throws exception in Kotlin
     * ```
     */
    fun <T> required(maybe: T?): T = maybe ?: throw IllegalArgumentException("Required value not available")
    
    /**
     * Indicate that the String value should be parsed into a specific type without specifying how.
     * 
     * **Important:** This is a placeholder that should be replaced by the Morphir tooling!
     * 
     * ```kotlin
     * val result: Int = Validate.parse("11") // This would fail at runtime without tooling support
     * ```
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> parse(string: String): T = 
        throw NotImplementedError("Don't know how to parse '$string' into expected type")
}