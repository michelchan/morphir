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

import java.util.regex.Pattern

/**
 * Constraint definitions for various data types.
 */
object Constraints {
    
    /**
     * Constraint for integer values.
     */
    data class IntConstraint(
        val minValue: Int,
        val range: Int
    )
    
    /**
     * Constraint for string values.
     */
    sealed class StringConstraint {
        data class StringSizeConstraint(val constraint: StringSizeConstraintValue) : StringConstraint()
        data class StringRegexConstraint(val regex: Pattern) : StringConstraint()
    }
    
    /**
     * Size constraint for strings.
     */
    data class StringSizeConstraintValue(
        val minLength: Int?,
        val maxLength: Int
    )
    
    /**
     * Constraint for decimal values.
     */
    sealed class DecimalConstraint {
        data class DecimalSizeConstraint(val constraint: DecimalSizeConstraintValue) : DecimalConstraint()
    }
    
    /**
     * Size constraint for decimal values.
     */
    data class DecimalSizeConstraintValue(
        val size: Int,
        val precision: Int
    )
}