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
 * Checking if things are the same.
 */
object Equality {
    
    /**
     * Check if values are "the same".
     */
    fun <T> equal(a: T, b: T): Boolean = a == b
    
    /**
     * Check if values are not "the same".
     * 
     * So `notEqual(a, b)` is the same as `!equal(a, b)`.
     */
    fun <T> notEqual(a: T, b: T): Boolean = a != b
}