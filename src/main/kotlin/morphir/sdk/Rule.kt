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
 * This module supports defining business logic as a set of rules. You can think of it as a functional rules engine.
 * The logic is built up of rules that are composed into rule sets. In traditional rules engines these rule sets can be
 * executed in a variety of ways that can yield different results. Morphir prefers predictability over flexibility so we
 * only support sequential execution. While this might sound limiting it greatly improves readability and enforces modelers
 * to break up large rule sets into smaller targeted ones.
 */
object Rule {
    
    /**
     * Type that represents a single rule. A rule is a function that is only applicable on certain inputs. In other words
     * it's a partial-function. Since Kotlin supports nullable types, it is represented as a function that returns
     * a nullable value. When the function is applicable it will return a non-null value, otherwise `null`.
     */
    typealias Rule<A, B> = (A) -> B?
    
    /**
     * Chain a list of rules into a single rule. Rules are evaluated sequentially in the order they were supplied and
     * the first rule that matches will be applied.
     * 
     * ```kotlin
     * val myChain = Rule.chain(listOf(
     *     { _: Int -> null }, // A rule that never matches
     *     { a: Int -> a }     // A rule that always matches and returns the original value
     * ))
     * 
     * myChain(42) == 42
     * ```
     */
    fun <A, B> chain(rules: kotlin.collections.List<Rule<A, B>>): Rule<A, B> = { input ->
        rules.firstNotNullOfOrNull { rule -> rule(input) }
    }
    
    /**
     * Simply returns true for any input. Use as a wildcard in a decision table.
     */
    fun <T> any(value: T): Boolean = true
    
    /**
     * Returns `true` only if the second argument is equal to the first. Use in a decision table for exact match.
     */
    fun <T> `is`(expected: T): (T) -> Boolean = { actual -> expected == actual }
    
    /**
     * Returns `true` only if the second argument can be found in the list specified in the first argument. Use in a
     * decision table to match when a value is in a predefined set.
     */
    fun <T> anyOf(list: kotlin.collections.List<T>): (T) -> Boolean = { value -> list.contains(value) }
    
    /**
     * Returns `true` only if the second argument cannot be found in the list specified in the first argument. Use in a
     * decision table to match when a value is not in a predefined set.
     */
    fun <T> noneOf(list: kotlin.collections.List<T>): (T) -> Boolean = { value -> !list.contains(value) }
}