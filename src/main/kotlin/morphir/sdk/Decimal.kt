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

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.pow

/**
 * Utilities for working with arbitrary-precision decimal numbers.
 */
object Decimal {

    /**
     * Converts an Int to a Decimal
     */
    fun fromInt(value: Int): BigDecimal = BigDecimal.valueOf(value.toLong())

    /**
     * Converts a Double to a Decimal
     */
    fun fromFloat(value: Double): BigDecimal {
        val decimal = BigDecimal.valueOf(value)
        return if (decimal.compareTo(zero) == 0) zero else decimal
    }

    /**
     * Converts an Int to a Decimal that represents n hundreds.
     */
    fun hundred(n: Int): BigDecimal = BigDecimal.valueOf((100 * n).toLong())

    /**
     * Converts an Int to a Decimal that represents n thousands
     */
    fun thousand(n: Int): BigDecimal = BigDecimal.valueOf((1000 * n).toLong())

    /**
     * Converts an Int to a Decimal that represents n millions.
     */
    fun million(n: Int): BigDecimal = BigDecimal.valueOf((n * 1000000).toLong())

    /**
     * Converts an Int to a Decimal that represents n tenths.
     */
    fun tenth(n: Int): BigDecimal = BigDecimal.valueOf(n.toDouble() * 0.1)

    /**
     * Converts an Int to a Decimal that represents n hundredths.
     */
    fun hundredth(n: Int): BigDecimal = BigDecimal.valueOf(n.toDouble() * 0.01)

    /**
     * Converts an Int to a Decimal that represents n thousandths.
     */
    fun thousandth(n: Int): BigDecimal = BigDecimal.valueOf(n.toDouble() * 0.001)

    /**
     * Converts an Int to a Decimal that represents n basis points (i.e. 1/10 of % or a ten-thousandth
     */
    fun bps(n: Int): BigDecimal = BigDecimal.valueOf(n.toDouble() * 0.0001)

    /**
     * Converts an Int to a Decimal that represents n millionth.
     */
    fun millionth(n: Int): BigDecimal = BigDecimal.valueOf(n.toDouble() * 0.000001)

    /**
     * Converts a String to a Maybe Decimal. The string shall be in the format [<sign>]<numbers>[.<numbers>][e<numbers>]
     */
    fun fromString(str: String): BigDecimal? = try {
        BigDecimal(str)
    } catch (e: NumberFormatException) {
        null
    }

    /**
     * Converts a Decimal to a String
     */
    fun toString(decimal: BigDecimal): String = decimal.toString()

    /**
     * Addition
     */
    fun add(a: BigDecimal, b: BigDecimal): BigDecimal = a.add(b)

    /**
     * Subtraction
     */
    fun sub(a: BigDecimal, b: BigDecimal): BigDecimal = a.subtract(b)

    /**
     * Multiplication
     */
    fun mul(a: BigDecimal, b: BigDecimal): BigDecimal = a.multiply(b)

    /**
     * Divide two decimals
     */
    fun div(a: BigDecimal, b: BigDecimal): BigDecimal? = try {
        if (b.compareTo(BigDecimal.ZERO) == 0) null
        else a.divide(b, MathContext.DECIMAL128)
    } catch (e: ArithmeticException) {
        null
    }

    /**
     * Divide two decimals providing a default for the cases the calculation fails, such as divide by zero or overflow/underflow.
     */
    fun divWithDefault(default: BigDecimal, a: BigDecimal, b: BigDecimal): BigDecimal =
        div(a, b) ?: default

    /**
     * Shift the decimal n digits to the left.
     */
    fun shiftDecimalLeft(n: Int, value: BigDecimal): BigDecimal {
        val multiplier = BigDecimal.valueOf(10.0.pow(-n))
        return value.multiply(multiplier)
    }

    /**
     * Shift the decimal n digits to the right.
     */
    fun shiftDecimalRight(n: Int, value: BigDecimal): BigDecimal {
        val multiplier = BigDecimal.valueOf(10.0.pow(n))
        return value.multiply(multiplier)
    }

    /**
     * Changes the sign of a Decimal
     */
    fun negate(value: BigDecimal): BigDecimal = value.negate()

    /**
     * Truncates the Decimal to the nearest integer with `TowardsZero` mode
     */
    fun truncate(n: BigDecimal): BigDecimal = n.setScale(0, RoundingMode.DOWN)

    /**
     * `round` to the nearest integer.
     */
    fun round(n: BigDecimal): BigDecimal = n.setScale(0, RoundingMode.HALF_UP)

    /**
     * Absolute value (sets the sign as positive)
     */
    fun abs(value: BigDecimal): BigDecimal = value.abs()

    /**
     * Compares two Decimals
     */
    fun compare(a: BigDecimal, b: BigDecimal): Int = a.compareTo(b)

    /**
     * Equals
     */
    fun eq(a: BigDecimal, b: BigDecimal): Boolean = a.compareTo(b) == 0

    /**
     * Not equals
     */
    fun neq(a: BigDecimal, b: BigDecimal): Boolean = !eq(a, b)

    /**
     * Greater than
     */
    fun gt(a: BigDecimal, b: BigDecimal): Boolean = a.compareTo(b) > 0

    /**
     * Greater than or equals
     */
    fun gte(a: BigDecimal, b: BigDecimal): Boolean = a.compareTo(b) >= 0

    /**
     * Less than
     */
    fun lt(a: BigDecimal, b: BigDecimal): Boolean = a.compareTo(b) < 0

    /**
     * Less than or equals
     */
    fun lte(a: BigDecimal, b: BigDecimal): Boolean = a.compareTo(b) <= 0

    /**
     * The number 0
     */
    val zero: BigDecimal = BigDecimal.ZERO

    /**
     * The number 1
     */
    val one: BigDecimal = BigDecimal.ONE

    /**
     * The number -1
     */
    val minusOne: BigDecimal = BigDecimal.valueOf(-1)
}
