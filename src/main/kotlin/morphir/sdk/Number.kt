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
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * This module provides a way to represent a number without the risk of rounding issues or division by zero for any of
 * the basic operations: `+`, `-`, `*`, `/`. More accurately a `Number` represents an arbitrary-precision rational number.
 * If you need irrational numbers please use a `Double`.
 */
sealed class Number {
    data class Rational(val numerator: BigInteger, val denominator: BigInteger) : Number()
}

/**
 * Exception thrown when division by zero is attempted.
 */
class DivisionByZeroException : Exception("Division by zero")

object NumberUtils {
    
    /**
     * Create a Number by converting it from an Int
     */
    fun fromInt(value: Int): Number.Rational = Number.Rational(BigInteger.valueOf(value.toLong()), BigInteger.ONE)
    
    /**
     * Turn a number into a decimal.
     * NOTE: it is possible for this operation to fail if the Number is a rational number for 0.
     */
    fun toDecimal(number: Number): BigDecimal? = when (number) {
        is Number.Rational -> {
            try {
                if (number.denominator == BigInteger.ZERO) null
                else BigDecimal(number.numerator).divide(BigDecimal(number.denominator), MathContext.DECIMAL128)
            } catch (e: ArithmeticException) {
                null
            }
        }
    }
    
    /**
     * Turn a number into a decimal, by providing a default value in the case things go awry.
     */
    fun coerceToDecimal(default: BigDecimal, number: Number): BigDecimal = 
        toDecimal(number) ?: default
    
    /**
     * Checks if two numbers are equal.
     * 
     * ```kotlin
     * equal(one, one) == true
     * equal(one, divide(ten, ten).getOrThrow()) == true
     * equal(one, zero) == false
     * ```
     */
    fun equal(a: Number, b: Number): Boolean = when (a) {
        is Number.Rational -> when (b) {
            is Number.Rational -> {
                val left = a.numerator * b.denominator
                val right = a.denominator * b.numerator
                left == right
            }
        }
    }
    
    /**
     * Checks if two numbers are not equal.
     * 
     * ```kotlin
     * notEqual(one, zero) == true
     * notEqual(zero, one) == true
     * notEqual(one, one) == false
     * ```
     */
    fun notEqual(a: Number, b: Number): Boolean = !equal(a, b)
    
    /**
     * Checks if the first number is less than the second
     */
    fun lessThan(a: Number, b: Number): Boolean = when (a) {
        is Number.Rational -> when (b) {
            is Number.Rational -> {
                val left = a.numerator * b.denominator
                val right = a.denominator * b.numerator
                left < right
            }
        }
    }
    
    /**
     * Checks if the first number is less than or equal to the second
     */
    fun lessThanOrEqual(a: Number, b: Number): Boolean = lessThan(a, b) || equal(a, b)
    
    /**
     * Checks if the first number is greater than the second
     */
    fun greaterThan(a: Number, b: Number): Boolean = when (a) {
        is Number.Rational -> when (b) {
            is Number.Rational -> {
                val left = a.numerator * b.denominator
                val right = a.denominator * b.numerator
                left > right
            }
        }
    }
    
    /**
     * Checks if the first number is greater or equal than the second
     */
    fun greaterThanOrEqual(a: Number, b: Number): Boolean = greaterThan(a, b) || equal(a, b)
    
    /**
     * Negate the given number, thus flipping the sign.
     */
    fun negate(number: Number): Number = when (number) {
        is Number.Rational -> Number.Rational(number.numerator.negate(), number.denominator)
    }
    
    /**
     * Takes the absolute value of the number
     */
    fun abs(number: Number): Number = when (number) {
        is Number.Rational -> Number.Rational(number.numerator.abs(), number.denominator.abs())
    }
    
    /**
     * Calculates the reciprocal of the number
     */
    fun reciprocal(number: Number): Number = when (number) {
        is Number.Rational -> {
            if (isZero(number)) number
            else Number.Rational(number.denominator, number.numerator)
        }
    }
    
    /**
     * Adds two numbers together.
     */
    fun add(a: Number, b: Number): Number = when (a) {
        is Number.Rational -> when (b) {
            is Number.Rational -> {
                val newNumerator = (a.numerator * b.denominator) + (a.denominator * b.numerator)
                val newDenominator = a.denominator * b.denominator
                Number.Rational(newNumerator, newDenominator)
            }
        }
    }
    
    /**
     * Subtracts one number from the other.
     */
    fun subtract(a: Number, b: Number): Number = when (a) {
        is Number.Rational -> when (b) {
            is Number.Rational -> {
                val newNumerator = (a.numerator * b.denominator) - (a.denominator * b.numerator)
                val newDenominator = a.denominator * b.denominator
                Number.Rational(newNumerator, newDenominator)
            }
        }
    }
    
    /**
     * Multiplies two numbers together
     */
    fun multiply(a: Number, b: Number): Number = when (a) {
        is Number.Rational -> when (b) {
            is Number.Rational -> {
                Number.Rational(a.numerator * b.numerator, a.denominator * b.denominator)
            }
        }
    }
    
    /**
     * Division
     */
    fun divide(a: Number, b: Number): Result<Number> = when (a) {
        is Number.Rational -> when (b) {
            is Number.Rational -> {
                if (isZero(b)) {
                    Result.failure(DivisionByZeroException())
                } else {
                    Result.success(Number.Rational(a.numerator * b.denominator, a.denominator * b.numerator))
                }
            }
        }
    }
    
    /**
     * Tries to simplify the number.
     */
    fun simplify(number: Number): Number? = when (number) {
        is Number.Rational -> {
            if (number.denominator == BigInteger.ZERO) null
            else {
                val gcd = number.numerator.gcd(number.denominator)
                val newNumerator = number.numerator.divide(gcd)
                val newDenominator = number.denominator.divide(gcd)
                Number.Rational(newNumerator, newDenominator)
            }
        }
    }
    
    /**
     * Tells if the number is simplified
     */
    fun isSimplified(number: Number): Boolean = when (number) {
        is Number.Rational -> {
            val simplified = simplify(number)
            simplified?.let { 
                when (it) {
                    is Number.Rational -> it.numerator == number.numerator && it.denominator == number.denominator
                }
            } ?: true
        }
    }
    
    /**
     * Create a fractional representation of the number
     */
    fun toFractionalString(number: Number): String = when (number) {
        is Number.Rational -> "${number.numerator}/${number.denominator}"
    }
    
    private fun isZero(number: Number): Boolean = when (number) {
        is Number.Rational -> number.numerator == BigInteger.ZERO
    }
    
    /**
     * Constant for 0
     */
    val zero: Number.Rational = Number.Rational(BigInteger.ZERO, BigInteger.ONE)
    
    /**
     * Constant for one
     */
    val one: Number.Rational = Number.Rational(BigInteger.ONE, BigInteger.ONE)
}