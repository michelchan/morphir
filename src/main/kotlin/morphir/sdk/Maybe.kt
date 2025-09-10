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
 * This library fills a bunch of important niches in Kotlin. A nullable type (T?) can help
 * you with optional arguments, error handling, and records with optional fields.
 * 
 * In Kotlin, we use nullable types (T?) instead of Maybe<T> as they are native to the language.
 */
object Maybe {
    
    /**
     * Provide a default value, turning an optional value into a normal
     * value. This comes in handy when paired with functions which give back a nullable value.
     * 
     * ```kotlin
     * Maybe.withDefault(100, 42) // 42
     * Maybe.withDefault(100, null) // 100
     * 
     * Maybe.withDefault("unknown", map["Tom"]) // "unknown" if key not found
     * ```
     * 
     * **Note:** This can be overused! Many cases are better handled by a `when`
     * expression. And if you end up using `withDefault` a lot, it can be a good sign
     * that a sealed class will clean your code up quite a bit!
     */
    fun <T> withDefault(default: T, maybe: T?): T = maybe ?: default
    
    /**
     * Transform a nullable value with a given function:
     * 
     * ```kotlin
     * Maybe.map(::sqrt, 9.0) // 3.0
     * Maybe.map(::sqrt, null) // null
     * 
     * Maybe.map(::sqrt, "9".toDoubleOrNull()) // 3.0
     * Maybe.map(::sqrt, "x".toDoubleOrNull()) // null
     * ```
     */
    fun <A, B> map(transform: (A) -> B, maybe: A?): B? = maybe?.let(transform)
    
    /**
     * Apply a function if all the arguments are non-null values.
     * 
     * ```kotlin
     * Maybe.map2({ a, b -> a + b }, 3, 4) // 7
     * Maybe.map2({ a, b -> a + b }, 3, null) // null
     * Maybe.map2({ a, b -> a + b }, null, 4) // null
     * 
     * Maybe.map2({ a, b -> a + b }, "1".toIntOrNull(), "123".toIntOrNull()) // 124
     * Maybe.map2({ a, b -> a + b }, "x".toIntOrNull(), "123".toIntOrNull()) // null
     * Maybe.map2({ a, b -> a + b }, "1".toIntOrNull(), "1.3".toIntOrNull()) // null
     * ```
     */
    fun <A, B, R> map2(transform: (A, B) -> R, maybeA: A?, maybeB: B?): R? =
        if (maybeA != null && maybeB != null) transform(maybeA, maybeB) else null
    
    /**
     * Apply a function if all three arguments are non-null values.
     */
    fun <A, B, C, R> map3(transform: (A, B, C) -> R, maybeA: A?, maybeB: B?, maybeC: C?): R? =
        if (maybeA != null && maybeB != null && maybeC != null) transform(maybeA, maybeB, maybeC) else null
    
    /**
     * Apply a function if all four arguments are non-null values.
     */
    fun <A, B, C, D, R> map4(transform: (A, B, C, D) -> R, maybeA: A?, maybeB: B?, maybeC: C?, maybeD: D?): R? =
        if (maybeA != null && maybeB != null && maybeC != null && maybeD != null) 
            transform(maybeA, maybeB, maybeC, maybeD) 
        else null
    
    /**
     * Apply a function if all five arguments are non-null values.
     */
    fun <A, B, C, D, E, R> map5(
        transform: (A, B, C, D, E) -> R, 
        maybeA: A?, 
        maybeB: B?, 
        maybeC: C?, 
        maybeD: D?, 
        maybeE: E?
    ): R? =
        if (maybeA != null && maybeB != null && maybeC != null && maybeD != null && maybeE != null) 
            transform(maybeA, maybeB, maybeC, maybeD, maybeE) 
        else null
    
    /**
     * Chain together many computations that may fail. This is equivalent to flatMap.
     * 
     * This means we only continue with the callback if the value is non-null. For
     * example, say you need to parse some user input as a month:
     * 
     * ```kotlin
     * fun parseMonth(userInput: String): Int? =
     *     userInput.toIntOrNull()?.let(::toValidMonth)
     * 
     * fun toValidMonth(month: Int): Int? =
     *     if (month in 1..12) month else null
     * ```
     * 
     * In the `parseMonth` function, if `toIntOrNull` produces `null` (because
     * the `userInput` was not an integer) this entire chain of operations will
     * short-circuit and result in `null`. If `toValidMonth` results in `null`,
     * again the chain of computations will result in `null`.
     */
    fun <A, B> andThen(callback: (A) -> B?, maybe: A?): B? = maybe?.let(callback)
    
    /**
     * Check whether a nullable value has a value in it or not
     * 
     * ```kotlin
     * Maybe.hasValue(42) // true
     * Maybe.hasValue(null) // false
     * ```
     */
    fun <T> hasValue(maybe: T?): Boolean = maybe != null
}