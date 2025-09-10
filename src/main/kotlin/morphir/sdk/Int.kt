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

import java.math.BigInteger

/**
 * This module adds support for fixed precision integers. We intentionally limit the operations 
 * that you can do on fixed precision integers because in general it's difficult to decide whether 
 * a calculation will result in an overflow in the general case. At the same time having the ability 
 * to encode fixed precision integers in your domain model can be very useful. This module allows 
 * you to do that while making sure that you can only use arbitrary precision integers in your calculation.
 * 
 * Example use:
 * 
 * ```kotlin
 * fun calc(a: Int8, b: Int16): Int8? {
 *     val arbA = Int.fromInt8(a)
 *     val arbB = Int.fromInt16(b)
 *     
 *     return (arbA * arbB).let { result ->
 *         Int.toInt8(result) ?: Int8(0)
 *     }
 * }
 * ```
 * 
 * The above example shows how the user is required to either provide a default value or change the 
 * return type of the function to handle cases where the calculation might return a value that doesn't 
 * fit in the 8 bit precision.
 */
object Int {
    
    /**
     * Represents an 8 bit integer value.
     */
    data class Int8(val value: kotlin.Int)
    
    /**
     * Turn an 8 bit integer value into an arbitrary precision integer to use in calculations.
     */
    fun fromInt8(int8: Int8): BigInteger = BigInteger.valueOf(int8.value.toLong())
    
    /**
     * Turn an arbitrary precision integer into an 8 bit integer if it fits within the precision.
     */
    fun toInt8(value: BigInteger): Int8? {
        return if (value < BigInteger.valueOf(-128) || value > BigInteger.valueOf(127)) {
            null
        } else {
            Int8(value.toInt())
        }
    }
    
    /**
     * Represents a 16 bit integer value.
     */
    data class Int16(val value: kotlin.Int)
    
    /**
     * Turn a 16 bit integer value into an arbitrary precision integer to use in calculations.
     */
    fun fromInt16(int16: Int16): BigInteger = BigInteger.valueOf(int16.value.toLong())
    
    /**
     * Turn an arbitrary precision integer into a 16 bit integer if it fits within the precision.
     */
    fun toInt16(value: BigInteger): Int16? {
        return if (value < BigInteger.valueOf(-32768) || value > BigInteger.valueOf(32767)) {
            null
        } else {
            Int16(value.toInt())
        }
    }
    
    /**
     * Represents a 32 bit integer value.
     */
    data class Int32(val value: kotlin.Int)
    
    /**
     * Turn a 32 bit integer value into an arbitrary precision integer to use in calculations.
     */
    fun fromInt32(int32: Int32): BigInteger = BigInteger.valueOf(int32.value.toLong())
    
    /**
     * Turn an arbitrary precision integer into a 32 bit integer if it fits within the precision.
     */
    fun toInt32(value: BigInteger): Int32? {
        return if (value < BigInteger.valueOf(kotlin.Int.MIN_VALUE.toLong()) || 
                   value > BigInteger.valueOf(kotlin.Int.MAX_VALUE.toLong())) {
            null
        } else {
            Int32(value.toInt())
        }
    }
    
    /**
     * Represents a 64 bit integer value.
     */
    data class Int64(val value: Long)
    
    /**
     * Turn a 64 bit integer value into an arbitrary precision integer to use in calculations.
     */
    fun fromInt64(int64: Int64): BigInteger = BigInteger.valueOf(int64.value)
    
    /**
     * Turn an arbitrary precision integer into a 64 bit integer if it fits within the precision.
     */
    fun toInt64(value: BigInteger): Int64? {
        return if (value < BigInteger.valueOf(Long.MIN_VALUE) || 
                   value > BigInteger.valueOf(Long.MAX_VALUE)) {
            null
        } else {
            Int64(value.toLong())
        }
    }
}