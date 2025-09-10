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

import morphir.sdk.Bool.*

/**
 * Comparing ordered values.
 * 
 * These functions only work on `Comparable` types. This includes numbers,
 * characters, strings, lists of comparable things, and other comparable types.
 */
object ComparableUtils {
    
    /**
     * Represents the relative ordering of two things.
     * The relations are less than, equal to, and greater than.
     */
    enum class Order {
        LT, EQ, GT
    }
    
    /**
     * Check if the first value is less than the second.
     */
    fun <T : Comparable<T>> lessThan(a: T, b: T): Boolean = a < b

    /**
     * Check if the first value is greater than the second.
     */
    fun <T : Comparable<T>> greaterThan(a: T, b: T): Boolean = a > b

    /**
     * Check if the first value is less than or equal to the second.
     */
    fun <T : Comparable<T>> lessThanOrEqual(a: T, b: T): Boolean = a <= b

    /**
     * Check if the first value is greater than or equal to the second.
     */
    fun <T : Comparable<T>> greaterThanOrEqual(a: T, b: T): Boolean = a >= b
    
    /**
     * Find the smaller of two comparables.
     * 
     * ```kotlin
     * ComparableUtils.min(42, 12345678) == 42
     * ComparableUtils.min("abc", "xyz") == "abc"
     * ```
     */
    fun <T : Comparable<T>> min(a: T, b: T): T = if (a <= b) a else b
    
    /**
     * Find the larger of two comparables.
     * 
     * ```kotlin
     * ComparableUtils.max(42, 12345678) == 12345678
     * ComparableUtils.max("abc", "xyz") == "xyz"
     * ```
     */
    fun <T : Comparable<T>> max(a: T, b: T): T = if (a >= b) a else b
    
    /**
     * Compare any two comparable values. Comparable values include `String`,
     * `Char`, `Int`, `Double`, or other types implementing Comparable.
     * 
     * ```kotlin
     * ComparableUtils.compare(3, 4) == Order.LT
     * ComparableUtils.compare(4, 4) == Order.EQ
     * ComparableUtils.compare(5, 4) == Order.GT
     * ```
     */
    fun <T : Comparable<T>> compare(a: T, b: T): Order = when {
        a < b -> Order.LT
        a > b -> Order.GT
        else -> Order.EQ
    }
}
