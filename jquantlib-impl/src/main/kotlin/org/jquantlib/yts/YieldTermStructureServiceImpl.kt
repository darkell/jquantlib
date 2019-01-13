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

import org.jquantlib.api.data.*
import org.jquantlib.api.data.Frequency.NoFrequency
import org.jquantlib.api.data.Frequency.Once
import org.jquantlib.api.service.CalendarService
import org.jquantlib.api.service.DayCounterService
import org.jquantlib.api.service.InterestRateService
import org.jquantlib.api.service.YieldTermStructureService
import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

class YieldTermStructureServiceImpl(
    private val calendarService: CalendarService,
    private val dayCounterService: DayCounterService,
    private val interestRateService: InterestRateService
) : YieldTermStructureService {
  override fun referenceDate(
      calendar: Calendar,
      evaluationDate: LocalDate,
      settlementDays: Long
  ) = calendarService.advance(calendar, evaluationDate, settlementDays, DAYS)

  override fun createFlatForward(
      calendar: Calendar,
      evaluationDate: LocalDate,
      settlementDays: Long,
      dayCounter: DayCounter,
      forward: Quote,
      compounding: Compounding,
      frequency: Frequency
  ) = FlatForward(
      referenceDate = referenceDate(calendar, evaluationDate, settlementDays),
      dayCounter = dayCounter,
      forward = forward,
      compounding = compounding,
      frequency = frequency
  )

  override fun discount(
      yts: YieldTermStructure,
      d: LocalDate,
      extrapolate: Boolean
  ) = when (yts) {
    is FlatForward -> yts.discount(yts.timeFromReference(d), extrapolate)
  }

  override fun discount(
      yts: YieldTermStructure,
      time: Double,
      extrapolate: Boolean
  ) = when (yts) {
    is FlatForward -> yts.discount(time, extrapolate)
  }

  private fun FlatForward.discount(
      t: Double,
      extrapolate: Boolean
  ) = interestRateService.discountFactor(createInterestRate(), t)

  private fun FlatForward.timeFromReference(date: LocalDate) =
      dayCounterService.yearFraction(dayCounter, referenceDate, date)

  private fun FlatForward.createInterestRate(): InterestRate {
    require(frequency !in invalidFrequencies) { "frequency not allowed for this interest rate" }

    return InterestRate(
        rate = forward.value,
        dayCounter = dayCounter,
        compounding = compounding,
        frequency = frequency
    )
  }

  companion object {
    private val invalidFrequencies = setOf(Once, NoFrequency)
  }

}