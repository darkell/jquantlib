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
import org.jquantlib.api.data.*
import org.jquantlib.api.jackson.QuantlibObjectMapperFactory
import org.jquantlib.calendar.CalendarServiceImpl
import org.jquantlib.dayCounter.DayCounterServiceImpl
import org.jquantlib.ir.InterestRateServiceImpl
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.io.File
import java.time.LocalDate

@RunWith(Parameterized::class)
class FlatForwardDiscountTest(
    private val params: FlatForwardDiscountParams
) {

  companion object {
    private val yieldTermStructureService = YieldTermStructureServiceImpl(
        dayCounterService = DayCounterServiceImpl(
            calendarService = CalendarServiceImpl()
        ),
        interestRateService = InterestRateServiceImpl()
    )

    @JvmStatic
    @Parameters(name = "{index}: discount({0})")
    fun data() : List<FlatForwardDiscountParams> {
      println()

      return QuantlibObjectMapperFactory
          .build()
          .readerFor(ListFlatForwardDiscountParamsTypeReference)
          .readValue<List<FlatForwardDiscountParams>>(
              File(javaClass.getResource("/FlatForward_discount.json").file).readText()
          )
    }
  }

  @Test
  fun discount() {
    val flatForward = FlatForward(
        referenceDate = params.settlementDate,
        dayCounter = params.dayCounter,
        forward = SimpleQuote(
            params.dividendYield
        )
    )

    assertEquals(params.expected, yieldTermStructureService.discount(flatForward, params.discountDate), 1e-4)
  }
}

object ListFlatForwardDiscountParamsTypeReference : TypeReference<List<FlatForwardDiscountParams>>()

data class FlatForwardDiscountParams(
    val settlementDate: LocalDate,
    val dayCounter: DayCounter,
    val dividendYield: Double,
    val discountDate: LocalDate,
    val compounding: Compounding,
    val frequency: Frequency,
    val expected: Double
)