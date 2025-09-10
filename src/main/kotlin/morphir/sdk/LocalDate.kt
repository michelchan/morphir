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

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * This module adds the definition of a date without time zones. Useful in business modeling.
 */
object LocalDateUtils {
    
    /**
     * Find the number of days between the given dates.
     */
    fun diffInDays(fromDate: LocalDate, toDate: LocalDate): Long =
        ChronoUnit.DAYS.between(fromDate, toDate)
    
    /**
     * Find the number of weeks between the given dates.
     */
    fun diffInWeeks(fromDate: LocalDate, toDate: LocalDate): Long =
        ChronoUnit.WEEKS.between(fromDate, toDate)
    
    /**
     * Find the number of months between the given dates.
     */
    fun diffInMonths(fromDate: LocalDate, toDate: LocalDate): Long =
        ChronoUnit.MONTHS.between(fromDate, toDate)
    
    /**
     * Find the number of years between the given dates.
     */
    fun diffInYears(fromDate: LocalDate, toDate: LocalDate): Long =
        ChronoUnit.YEARS.between(fromDate, toDate)
    
    /**
     * Add the given days to a given date.
     */
    fun addDays(count: Long, date: LocalDate): LocalDate = date.plusDays(count)
    
    /**
     * Add the given weeks to a given date.
     */
    fun addWeeks(count: Long, date: LocalDate): LocalDate = date.plusWeeks(count)
    
    /**
     * Add the given months to a given date.
     */
    fun addMonths(count: Long, date: LocalDate): LocalDate = date.plusMonths(count)
    
    /**
     * Add the given years to a given date.
     */
    fun addYears(count: Long, date: LocalDate): LocalDate = date.plusYears(count)
    
    /**
     * Create a date from a calendar date: a year, month, and day of
     * the month. Out-of-range day values will be clamped.
     * 
     * ```kotlin
     * LocalDateUtils.fromCalendarDate(2018, Month.SEPTEMBER, 26)
     * ```
     */
    fun fromCalendarDate(year: Int, month: Month, dayOfMonth: Int): LocalDate =
        try {
            LocalDate.of(year, month, dayOfMonth)
        } catch (e: Exception) {
            // Clamp to valid range
            val lastDayOfMonth = LocalDate.of(year, month, 1).lengthOfMonth()
            val clampedDay = dayOfMonth.coerceIn(1, lastDayOfMonth)
            LocalDate.of(year, month, clampedDay)
        }
    
    /**
     * Create a date from an ordinal date: a year and day of the year.
     * Out-of-range day values will be clamped.
     * 
     * ```kotlin
     * LocalDateUtils.fromOrdinalDate(2018, 269)
     * ```
     */
    fun fromOrdinalDate(year: Int, dayOfYear: Int): LocalDate =
        try {
            LocalDate.ofYearDay(year, dayOfYear)
        } catch (e: Exception) {
            // Clamp to valid range
            val maxDayOfYear = if (LocalDate.of(year, 1, 1).isLeapYear) 366 else 365
            val clampedDay = dayOfYear.coerceIn(1, maxDayOfYear)
            LocalDate.ofYearDay(year, clampedDay)
        }
    
    /**
     * Construct a LocalDate based on ISO formatted string.
     */
    fun fromISO(iso: String): LocalDate? = try {
        LocalDate.parse(iso)
    } catch (e: Exception) {
        null
    }
    
    /**
     * Convert a LocalDate to a string in ISO format.
     */
    fun toISOString(localDate: LocalDate): String = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
    
    /**
     * Construct a LocalDate based on Year, Month, Day.
     * Errors can occur when any of the given values fall outside of their relevant constraints.
     * For example, the date given as 2000 2 30 (2000-Feb-30) would fail because the day of the 30th is impossible.
     */
    fun fromParts(year: Int, monthNumber: Int, dayOfMonth: Int): LocalDate? = try {
        if (monthNumber in 1..12) {
            LocalDate.of(year, monthNumber, dayOfMonth)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
    
    /**
     * Returns the year as a number.
     */
    fun year(localDate: LocalDate): Int = localDate.year
    
    /**
     * Returns the month of the year for a given date.
     */
    fun month(localDate: LocalDate): Month = localDate.month
    
    /**
     * Returns the month of the year as an Int, where January is month 1 and December is month 12.
     */
    fun monthNumber(localDate: LocalDate): Int = localDate.monthValue
    
    /**
     * Converts a Month to an Int, where January is month 1 and December is month 12.
     */
    fun monthToInt(month: Month): Int = month.value
    
    /**
     * The day of the month (1–31).
     */
    fun day(localDate: LocalDate): Int = localDate.dayOfMonth
    
    /**
     * Returns the day of week for a date.
     */
    fun dayOfWeek(localDate: LocalDate): DayOfWeek = localDate.dayOfWeek
    
    /**
     * Returns true if the date falls on a weekend (Saturday or Sunday).
     */
    fun isWeekend(localDate: LocalDate): Boolean {
        val dayOfWeek = localDate.dayOfWeek
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY
    }
    
    /**
     * Returns true if the date falls on a weekday (any day other than Saturday or Sunday).
     */
    fun isWeekday(localDate: LocalDate): Boolean = !isWeekend(localDate)
}