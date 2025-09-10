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
 * Extra utilities on lists.
 */
object List {
    
    /**
     * Returns all elements of a list except for the last.
     * 
     * ```kotlin
     * List.init(listOf(1, 2, 3)) == listOf(1, 2)
     * List.init(emptyList<Int>()) == null
     * ```
     */
    fun <T> init(list: kotlin.collections.List<T>): kotlin.collections.List<T>? =
        if (list.isEmpty()) null else list.dropLast(1)
    
    /**
     * Simulates a SQL inner-join.
     * 
     * ```kotlin
     * val dataSetA = listOf(Pair(1, "a"), Pair(2, "b"))
     * val dataSetB = listOf(Pair(3, "C"), Pair(2, "B"))
     * 
     * List.innerJoin(
     *     listA = dataSetA,
     *     listB = dataSetB,
     *     onPredicate = { a, b -> a.first == b.first }
     * ) == listOf(Pair(Pair(2, "b"), Pair(2, "B")))
     * ```
     */
    fun <A, B> innerJoin(
        listA: kotlin.collections.List<A>,
        listB: kotlin.collections.List<B>,
        onPredicate: (A, B) -> Boolean
    ): kotlin.collections.List<Pair<A, B>> {
        return listA.flatMap { a ->
            listB.mapNotNull { b ->
                if (onPredicate(a, b)) Pair(a, b) else null
            }
        }
    }
    
    /**
     * Simulates a SQL left-outer-join.
     * 
     * ```kotlin
     * val dataSetA = listOf(Pair(1, "a"), Pair(2, "b"))
     * val dataSetB = listOf(Pair(3, "C"), Pair(2, "B"))
     * 
     * List.leftJoin(
     *     listA = dataSetA,
     *     listB = dataSetB,
     *     onPredicate = { a, b -> a.first == b.first }
     * ) == listOf(
     *     Pair(Pair(1, "a"), null),
     *     Pair(Pair(2, "b"), Pair(2, "B"))
     * )
     * ```
     */
    fun <A, B> leftJoin(
        listA: kotlin.collections.List<A>,
        listB: kotlin.collections.List<B>,
        onPredicate: (A, B) -> Boolean
    ): kotlin.collections.List<Pair<A, B?>> {
        return listA.flatMap { a ->
            val matchingRows = listB.mapNotNull { b ->
                if (onPredicate(a, b)) Pair(a, b) else null
            }
            
            if (matchingRows.isEmpty()) {
                listOf(Pair(a, null))
            } else {
                matchingRows
            }
        }
    }
}