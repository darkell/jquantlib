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
import java.time.Period
import org.jquantlib.utils.Convertors.fromQl
import org.jquantlib.utils.Convertors.toQl

class CalendarServiceImpl : CalendarService {

  private fun calendars(calendar: Calendar) = when (calendar) {
    is Australia -> org.jquantlib.time.calendars.Australia()
    UnitedStatesSettlement -> org.jquantlib.time.calendars.UnitedStates(org.jquantlib.time.calendars.UnitedStates.Market.SETTLEMENT)
    UnitedStatesNyse -> org.jquantlib.time.calendars.UnitedStates(org.jquantlib.time.calendars.UnitedStates.Market.NYSE)
    UnitedStatesGovernmentBond -> org.jquantlib.time.calendars.UnitedStates(org.jquantlib.time.calendars.UnitedStates.Market.GOVERNMENTBOND)
    UnitedStatesNerc -> org.jquantlib.time.calendars.UnitedStates(org.jquantlib.time.calendars.UnitedStates.Market.NERC)
    Target -> org.jquantlib.time.calendars.Target()
  }

  override fun isBusinessDay(
      calendar: Calendar,
      date: LocalDate
  ): Boolean {
    return calendars(calendar).isBusinessDay(date.toQl())
  }

  override fun isHoliday(
      calendar: Calendar,
      date: LocalDate
  ): Boolean {
    return calendars(calendar).isHoliday(date.toQl())
  }

  override fun isEndOfMonth(
      calendar: Calendar,
      date: LocalDate
  ): Boolean {
    return calendars(calendar).isEndOfMonth(date.toQl())
  }

  override fun endOfMonth(
      calendar: Calendar,
      date: LocalDate
  ): LocalDate {
    return calendars(calendar).endOfMonth(date.toQl()).fromQl()
  }

  override fun adjust(
      calendar: Calendar,
      date: LocalDate,
      c: BusinessDayConvention
  ): LocalDate {
    return calendars(calendar).adjust(date.toQl(), c.toQl()).fromQl()
  }

  override fun advance(
      calendar: Calendar,
      date: LocalDate,
      period: Period,
      c: BusinessDayConvention,
      endOfMonth: Boolean
  ): LocalDate {
    return calendars(calendar).advance(date.toQl(), period.toQl(), c.toQl(), endOfMonth).fromQl()
  }

  override fun businessDaysBetween(
      calendar: Calendar,
      from: LocalDate,
      to: LocalDate,
      includeFirst: Boolean,
      includeLast: Boolean
  ): Long {
    return calendars(calendar).businessDaysBetween(from.toQl(), to.toQl(), includeFirst, includeLast).toLong()
  }

}