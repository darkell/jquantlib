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
import org.jquantlib.api.data.*
import org.jquantlib.api.jackson.QuantlibObjectMapperFactory
import org.jquantlib.calendar.CalendarServiceImpl
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.io.File
import java.time.LocalDate

@RunWith(Parameterized::class)
class DayCounterServiceDayCountTest(
    private val params: DayCounterServiceDayCountParams
) {

  companion object {
    private val dayCounterService = DayCounterServiceImpl(
        calendarService = CalendarServiceImpl()
    )

    @JvmStatic
    @Parameters(name = "{index}: dayCount({0})")
    fun data() : List<DayCounterServiceDayCountParams> {
      return QuantlibObjectMapperFactory
          .build()
          .readerFor(ListDayCounterServiceDayCountParamsTypeReference)
          .readValue<List<DayCounterServiceDayCountParams>>(
              File(javaClass.getResource("/DayCounter_dayCount.json").file).readText()
          )
    }
  }

  @Test
  fun dayCount() {
    assertEquals(
        params.expected,
        dayCounterService.dayCount(
            dayCounter = params.dayCounter,
            start = params.d1,
            end = params.d2
        )
    )
  }
}

object ListDayCounterServiceDayCountParamsTypeReference : TypeReference<List<DayCounterServiceDayCountParams>>()

data class DayCounterServiceDayCountParams(
    val dayCounter: DayCounter,
    val d1: LocalDate,
    val d2: LocalDate,
    val expected: Long
)