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
 * Boolean operations.
 * 
 * A "Boolean" value. It can either be `true` or `false`.
 */
object Bool {
    
    /**
     * Negate a boolean value.
     * 
     * ```kotlin
     * Bool.not(true) == false
     * Bool.not(false) == true
     * ```
     */
    fun not(value: Boolean): Boolean = !value
    
    /**
     * The logical AND operator. `true` if both inputs are `true`.
     * 
     * ```kotlin
     * Bool.and(true, true) == true
     * Bool.and(true, false) == false
     * Bool.and(false, true) == false
     * Bool.and(false, false) == false
     * ```
     */
    fun and(left: Boolean, right: Boolean): Boolean = left && right
    
    /**
     * The logical OR operator. `true` if one or both inputs are `true`.
     * 
     * ```kotlin
     * Bool.or(true, true) == true
     * Bool.or(true, false) == true
     * Bool.or(false, true) == true
     * Bool.or(false, false) == false
     * ```
     */
    fun or(left: Boolean, right: Boolean): Boolean = left || right
    
    /**
     * The exclusive-or operator. `true` if exactly one input is `true`.
     * 
     * ```kotlin
     * Bool.xor(true, true) == false
     * Bool.xor(true, false) == true
     * Bool.xor(false, true) == true
     * Bool.xor(false, false) == false
     * ```
     */
    fun xor(left: Boolean, right: Boolean): Boolean = left xor right
}
