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
 * This module contains operations that are specific to lists of results. These operations are very useful for modeling
 * processing pipelines where errors could happen at any point in the pipeline but they should not break the processing
 * itself.
 */
object ResultList {
    
    /**
     * Type that represents a list that contains a mix of failed and successful records.
     */
    typealias ResultList<E, A> = kotlin.collections.List<Result<A>>
    
    /**
     * Create a result list from any list.
     * 
     * ```kotlin
     * ResultList.fromList(listOf(1, 2, 3)) == listOf(Result.success(1), Result.success(2), Result.success(3))
     * ```
     */
    fun <E, A> fromList(list: kotlin.collections.List<A>): ResultList<E, A> = list.map { Result.success(it) }
    
    /**
     * Extract all errors from the result list.
     * 
     * ```kotlin
     * ResultList.errors(listOf(Result.success(1), Result.success(2))) == emptyList()
     * ResultList.errors(listOf(Result.success(1), Result.failure("foo"))) == listOf("foo")
     * ```
     */
    fun <E, A> errors(resultList: ResultList<E, A>): kotlin.collections.List<E> = 
        resultList.mapNotNull { result -> 
            if (result.isFailure) result.exceptionOrNull() as? E else null 
        }
    
    /**
     * Extract all successes from the result list.
     * 
     * ```kotlin
     * ResultList.successes(listOf(Result.success(1), Result.success(2))) == listOf(1, 2)
     * ResultList.successes(listOf(Result.success(1), Result.failure("foo"))) == listOf(1)
     * ```
     */
    fun <E, A> successes(resultList: ResultList<E, A>): kotlin.collections.List<A> = 
        resultList.mapNotNull { result -> result.getOrNull() }
    
    /**
     * Partition a result list into errors and successes.
     * 
     * ```kotlin
     * val (errors, successes) = ResultList.partition(listOf(Result.success(1), Result.failure("foo")))
     * errors == listOf("foo")
     * successes == listOf(1)
     * ```
     */
    fun <E, A> partition(resultList: ResultList<E, A>): Pair<kotlin.collections.List<E>, kotlin.collections.List<A>> {
        val errors = mutableListOf<E>()
        val successes = mutableListOf<A>()
        
        resultList.forEach { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { successes.add(it) }
            } else {
                (result.exceptionOrNull() as? E)?.let { errors.add(it) }
            }
        }
        
        return Pair(errors, successes)
    }
    
    /**
     * Filter a result list retaining all previously failed items.
     * 
     * ```kotlin
     * ResultList.filter({ it % 2 == 1 }, listOf(Result.success(1), Result.success(2), Result.success(3))) == 
     *     listOf(Result.success(1), Result.success(3))
     * ```
     */
    fun <E, A> filter(predicate: (A) -> Boolean, resultList: ResultList<E, A>): ResultList<E, A> = 
        resultList.filter { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let(predicate) ?: false
            } else {
                true // Keep all errors
            }
        }
    
    /**
     * Filter a result list where the filter function itself can fail.
     */
    fun <E, A> filterOrFail(predicate: (A) -> Result<Boolean>, resultList: ResultList<E, A>): ResultList<E, A> = 
        resultList.mapNotNull { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { value ->
                    when (val filterResult = predicate(value)) {
                        is Result<Boolean> -> {
                            if (filterResult.isSuccess) {
                                if (filterResult.getOrNull() == true) Result.success(value) else null
                            } else {
                                filterResult as Result<A> // Cast to error result
                            }
                        }
                        else -> null
                    }
                }
            } else {
                result // Keep all errors
            }
        }
    
    /**
     * Map a result list retaining all previously failed items.
     * 
     * ```kotlin
     * ResultList.map({ it * 2 }, listOf(Result.success(1), Result.success(2))) == 
     *     listOf(Result.success(2), Result.success(4))
     * ```
     */
    fun <E, A, B> map(transform: (A) -> B, resultList: ResultList<E, A>): kotlin.collections.List<Result<B>> = 
        resultList.map { result -> result.map(transform) }
    
    /**
     * Map a result list where the mapping function itself can fail.
     */
    fun <E, A, B> mapOrFail(transform: (A) -> Result<B>, resultList: ResultList<E, A>): kotlin.collections.List<Result<B>> = 
        resultList.map { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let(transform) ?: Result.failure(Exception("Null value"))
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
        }
    
    /**
     * Turn a list of results into a single result keeping all errors.
     */
    fun <E, A> keepAllErrors(results: ResultList<E, A>): Result<kotlin.collections.List<A>> {
        val successes = mutableListOf<A>()
        val errors = mutableListOf<E>()
        
        results.forEach { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { successes.add(it) }
            } else {
                (result.exceptionOrNull() as? E)?.let { errors.add(it) }
            }
        }
        
        return if (errors.isEmpty()) {
            Result.success(successes)
        } else {
            Result.failure(Exception("Multiple errors: $errors"))
        }
    }
    
    /**
     * Turn a list of results into a single result keeping only the first error.
     */
    fun <E, A> keepFirstError(results: ResultList<E, A>): Result<kotlin.collections.List<A>> {
        return when (val allErrorsResult = keepAllErrors(results)) {
            is Result<*> -> {
                if (allErrorsResult.isSuccess) {
                    allErrorsResult
                } else {
                    // Return first error
                    results.firstOrNull { it.isFailure } ?: Result.success(emptyList())
                }
            }
            else -> Result.success(emptyList())
        }
    }
}