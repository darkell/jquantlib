/*
 Copyright (C) 2019 David Arkell

 This source code is release under the BSD License.

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.api.service

import org.jquantlib.api.data.BusinessDayConvention
import org.jquantlib.api.data.BusinessDayConvention.Following
import org.jquantlib.api.data.Calendar
import java.time.LocalDate
import java.time.Period

interface CalendarService {

  fun isBusinessDay(
      calendar: Calendar,
      date: LocalDate): Boolean

  fun isHoliday(
      calendar: Calendar,
      date: LocalDate
  ): Boolean

  //fun isWeekend(Weekday w): Boolean

  fun isEndOfMonth(
      calendar: Calendar,
      date: LocalDate
  ): Boolean

  /**
   * Returns last business day of the month to which the given date belongs
   *
   * @param d
   * @return last business Date based on passed date
   */
  fun endOfMonth(
      calendar: Calendar,
      date: LocalDate
  ): LocalDate

  /**
   * Adjusts a non-business day to the appropriate near business day
   * with respect to the given convention.
   *
   * @note The input date is not modified
   */
  fun adjust(
      calendar: Calendar,
      date: LocalDate,
      c: BusinessDayConvention = Following
  ): LocalDate

  /**
   * Advances the given date as specified by the given period and
   * returns the result.
   *
   * @note The input date is not modified.
   */
  fun advance(
      calendar: Calendar,
      date: LocalDate,
      period: Period,
      c: BusinessDayConvention = Following,
      endOfMonth: Boolean = false
  ): LocalDate

  fun businessDaysBetween(
      calendar: Calendar,
      from: LocalDate,
      to: LocalDate,
      includeFirst: Boolean = true,
      includeLast: Boolean = false
  ): Long

}