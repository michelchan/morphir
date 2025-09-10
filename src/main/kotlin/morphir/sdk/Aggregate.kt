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

import kotlin.math.max
import kotlin.math.min

/**
 * This module contains functions specifically designed to work with large data sets.
 */
object Aggregate {
    
    /**
     * Sealed class representing different aggregation operators.
     */
    sealed class Operator<A> {
        object Count : Operator<Any>()
        data class Sum<A>(val getValue: (A) -> Double) : Operator<A>()
        data class Avg<A>(val getValue: (A) -> Double) : Operator<A>()
        data class Min<A>(val getValue: (A) -> Double) : Operator<A>()
        data class Max<A>(val getValue: (A) -> Double) : Operator<A>()
        data class WAvg<A>(val getWeight: (A) -> Double, val getValue: (A) -> Double) : Operator<A>()
    }
    
    /**
     * Type that represents an aggregation on a type `A` with a key of `Key`. It encapsulates the following information:
     * 
     * - `key` is a function that gets the key of each `A`
     * - `filter` is a function used for filtering items out before the aggregation. This can be set to `{ true }` to not do any filtering.
     * - `operator` is the aggregation operation to apply (count, sum, average, ...)
     */
    data class Aggregation<A, Key>(
        val key: (A) -> Key,
        val filter: (A) -> Boolean,
        val operator: Operator<A>
    )
    
    /**
     * Count the number of rows in a group.
     */
    fun <A> count(): Aggregation<A, Unit> = Aggregation(
        key = { Unit },
        filter = { true },
        operator = Operator.Count
    )
    
    /**
     * Apply a function to each row that returns a numeric value and return the sum of the values.
     */
    fun <A> sumOf(getValue: (A) -> Double): Aggregation<A, Unit> = Aggregation(
        key = { Unit },
        filter = { true },
        operator = Operator.Sum(getValue)
    )
    
    /**
     * Apply a function to each row that returns a numeric value and return the average of the values.
     */
    fun <A> averageOf(getValue: (A) -> Double): Aggregation<A, Unit> = Aggregation(
        key = { Unit },
        filter = { true },
        operator = Operator.Avg(getValue)
    )
    
    /**
     * Apply a function to each row that returns a numeric value and return the minimum of the values.
     */
    fun <A> minimumOf(getValue: (A) -> Double): Aggregation<A, Unit> = Aggregation(
        key = { Unit },
        filter = { true },
        operator = Operator.Min(getValue)
    )
    
    /**
     * Apply a function to each row that returns a numeric value and return the maximum of the values.
     */
    fun <A> maximumOf(getValue: (A) -> Double): Aggregation<A, Unit> = Aggregation(
        key = { Unit },
        filter = { true },
        operator = Operator.Max(getValue)
    )
    
    /**
     * Apply two functions to each row that returns a numeric value and return the weighted average of the values using the first
     * function to get the weights.
     */
    fun <A> weightedAverageOf(getWeight: (A) -> Double, getValue: (A) -> Double): Aggregation<A, Unit> = 
        Aggregation(
            key = { Unit },
            filter = { true },
            operator = Operator.WAvg(getWeight, getValue)
        )
    
    /**
     * Changes the key of an aggregation.
     */
    fun <A, Key> byKey(keyFunc: (A) -> Key, aggregation: Aggregation<A, Unit>): Aggregation<A, Key> =
        aggregation.copy(key = keyFunc)
    
    /**
     * Adds a filter to an aggregation.
     */
    fun <A, Key> withFilter(filter: (A) -> Boolean, aggregation: Aggregation<A, Key>): Aggregation<A, Key> =
        aggregation.copy(filter = filter)
    
    /**
     * Group a list of items into a dictionary. Grouping is done using a function that returns a key for each item.
     * The resulting dictionary will use those keys as the key of each entry in the dictionary and values will be lists of
     * items for each key.
     */
    fun <A, Key> groupBy(getKey: (A) -> Key, list: kotlin.collections.List<A>): Map<Key, kotlin.collections.List<A>> =
        list.groupBy(getKey)
    
    /**
     * Aggregates a dictionary that contains lists of items as values into a list that contains exactly one item per key.
     */
    fun <A, Key, B> aggregate(
        transform: (Key, (Aggregation<A, Unit>) -> Double) -> B,
        dict: Map<Key, kotlin.collections.List<A>>
    ): kotlin.collections.List<B> =
        dict.map { (key, items) ->
            transform(key) { aggregation ->
                val filteredItems = items.filter(aggregation.filter)
                aggregateHelper(aggregation.operator, filteredItems)
            }
        }
    
    /**
     * Map function that provides an aggregated value to the mapping function.
     */
    fun <A, Key, B> aggregateMap(
        aggregation: Aggregation<A, Key>,
        transform: (Double, A) -> B,
        list: kotlin.collections.List<A>
    ): kotlin.collections.List<B> {
        val aggregated = list.filter(aggregation.filter)
            .groupBy(aggregation.key)
            .mapValues { (_, items) -> aggregateHelper(aggregation.operator, items) }
        
        return list.map { item ->
            val aggregatedValue = aggregated[aggregation.key(item)] ?: 0.0
            transform(aggregatedValue, item)
        }
    }
    
    private fun <A> aggregateHelper(operator: Operator<A>, items: kotlin.collections.List<A>): Double =
        when (operator) {
            is Operator.Count -> items.size.toDouble()
            is Operator.Sum -> items.sumOf(operator.getValue)
            is Operator.Avg -> if (items.isEmpty()) 0.0 else items.sumOf(operator.getValue) / items.size
            is Operator.Min -> items.minOfOrNull(operator.getValue) ?: 0.0
            is Operator.Max -> items.maxOfOrNull(operator.getValue) ?: 0.0
            is Operator.WAvg -> {
                val totalWeight = items.sumOf(operator.getWeight)
                if (totalWeight == 0.0) 0.0
                else items.sumOf { operator.getWeight(it) * operator.getValue(it) } / totalWeight
            }
        }
}