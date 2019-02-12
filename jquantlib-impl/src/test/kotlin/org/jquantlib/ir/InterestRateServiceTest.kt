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

package org.jquantlib.ir

import com.fasterxml.jackson.core.type.TypeReference
import org.jquantlib.DataLoader.data
import org.jquantlib.DataLoader.dataForEach
import org.jquantlib.api.data.Compounding
import org.jquantlib.api.data.DayCounter
import org.jquantlib.api.data.Frequency
import org.jquantlib.api.data.Frequency.*
import org.jquantlib.api.data.InterestRate
import org.jquantlib.calendar.CalendarServiceImpl
import org.jquantlib.dayCounter.DayCounterServiceImpl
import org.junit.Assert.assertEquals
import org.junit.Test

class InterestRateServiceTest {

  private val service = InterestRateServiceImpl(
      dayCounterService = DayCounterServiceImpl(
          calendarService = CalendarServiceImpl()
      )
  )

  @Test
  fun compoundFactor() {
    dataForEach("InterestRate_compoundFactor_time.json", ListParamsTypeReference) {
      assertEquals(
          it.toString(),
          it.expected,
          service.compoundFactor(it.interestRate, it.time),
          1e-8
      )
    }
  }

  @Test
  fun discountFactor() {
    dataForEach("InterestRate_discountFactor_time.json", ListParamsTypeReference) {
      assertEquals(
          it.toString(),
          it.expected,
          service.discountFactor(it.interestRate, it.time),
          1e-8
      )
    }
  }

  @Test
  fun impliedRate() {
    data("InterestRate_impliedRate_time.json", ListParams2TypeReference)
        .filter { it.frequency == it.expected.frequency }
        .forEach {
          assertInterestRateEquals(
              it.toString(),
              it.expected,
              service.impliedRate(
                  compound = it.compound,
                  dayCounter = it.dayCounter,
                  compounding = it.compounding,
                  frequency = it.frequency,
                  time = it.time
              )
          )
        }
  }

  @Test
  fun equivalentRate() {
    data("InterestRate_equivalentRate_time.json", ListParams3TypeReference)
        .filter { it.interestRate.frequency != NoFrequency }
        .filter { it.expected.frequency != NoFrequency }
        .forEach {
          assertInterestRateEquals(
              it.toString(),
              it.expected,
              service.equivalentRate(
                  interestRate = it.interestRate,
                  compounding = it.compounding,
                  frequency = it.frequency,
                  time = it.time
              )
          )
        }
  }

  fun assertInterestRateEquals(
      message: String,
      expected: InterestRate,
      actual: InterestRate
  ) {
    assertEquals(message, expected.rate, actual.rate, 1e-10)
    assertEquals(message, expected.dayCounter, actual.dayCounter)
    assertEquals(message, expected.compounding, actual.compounding)
    assertEquals(message, expected.frequency, actual.frequency)
  }

  object ListParamsTypeReference : TypeReference<List<Params>>()

  data class Params(
      val interestRate: InterestRate,
      val time: Double,
      val expected: Double
  )

  object ListParams2TypeReference : TypeReference<List<Params2>>()

  data class Params2(
      val compound: Double,
      val dayCounter: DayCounter,
      val compounding: Compounding,
      val frequency: Frequency,
      val time: Double,
      val expected: InterestRate
  )

  object ListParams3TypeReference : TypeReference<List<Params3>>()

  data class Params3(
      val interestRate: InterestRate,
      val compounding: Compounding,
      val frequency: Frequency,
      val time: Double,
      val expected: InterestRate
  )

}