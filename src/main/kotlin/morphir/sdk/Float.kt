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

import kotlin.math.*

/**
 * A `Float` is a [floating-point number][fp]. Valid syntax for floats includes:
 * 
 *     0
 *     42
 *     3.14
 *     0.1234
 *     6.022e23   // == (6.022 * 10^23)
 *     6.022e+23  // == (6.022 * 10^23)
 *     1.602e−19  // == (1.602 * 10^-19)
 *     1e3        // == (1 * 10^3) == 1000
 * 
 * **Historical Note:** The particular details of floats (e.g. `NaN`) are
 * specified by [IEEE 754][ieee] which is literally hard-coded into almost all
 * CPUs in the world. That means if you think `NaN` is weird, you must
 * successfully overtake Intel and AMD with a chip that is not backwards
 * compatible with any widely-used assembly language.
 * 
 * [fp]: https://en.wikipedia.org/wiki/Floating-point_arithmetic
 * [ieee]: https://en.wikipedia.org/wiki/IEEE_754
 */
object Float {
    
    /**
     * Floating-point division:
     * 
     * ```kotlin
     * Float.divide(10.0, 4.0) == 2.5
     * Float.divide(11.0, 4.0) == 2.75
     * Float.divide(12.0, 4.0) == 3.0
     * Float.divide(13.0, 4.0) == 3.25
     * Float.divide(14.0, 4.0) == 3.5
     * Float.divide(-1.0, 4.0) == -0.25
     * Float.divide(-5.0, 4.0) == -1.25
     * ```
     */
    fun divide(a: Double, b: Double): Double = a / b
    
    /**
     * Convert an integer into a float. Useful when mixing `Int` and `Float`
     * values like this:
     * 
     * ```kotlin
     * fun halfOf(number: Int): Double = Float.fromInt(number) / 2
     * ```
     */
    fun fromInt(value: Int): Double = value.toDouble()
    
    /**
     * Round a number to the nearest integer.
     * 
     * ```kotlin
     * Float.round(1.0) == 1
     * Float.round(1.2) == 1
     * Float.round(1.5) == 2
     * Float.round(1.8) == 2
     * Float.round(-1.2) == -1
     * Float.round(-1.5) == -1
     * Float.round(-1.8) == -2
     * ```
     */
    fun round(value: Double): Int = kotlin.math.round(value).toInt()
    
    /**
     * Floor function, rounding down.
     * 
     * ```kotlin
     * Float.floor(1.0) == 1
     * Float.floor(1.2) == 1
     * Float.floor(1.5) == 1
     * Float.floor(1.8) == 1
     * Float.floor(-1.2) == -2
     * Float.floor(-1.5) == -2
     * Float.floor(-1.8) == -2
     * ```
     */
    fun floor(value: Double): Int = kotlin.math.floor(value).toInt()
    
    /**
     * Ceiling function, rounding up.
     * 
     * ```kotlin
     * Float.ceiling(1.0) == 1
     * Float.ceiling(1.2) == 2
     * Float.ceiling(1.5) == 2
     * Float.ceiling(1.8) == 2
     * Float.ceiling(-1.2) == -1
     * Float.ceiling(-1.5) == -1
     * Float.ceiling(-1.8) == -1
     * ```
     */
    fun ceiling(value: Double): Int = kotlin.math.ceil(value).toInt()
    
    /**
     * Truncate a number, rounding towards zero.
     * 
     * ```kotlin
     * Float.truncate(1.0) == 1
     * Float.truncate(1.2) == 1
     * Float.truncate(1.5) == 1
     * Float.truncate(1.8) == 1
     * Float.truncate(-1.2) == -1
     * Float.truncate(-1.5) == -1
     * Float.truncate(-1.8) == -1
     * ```
     */
    fun truncate(value: Double): Int = kotlin.math.truncate(value).toInt()
    
    /**
     * Take the square root of a number.
     * 
     * ```kotlin
     * Float.sqrt(4.0) == 2.0
     * Float.sqrt(9.0) == 3.0
     * Float.sqrt(16.0) == 4.0
     * Float.sqrt(25.0) == 5.0
     * ```
     */
    fun sqrt(value: Double): Double = kotlin.math.sqrt(value)
    
    /**
     * Calculate the logarithm of a number with a given base.
     * 
     * ```kotlin
     * Float.logBase(10.0, 100.0) == 2.0
     * Float.logBase(2.0, 256.0) == 8.0
     * ```
     */
    fun logBase(base: Double, number: Double): Double = kotlin.math.ln(number) / kotlin.math.ln(base)
    
    /**
     * An approximation of e.
     */
    val e: Double = kotlin.math.E
    
