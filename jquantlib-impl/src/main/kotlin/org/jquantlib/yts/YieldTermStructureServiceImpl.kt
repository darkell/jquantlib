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

import org.jquantlib.QL
import org.jquantlib.api.data.*
import org.jquantlib.api.service.DayCounterService
import org.jquantlib.api.service.InterestRateService
import org.jquantlib.api.service.YieldTermStructureService
import java.time.LocalDate

class YieldTermStructureServiceImpl(
    private val dayCounterService: DayCounterService,
    private val interestRateService: InterestRateService
) : YieldTermStructureService {
  override fun discount(
      yts: YieldTermStructure,
      d: LocalDate,
      extrapolate: Boolean
  ): Double {
    return when (yts) {
      is FlatForward -> yts.discount(yts.timeFromReference(d), extrapolate)
    }
  }

  private fun FlatForward.discount(
      t: Double,
      extrapolate: Boolean
  ): Double {
    return interestRateService.discountFactor(createInterestRate(), t)
  }

  private fun FlatForward.timeFromReference(date: LocalDate): Double {
    return dayCounterService.yearFraction(dayCounter, referenceDate, date)
  }

  private fun FlatForward.createInterestRate(): InterestRate {
    QL.require(frequency !in invalidFrequencies, "frequency not allowed for this interest rate")

    return InterestRate(
        rate = forward.value,
        dc = dayCounter,
        compound = compounding,
        frequency = frequency
    )
  }

  private val invalidFrequencies = setOf(
      Frequency.Once,
      Frequency.NoFrequency
  )

}