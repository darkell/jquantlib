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

import org.junit.Test

class CalendarServiceTest {

  private val calendarService = CalendarServiceImpl()

  @Test
  fun isHoliday() {
    assertEqualsCalendarLocalDateBooleanParams("Calendar_isHoliday.json") {
      calendarService.isHoliday(it.calendar, it.date)
    }
  }

  @Test
  fun isWeekend() {
    assertEqualsCalendarLocalDateBooleanParams("Calendar_isWeekend.json") {
      calendarService.isWeekend(it.calendar, it.date)
    }
  }

  @Test
  fun isBusinessDay() {
    assertEqualsCalendarLocalDateBooleanParams("Calendar_isBusinessDay.json") {
      calendarService.isBusinessDay(it.calendar, it.date)
    }
  }

  @Test
  fun isEndOfMonth() {
    assertEqualsCalendarLocalDateBooleanParams("Calendar_isEndOfMonth.json") {
      calendarService.isEndOfMonth(it.calendar, it.date)
    }
  }

  @Test
  fun endOfMonth() {
    assertEqualsCalendarDateDateParams("Calendar_isEndOfMonth.json") {
      calendarService.endOfMonth(it.calendar, it.date)
    }
  }

  @Test
  fun adjust() {
    assertEqualsCalendarDateBdcDateParams("Calendar_adjust.json") {
      calendarService.adjust(it.calendar, it.date, it.businessDayConvention)
    }
  }

  @Test
  fun advance() {
    assertEqualsCalendarDateLongUnitBdcBooleanDateParams("Calendar_advance.json") {
      calendarService.advance(
          calendar = it.calendar,
          date = it.date,
          n = it.n,
          unit = it.unit,
          c = it.businessDayConvention,
          endOfMonth = it.endOfMonth
      )
    }
  }

  @Test
  fun businessDaysBetween() {
    assertEqualsCalendarDateDateBooleanBooleanLongParams("Calendar_businessDaysBetween.json") {
      calendarService.businessDaysBetween(
          calendar = it.calendar,
          from = it.from,
          to = it.to,
          includeFirst = it.includeFirst,
          includeLast = it.includeLast
      )
    }
  }

}