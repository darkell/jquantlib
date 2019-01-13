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

package org.jquantlib.yts

import com.fasterxml.jackson.core.type.TypeReference
import org.jquantlib.DataLoader
import org.jquantlib.DataLoader.data
import org.jquantlib.api.data.*
import org.jquantlib.api.jackson.QuantlibObjectMapperFactory
import org.jquantlib.calendar.CalendarServiceImpl
import org.jquantlib.dayCounter.DayCounterServiceImpl
import org.jquantlib.ir.InterestRateServiceImpl
import org.jquantlib.ir.InterestRateServiceTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.io.File
import java.time.LocalDate

class FlatForwardDiscountTest {

  private val calendarService = CalendarServiceImpl()

  private val dayCounterService = DayCounterServiceImpl(
      calendarService = calendarService
  )

  private val interestRateService = InterestRateServiceImpl(
      dayCounterService = dayCounterService
  )

  private val yieldTermStructureService = YieldTermStructureServiceImpl(
      calendarService = calendarService,
      dayCounterService = dayCounterService,
      interestRateService = interestRateService
  )

  @Test
  fun discount_date() {
    data("/FlatForward_discount_date.json", ListParams1TypeReference).forEach {
      assertEquals(
          "$it",
          it.expected,
          yieldTermStructureService.discount(it.flatForward, it.date),
          1e-10
      )
    }
  }

  @Test
  fun discount_time() {
    data("/FlatForward_discount_time.json", ListParams2TypeReference).forEach {
      assertEquals(
          "$it",
          it.expected,
          yieldTermStructureService.discount(it.flatForward, it.time),
          1e-10
      )
    }
  }

  object ListParams1TypeReference : TypeReference<List<Params1>>()

  data class Params1(
      val flatForward: FlatForward,
      val date: LocalDate,
      val expected: Double
  )

  object ListParams2TypeReference : TypeReference<List<Params2>>()

  data class Params2(
      val flatForward: FlatForward,
      val time: Double,
      val expected: Double
  )

}
