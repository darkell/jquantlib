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
import org.jquantlib.api.data.*
import org.jquantlib.api.jackson.QuantlibObjectMapperFactory
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.io.File
import java.time.LocalDate

@RunWith(Parameterized::class)
class DayCounterServiceYearFractionTest(
    private val params: DayCounterServiceYearFractionParams
) {

  companion object {
    private val dayCounterService = DayCounterServiceImpl(
        calendarService = CalendarServiceImpl()
    )

    @JvmStatic
    @Parameters(name = "{index}: yearFraction({0})")
    fun data() : List<DayCounterServiceYearFractionParams> {
      return QuantlibObjectMapperFactory
          .build()
          .readerFor(ListDayCounterServiceYearFractionParamsTypeReference)
          .readValue<List<DayCounterServiceYearFractionParams>>(
              File(javaClass.getResource("/DayCounter_yearFraction.json").file).readText()
          )
    }
  }

  @Test
  fun yearFraction() {
    assertEquals(
        params.expected,
        dayCounterService.yearFraction(
            dayCounter = params.dayCounter,
            start = params.d1,
            end = params.d2
        ),
        1e-10
    )
  }
}

object ListDayCounterServiceYearFractionParamsTypeReference : TypeReference<List<DayCounterServiceYearFractionParams>>()

data class DayCounterServiceYearFractionParams(
    val dayCounter: DayCounter,
    val d1: LocalDate,
    val d2: LocalDate,
    val expected: Double
)