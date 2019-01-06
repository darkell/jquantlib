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

package org.jquantlib.calendar

import org.jquantlib.api.data.*
import org.jquantlib.api.data.Target
import org.jquantlib.api.service.CalendarService
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CalendarServiceImpl : CalendarService {

  private fun Calendar.toInternal() = when (this) {
      Australia -> CalendarInternalAustralia
      UnitedStatesSettlement -> CalendarInternalUnitedStatesSettlement
      UnitedStatesNyse -> TODO()
      UnitedStatesGovernmentBond -> TODO()
      UnitedStatesNerc -> TODO()
      Target -> CalendarInternalTarget
    }

  override fun isBusinessDay(calendar: Calendar, date: LocalDate) =
      calendar.toInternal().isBusinessDay(date)

  override fun isHoliday(calendar: Calendar, date: LocalDate): Boolean {
    return calendar.toInternal().isHoliday(date)
  }

  override fun isWeekend(calendar: Calendar, date: LocalDate) =
      calendar.toInternal().isWeekend(date)

  override fun isEndOfMonth(calendar: Calendar, date: LocalDate) =
      calendar.toInternal().isEndOfMonth(date)

  override fun endOfMonth(calendar: Calendar, date: LocalDate) =
      calendar.toInternal().endOfMonth(date)

  override fun adjust(calendar: Calendar, date: LocalDate, c: BusinessDayConvention) =
      calendar.toInternal().adjust(date, c)

  override fun advance(
      calendar: Calendar,
      date: LocalDate,
      n: Long,
      unit: ChronoUnit,
      c: BusinessDayConvention,
      endOfMonth: Boolean
  ) = calendar.toInternal().advance(date, n, unit, c, endOfMonth)

  override fun businessDaysBetween(
      calendar: Calendar,
      from: LocalDate,
      to: LocalDate,
      includeFirst: Boolean,
      includeLast: Boolean
  ) = calendar.toInternal().businessDaysBetween(from, to, includeFirst, includeLast)

}