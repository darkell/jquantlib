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

package org.jquantlib.dayCounter

import com.fasterxml.jackson.core.type.TypeReference
import org.jquantlib.DataLoader
import org.jquantlib.DataLoader.data
import org.jquantlib.api.data.DayCounter
import org.jquantlib.api.jackson.QuantlibObjectMapperFactory
import org.jquantlib.calendar.CalendarServiceImpl
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.time.LocalDate

class DayCounterServiceTest {

  private val dayCounterService = DayCounterServiceImpl(
      calendarService = CalendarServiceImpl()
  )

  @Test
  fun dayCount() {
    for (params in data("/DayCounter_dayCount.json", ListDayCountTypeReference)) {
      assertEquals(
          "$params",
          params.expected,
          dayCounterService.dayCount(
              dayCounter = params.dayCounter,
              start = params.start,
              end = params.end
          )
      )
    }
  }

  object ListDayCountTypeReference : TypeReference<List<DayCountParams>>()

  data class DayCountParams(
      val dayCounter: DayCounter,
      val start: LocalDate,
      val end: LocalDate,
      val expected: Long
  )

  @Test
  fun yearFraction() {
    for (params in data("/DayCounter_yearFraction.json", ListYearFractionParamsTypeReference)) {
      assertEquals(
          "$params",
          params.expected,
          dayCounterService.yearFraction(
              dayCounter = params.dayCounter,
              start = params.start,
              end = params.end
          ),
          1e-10
      )
    }
  }

  object ListYearFractionParamsTypeReference : TypeReference<List<YearFractionParams>>()

  data class YearFractionParams(
      val dayCounter: DayCounter,
      val start: LocalDate,
      val end: LocalDate,
      val expected: Double
  )

}