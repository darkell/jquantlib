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
import org.jquantlib.api.data.*
import org.jquantlib.api.jackson.QuantlibObjectMapperFactory
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.io.File

@RunWith(Parameterized::class)
class InterestRateServiceCompoundFactorTimeTest(
    private val params: InterestRateCompoundFactorTimeParams
) {

  companion object {
    private val service = InterestRateServiceImpl()

    @JvmStatic
    @Parameters(name = "{index}: discount({0})")
    fun data() : List<InterestRateCompoundFactorTimeParams> {
      return QuantlibObjectMapperFactory
          .build()
          .readerFor(ListInterestRateCompoundFactorTimeParamsTypeReference)
          .readValue<List<InterestRateCompoundFactorTimeParams>>(
              File(javaClass.getResource("/InterestRate_compoundFactor_time.json").file).readText()
          )
    }
  }

  @Test
  fun discount() {
    val interestRate = InterestRate(
        rate = params.rate,
        dc = params.dayCounter,
        compound = params.compounding,
        frequency = params.frequency
    )

    assertEquals(
        params.expected,
        service.compoundFactor(interestRate, params.time),
        1e-8
    )
  }
}

object ListInterestRateCompoundFactorTimeParamsTypeReference : TypeReference<List<InterestRateCompoundFactorTimeParams>>()

data class InterestRateCompoundFactorTimeParams(
    val rate: Double,
    val dayCounter: DayCounter,
    val compounding: Compounding,
    val frequency: Frequency,
    val time: Double,
    val expected: Double
)
