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
 * A dictionary mapping unique keys to values. The keys can be any comparable
 * type. This includes `Int`, `Double`, `String`, and other comparable types.
 * 
 * Insert, remove, and query operations all take _O(log n)_ time.
 * 
 * In Kotlin, we use Map<K, V> which provides similar functionality.
 */
object Dict {
    
    /**
     * Create an empty dictionary.
     */
    fun <K, V> empty(): Map<K, V> = emptyMap()
    
    /**
     * Get the value associated with a key. If the key is not found, return
     * `null`. This is useful when you are not sure if a key will be in the
     * dictionary.
     * 
     * ```kotlin
     * val animals = mapOf("Tom" to "Cat", "Jerry" to "Mouse")
     * 
     * Dict.get("Tom", animals) == "Cat"
     * Dict.get("Jerry", animals) == "Mouse"
     * Dict.get("Spike", animals) == null
     * ```
     */
    fun <K, V> get(key: K, dict: Map<K, V>): V? = dict[key]
    
    /**
     * Determine if a key is in a dictionary.
     */
    fun <K, V> member(key: K, dict: Map<K, V>): Boolean = dict.containsKey(key)
    
    /**
     * Determine the number of key-value pairs in the dictionary.
     */
    fun <K, V> size(dict: Map<K, V>): Int = dict.size
    
    /**
     * Determine if a dictionary is empty.
     * 
     * ```kotlin
     * Dict.isEmpty(Dict.empty<String, String>()) == true
     * ```
     */
    fun <K, V> isEmpty(dict: Map<K, V>): Boolean = dict.isEmpty()
    
    /**
     * Insert a key-value pair into a dictionary. Replaces value when there is
     * a collision.
     */
    fun <K, V> insert(key: K, value: V, dict: Map<K, V>): Map<K, V> = 
        dict.toMutableMap().apply { put(key, value) }.toMap()
    
    /**
     * Remove a key-value pair from a dictionary. If the key is not found,
     * no changes are made.
     */
    fun <K, V> remove(key: K, dict: Map<K, V>): Map<K, V> =
        dict.toMutableMap().apply { remove(key) }.toMap()
    
    /**
     * Update the value of a dictionary for a specific key with a given function.
     */
    fun <K, V> update(key: K, transform: (V?) -> V?, dict: Map<K, V>): Map<K, V> {
        val currentValue = dict[key]
        val newValue = transform(currentValue)
        return if (newValue != null) {
            dict.toMutableMap().apply { put(key, newValue) }.toMap()
        } else {
            dict.toMutableMap().apply { remove(key) }.toMap()
        }
    }
    
    /**
     * Create a dictionary with one key-value pair.
     */
    fun <K, V> singleton(key: K, value: V): Map<K, V> = mapOf(key to value)
    
    /**
     * Combine two dictionaries. If there is a collision, preference is given
     * to the first dictionary.
     */
    fun <K, V> union(dict1: Map<K, V>, dict2: Map<K, V>): Map<K, V> = dict2 + dict1
    
    /**
     * Keep a key-value pair when its key appears in the second dictionary.
     * Preference is given to values in the first dictionary.
     */
    fun <K, V> intersect(dict1: Map<K, V>, dict2: Map<K, V>): Map<K, V> =
        dict1.filter { (key, _) -> dict2.containsKey(key) }
    
    /**
     * Keep a key-value pair when its key does not appear in the second dictionary.
     */
    fun <K, V, W> diff(dict1: Map<K, V>, dict2: Map<K, W>): Map<K, V> =
        dict1.filter { (key, _) -> !dict2.containsKey(key) }
    
    /**
     * The most general way of combining two dictionaries. You provide three
     * accumulators for when a given key appears:
     * 
     * 1. Only in the left dictionary.
     * 2. In both dictionaries.
     * 3. Only in the right dictionary.
     * 
     * You then traverse all the keys from lowest to highest, building up whatever
     * you want.
     */
    fun <K : Comparable<K>, A, B, Result> merge(
        leftOnly: (K, A, Result) -> Result,
        both: (K, A, B, Result) -> Result,
        rightOnly: (K, B, Result) -> Result,
        dict1: Map<K, A>,
        dict2: Map<K, B>,
        initialResult: Result
    ): Result {
        val allKeys = (dict1.keys + dict2.keys).sorted()
        return allKeys.fold(initialResult) { acc, key ->
            val value1 = dict1[key]
            val value2 = dict2[key]
            when {
                value1 != null && value2 != null -> both(key, value1, value2, acc)
                value1 != null -> leftOnly(key, value1, acc)
                value2 != null -> rightOnly(key, value2, acc)
                else -> acc
            }
        }
    }
    
    /**
     * Apply a function to all values in a dictionary.
     */
    fun <K, A, B> map(transform: (K, A) -> B, dict: Map<K, A>): Map<K, B> =
        dict.mapValues { (key, value) -> transform(key, value) }
    
    /**
     * Fold over the key-value pairs in a dictionary from lowest key to highest key.
     */
    fun <K : Comparable<K>, V, B> foldl(combine: (K, V, B) -> B, initial: B, dict: Map<K, V>): B =
        dict.toList().sortedBy { it.first }.fold(initial) { acc, (key, value) ->
            combine(key, value, acc)
        }
    
    /**
     * Fold over the key-value pairs in a dictionary from highest key to lowest key.
     */
    fun <K : Comparable<K>, V, B> foldr(combine: (K, V, B) -> B, initial: B, dict: Map<K, V>): B =
        dict.toList().sortedByDescending { it.first }.fold(initial) { acc, (key, value) ->
            combine(key, value, acc)
        }
    
    /**
     * Keep only the key-value pairs that pass the given test.
     */
    fun <K, V> filter(predicate: (K, V) -> Boolean, dict: Map<K, V>): Map<K, V> =
        dict.filter { (key, value) -> predicate(key, value) }
    
    /**
     * Partition a dictionary according to some test. The first dictionary
     * contains all key-value pairs which passed the test, and the second contains
     * the pairs that did not.
     */
    fun <K, V> partition(predicate: (K, V) -> Boolean, dict: Map<K, V>): Pair<Map<K, V>, Map<K, V>> =
        dict.partition { (key, value) -> predicate(key, value) }.let { (passing, failing) ->
            Pair(passing, failing)
        }
    
    /**
     * Get all of the keys in a dictionary, sorted from lowest to highest.
     * 
     * ```kotlin
     * Dict.keys(mapOf(0 to "Alice", 1 to "Bob")) == listOf(0, 1)
     * ```
     */
    fun <K : Comparable<K>, V> keys(dict: Map<K, V>): kotlin.collections.List<K> = dict.keys.sorted()
    
    /**
     * Get all of the values in a dictionary, in the order of their keys.
     * 
     * ```kotlin
     * Dict.values(mapOf(0 to "Alice", 1 to "Bob")) == listOf("Alice", "Bob")
     * ```
     */
    fun <K : Comparable<K>, V> values(dict: Map<K, V>): kotlin.collections.List<V> =
        dict.toList().sortedBy { it.first }.map { it.second }
    
    /**
     * Convert a dictionary into an association list of key-value pairs, sorted by keys.
     */
    fun <K : Comparable<K>, V> toList(dict: Map<K, V>): kotlin.collections.List<Pair<K, V>> =
        dict.toList().sortedBy { it.first }
    
    /**
     * Convert an association list into a dictionary.
     */
    fun <K, V> fromList(pairs: kotlin.collections.List<Pair<K, V>>): Map<K, V> = pairs.toMap()
}