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
 * API for modeling stateful applications.
 */
object StatefulApp {
    
    /**
     * Type that represents a stateful application. The following type parameters allow you to fit it to your use case:
     * 
     * - **K** - Key that's used to partition commands, events and state.
     * - **C** - Type that defines all the commands accepted by the application.
     * - **S** - Type that defines the state managed by the application.
     * - **E** - Type that defines all the events published by the application.
     */
    data class StatefulApp<K, C, S, E>(
        val logic: (S?, C) -> Pair<S?, E>
    )
}