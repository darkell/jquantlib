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

import com.fasterxml.jackson.core.type.TypeReference
import org.jquantlib.DataLoader
import org.jquantlib.DataLoader.data
import org.jquantlib.api.data.*
import org.jquantlib.api.jackson.QuantlibObjectMapperFactory
import org.jquantlib.calendar.CalendarServiceImpl
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import java.io.File
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CalendarServiceTest {

  private val calendarService = CalendarServiceImpl()

  @Test
  fun blah() {
    val s = generateSequence(LocalDate.of(2001, 1, 1)) { it.plusDays(1) }

    s.takeWhile { it.isBefore(LocalDate.of(2001, 1, 31)) }.forEach { println(it) }
  }

  @Test
  fun isHoliday() {
    data("/Calendar_isHoliday.json", ListBooleanParamsTypeReference).forEach {
      assertEquals(
          "$it",
          it.expected,
          calendarService.isHoliday(
              calendar = it.calendar,
              date = it.date
          )
      )
    }
  }

  @Test
  fun isWeekend() {
    data("/Calendar_isWeekend.json", ListBooleanParamsTypeReference).forEach {
      assertEquals(
          "$it",
          it.expected,
          calendarService.isWeekend(
              calendar = it.calendar,
              date = it.date
          )
      )
    }
  }

  @Test
  fun isBusinessDay() {
    data("/Calendar_isBusinessDay.json", ListBooleanParamsTypeReference).forEach {
      assertEquals(
          "$it",
          it.expected,
          calendarService.isBusinessDay(
              calendar = it.calendar,
              date = it.date
          )
      )
    }
  }

  @Test
  fun isEndOfMonth() {
    data("/Calendar_isEndOfMonth.json", ListBooleanParamsTypeReference).forEach {
      assertEquals(
          "$it",
          it.expected,
          calendarService.isEndOfMonth(
              calendar = it.calendar,
              date = it.date
          )
      )
    }
  }

  @Test
  fun endOfMonth() {
    data("/Calendar_endOfMonth.json", ListEndOfMonthParamsTypeReference).forEach {
      assertEquals(
          "$it",
          it.expected,
          calendarService.endOfMonth(
              calendar = it.calendar,
              date = it.date
          )
      )
    }
  }

  @Test
  fun adjust() {
    data("/Calendar_adjust.json", ListAdjustParamsTypeReference).forEach {
      assertEquals(
          "$it",
          it.expected,
          calendarService.adjust(
              calendar = it.calendar,
              date = it.date,
              c = it.businessDayConvention
          )
      )
    }
  }

  @Test
  fun advance() {
    data("/Calendar_advance.json", ListAdvanceParamsTypeReference).forEach {
      assertEquals(
          "$it",
          it.expected,
          calendarService.advance(
              calendar = it.calendar,
              date = it.date,
              n = it.n,
              unit = it.unit,
              c = it.businessDayConvention,
              endOfMonth = it.endOfMonth
          )
      )
    }
  }

  @Test
  fun businessDaysBetween() {
    data("/Calendar_businessDaysBetween.json", ListBusinessDaysBetweenParamsTypeReference).forEach {
      assertEquals(
          "$it",
          it.expected,
          calendarService.businessDaysBetween(
              calendar = it.calendar,
              from = it.from,
              to = it.to,
              includeFirst = it.includeFirst,
              includeLast = it.includeLast
          )
      )
    }
  }

  object ListBooleanParamsTypeReference : TypeReference<List<BooleanParams>>()

  data class BooleanParams(
      val calendar: Calendar,
      val date: LocalDate,
      val expected: Boolean
  )

  object ListEndOfMonthParamsTypeReference : TypeReference<List<EndOfMonthParams>>()

  data class EndOfMonthParams(
      val calendar: Calendar,
      val date: LocalDate,
      val expected: LocalDate
  )

  object ListAdjustParamsTypeReference : TypeReference<List<AdjustParams>>()

  data class AdjustParams(
      val calendar: Calendar,
      val date: LocalDate,
      val businessDayConvention: BusinessDayConvention,
      val expected: LocalDate
  )

  object ListAdvanceParamsTypeReference : TypeReference<List<AdvanceParams>>()

  data class AdvanceParams(
      val calendar: Calendar,
      val date: LocalDate,
      val n: Long,
      val unit: ChronoUnit,
      val businessDayConvention: BusinessDayConvention,
      val endOfMonth: Boolean,
      val expected: LocalDate
  )

  object ListBusinessDaysBetweenParamsTypeReference : TypeReference<List<BusinessDaysBetweenParams>>()

  data class BusinessDaysBetweenParams(
      val calendar: Calendar,
      val from: LocalDate,
      val to: LocalDate,
      val includeFirst: Boolean,
      val includeLast: Boolean,
      val expected: Long
  )

}