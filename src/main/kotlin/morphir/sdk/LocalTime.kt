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

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * This module adds the definition of basic time without time zones.
 */
object LocalTimeUtils {
    
    /**
     * Add the given hours to a given time
     */
    fun addHours(hours: Long, time: LocalTime): LocalTime = time.plusHours(hours)
    
    /**
     * Add the given minutes to a given time.
     */
    fun addMinutes(minutes: Long, time: LocalTime): LocalTime = time.plusMinutes(minutes)
    
    /**
     * Add the given seconds to a given time.
     */
    fun addSeconds(seconds: Long, time: LocalTime): LocalTime = time.plusSeconds(seconds)
    
    /**
     * Find the difference of given times in hours
     */
    fun diffInHours(timeA: LocalTime, timeB: LocalTime): Long = 
        ChronoUnit.HOURS.between(timeB, timeA)
    
    /**
     * Find the difference of given times in minutes
     */
    fun diffInMinutes(timeA: LocalTime, timeB: LocalTime): Long = 
        ChronoUnit.MINUTES.between(timeB, timeA)
    
    /**
     * Find the difference of given times in seconds
     */
    fun diffInSeconds(timeA: LocalTime, timeB: LocalTime): Long = 
        ChronoUnit.SECONDS.between(timeB, timeA)
    
    /**
     * Construct a LocalTime based on ISO formatted string.
     */
    fun fromISO(iso: String): LocalTime? = try {
        LocalTime.parse(iso)
    } catch (e: Exception) {
        null
    }
    
    /**
     * Construct a LocalTime based on number of milliseconds from midnight.
     */
    fun fromMilliseconds(millis: Long): LocalTime = LocalTime.ofNanoOfDay(millis * 1_000_000)
    
    /**
     * Convert LocalTime to ISO string representation.
     */
    fun toISOString(time: LocalTime): String = time.format(DateTimeFormatter.ISO_LOCAL_TIME)
}