    /**
     * An approximation of pi.
     */
    val pi: Double = kotlin.math.PI
    
    /**
     * Figure out the cosine given an angle in radians.
     * 
     * ```kotlin
     * Float.cos(kotlin.math.PI / 3) == 0.5000000000000001
     * ```
     */
    fun cos(angle: Double): Double = kotlin.math.cos(angle)
    
    /**
     * Figure out the sine given an angle in radians.
     * 
     * ```kotlin
     * Float.sin(kotlin.math.PI / 6) == 0.49999999999999994
     * ```
     */
    fun sin(angle: Double): Double = kotlin.math.sin(angle)
    
    /**
     * Figure out the tangent given an angle in radians.
     * 
     * ```kotlin
     * Float.tan(kotlin.math.PI / 4) == 0.9999999999999999
     * ```
     */
    fun tan(angle: Double): Double = kotlin.math.tan(angle)
    
    /**
     * Figure out the arccosine for `adjacent / hypotenuse` in radians:
     * 
     * ```kotlin
     * Float.acos(1.0 / 2.0) == 1.0471975511965979 // 60° or pi/3 radians
     * ```
     */
    fun acos(value: Double): Double = kotlin.math.acos(value)
    
    /**
     * Figure out the arcsine for `opposite / hypotenuse` in radians:
     * 
     * ```kotlin
     * Float.asin(1.0 / 2.0) == 0.5235987755982989 // 30° or pi/6 radians
     * ```
     */
    fun asin(value: Double): Double = kotlin.math.asin(value)
    
    /**
     * This helps you find the angle (in radians) to an `(x,y)` coordinate, but
     * in a way that is rarely useful in programming. **You probably want
     * [atan2] instead!**
     * 
     * This version takes `y/x` as its argument, so there is no way to know whether
     * the negative signs comes from the `y` or `x` value. So as we go counter-clockwise
     * around the origin from point `(1,1)` to `(1,-1)` to `(-1,-1)` to `(-1,1)` we do
     * not get angles that go in the full circle:
     * 
     * ```kotlin
     * Float.atan(1.0 / 1.0) == 0.7853981633974483   //  45° or   pi/4 radians
     * Float.atan(1.0 / -1.0) == -0.7853981633974483  // 315° or 7*pi/4 radians
     * Float.atan(-1.0 / -1.0) == 0.7853981633974483  //  45° or   pi/4 radians
     * Float.atan(-1.0 / 1.0) == -0.7853981633974483  // 315° or 7*pi/4 radians
     * ```
     * 
     * Notice that everything is between `pi/2` and `-pi/2`. That is pretty useless
     * for figuring out angles in any sort of visualization, so again, check out
     * [atan2] instead!
     */
    fun atan(value: Double): Double = kotlin.math.atan(value)
    
    /**
     * This helps you find the angle (in radians) to an `(x,y)` coordinate.
     * So rather than saying `atan(y/x)` you say `atan2(y, x)` and you can get a full
     * range of angles:
     * 
     * ```kotlin
     * Float.atan2(1.0, 1.0) == 0.7853981633974483    //  45° or   pi/4 radians
     * Float.atan2(1.0, -1.0) == 2.356194490192345    // 135° or 3*pi/4 radians
     * Float.atan2(-1.0, -1.0) == -2.356194490192345  // 225° or 5*pi/4 radians
     * Float.atan2(-1.0, 1.0) == -0.7853981633974483  // 315° or 7*pi/4 radians
     * ```
     */
    fun atan2(y: Double, x: Double): Double = kotlin.math.atan2(y, x)
    
    /**
     * Determine whether a float is an undefined or unrepresentable number.
     * NaN stands for _not a number_ and it is [a standardized part of floating point
     * numbers](https://en.wikipedia.org/wiki/NaN).
     * 
     * ```kotlin
     * Float.isNaN(0.0 / 0.0) == true
     * Float.isNaN(kotlin.math.sqrt(-1.0)) == true
     * Float.isNaN(1.0 / 0.0) == false // infinity is a number
     * Float.isNaN(1.0) == false
     * ```
     */
    fun isNaN(value: Double): Boolean = value.isNaN()
    
    /**
     * Determine whether a float is positive or negative infinity.
     * 
     * ```kotlin
     * Float.isInfinite(0.0 / 0.0) == false
     * Float.isInfinite(kotlin.math.sqrt(-1.0)) == false
     * Float.isInfinite(1.0 / 0.0) == true
     * Float.isInfinite(1.0) == false
     * ```
     * 
     * Notice that NaN is not infinite! For float `n` to be finite implies that
     * `!(isInfinite(n) || isNaN(n))` evaluates to `true`.
     */
    fun isInfinite(value: Double): Boolean = value.isInfinite()
}