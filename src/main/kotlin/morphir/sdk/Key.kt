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
 * Helpers to work with composite keys.
 * 
 * ## Motivation
 * 
 * It can be useful to create composite keys from multiple fields in business modeling.
 * In Kotlin, we can use data classes and tuples (Pair, Triple) to represent composite keys.
 * This module provides utilities for working with various key sizes.
 */
object Key {
    
    /**
     * Type that represents a zero element key.
     */
    typealias Key0 = Unit
    
    /**
     * Type that represents a composite key with 2 elements.
     */
    typealias Key2<K1, K2> = Pair<K1, K2>
    
    /**
     * Type that represents a composite key with 3 elements.
     */
    typealias Key3<K1, K2, K3> = Triple<K1, K2, K3>
    
    /**
     * Data class for 4-element keys
     */
    data class Key4<K1, K2, K3, K4>(val k1: K1, val k2: K2, val k3: K3, val k4: K4) : Comparable<Key4<K1, K2, K3, K4>>
        where K1 : Comparable<K1>, K2 : Comparable<K2>, K3 : Comparable<K3>, K4 : Comparable<K4> {
        override fun compareTo(other: Key4<K1, K2, K3, K4>): Int {
            var result = k1.compareTo(other.k1)
            if (result != 0) return result
            result = k2.compareTo(other.k2)
            if (result != 0) return result
            result = k3.compareTo(other.k3)
            if (result != 0) return result
            return k4.compareTo(other.k4)
        }
    }
    
    /**
     * Data class for 5-element keys
     */
    data class Key5<K1, K2, K3, K4, K5>(val k1: K1, val k2: K2, val k3: K3, val k4: K4, val k5: K5) : Comparable<Key5<K1, K2, K3, K4, K5>>
        where K1 : Comparable<K1>, K2 : Comparable<K2>, K3 : Comparable<K3>, K4 : Comparable<K4>, K5 : Comparable<K5> {
        override fun compareTo(other: Key5<K1, K2, K3, K4, K5>): Int {
            var result = k1.compareTo(other.k1)
            if (result != 0) return result
            result = k2.compareTo(other.k2)
            if (result != 0) return result
            result = k3.compareTo(other.k3)
            if (result != 0) return result
            result = k4.compareTo(other.k4)
            if (result != 0) return result
            return k5.compareTo(other.k5)
        }
    }
    
    /**
     * Creates a key with zero elements.
     */
    fun <T> noKey(value: T): Key0 = Unit
    
    /**
     * Creates a key with zero elements.
     */
    fun <T> key0(value: T): Key0 = Unit
    
    /**
     * Create a composite key with 2 elements.
     */
    fun <T, K1, K2> key2(getKey1: (T) -> K1, getKey2: (T) -> K2): (T) -> Key2<K1, K2> = 
        { value -> Pair(getKey1(value), getKey2(value)) }
    
    /**
     * Create a composite key with 3 elements.
     */
    fun <T, K1, K2, K3> key3(getKey1: (T) -> K1, getKey2: (T) -> K2, getKey3: (T) -> K3): (T) -> Key3<K1, K2, K3> = 
        { value -> Triple(getKey1(value), getKey2(value), getKey3(value)) }
    
    /**
     * Create a composite key with 4 elements.
     */
    fun <T, K1, K2, K3, K4> key4(
        getKey1: (T) -> K1, 
        getKey2: (T) -> K2, 
        getKey3: (T) -> K3, 
        getKey4: (T) -> K4
    ): (T) -> Key4<K1, K2, K3, K4> where K1 : Comparable<K1>, K2 : Comparable<K2>, K3 : Comparable<K3>, K4 : Comparable<K4> = 
        { value -> Key4(getKey1(value), getKey2(value), getKey3(value), getKey4(value)) }
    
    /**
     * Create a composite key with 5 elements.
     */
    fun <T, K1, K2, K3, K4, K5> key5(
        getKey1: (T) -> K1, 
        getKey2: (T) -> K2, 
        getKey3: (T) -> K3, 
        getKey4: (T) -> K4, 
        getKey5: (T) -> K5
    ): (T) -> Key5<K1, K2, K3, K4, K5> where K1 : Comparable<K1>, K2 : Comparable<K2>, K3 : Comparable<K3>, K4 : Comparable<K4>, K5 : Comparable<K5> = 
        { value -> Key5(getKey1(value), getKey2(value), getKey3(value), getKey4(value), getKey5(value)) }
}