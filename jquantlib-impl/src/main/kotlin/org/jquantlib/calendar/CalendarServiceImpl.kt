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

import org.jquantlib.api.data.BusinessDayConvention
import org.jquantlib.api.service.CalendarService
import org.jquantlib.time.Calendar
import org.jquantlib.time.calendars.Australia
import org.jquantlib.time.calendars.Target
import org.jquantlib.time.calendars.UnitedStates
import java.time.LocalDate
import java.time.Period
import org.jquantlib.utils.Convertors.fromQl
import org.jquantlib.utils.Convertors.toQl

class CalendarServiceImpl : CalendarService {

  private val calendars = mapOf(
      "AU" to Australia() as Calendar,
      "US" to UnitedStates(UnitedStates.Market.SETTLEMENT) as Calendar,
      "US_SETTLEMENT" to UnitedStates(UnitedStates.Market.SETTLEMENT) as Calendar,
      "US_NYSE" to UnitedStates(UnitedStates.Market.NYSE) as Calendar,
      "US_GOVERNMENTBOND" to UnitedStates(UnitedStates.Market.GOVERNMENTBOND) as Calendar,
      "US_NERC" to UnitedStates(UnitedStates.Market.NERC) as Calendar,
      "TARGET" to Target() as Calendar
  )

  override fun isBusinessDay(id: String, date: LocalDate): Boolean {
    return calendars[id]!!.isBusinessDay(date.toQl())
  }

  override fun isHoliday(id: String, date: LocalDate): Boolean {
    return calendars[id]!!.isHoliday(date.toQl())
  }

  override fun isEndOfMonth(id: String, date: LocalDate): Boolean {
    return calendars[id]!!.isEndOfMonth(date.toQl())
  }

  override fun endOfMonth(id: String, date: LocalDate): LocalDate {
    return calendars[id]!!.endOfMonth(date.toQl()).fromQl()
  }

  override fun adjust(id: String, date: LocalDate, c: BusinessDayConvention): LocalDate {
    return calendars[id]!!.adjust(date.toQl(), c.toQl()).fromQl()
  }

  override fun advance(id: String, date: LocalDate, period: Period, c: BusinessDayConvention, endOfMonth: Boolean): LocalDate {
    return calendars[id]!!.advance(date.toQl(), period.toQl(), c.toQl(), endOfMonth).fromQl()
  }

  override fun businessDaysBetween(id: String, from: LocalDate, to: LocalDate, includeFirst: Boolean, includeLast: Boolean): Long {
    return calendars[id]!!.businessDaysBetween(from.toQl(), to.toQl(), includeFirst, includeLast).toLong()
  }

}