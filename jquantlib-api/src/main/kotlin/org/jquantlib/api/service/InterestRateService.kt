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

package org.jquantlib.api.service

import org.jquantlib.api.data.Compounding
import org.jquantlib.api.data.DayCounter
import org.jquantlib.api.data.Frequency
import org.jquantlib.api.data.InterestRate
import java.time.LocalDate

interface InterestRateService {

  fun discountFactor(interestRate: InterestRate, time: Double): Double

  fun compoundFactor(interestRate: InterestRate, time: Double): Double

  fun impliedRate(
      compound: Double,
      dayCounter: DayCounter,
      compounding: Compounding,
      frequency: Frequency,
      time: Double
  ): InterestRate

  fun impliedRate(
      compound: Double,
      dayCounter: DayCounter,
      compounding: Compounding,
      frequency: Frequency,
      start: LocalDate,
      end: LocalDate,
      refStart: LocalDate?,
      refEnd: LocalDate?
  ): InterestRate

  fun equivalentRate(
      interestRate: InterestRate,
      compounding: Compounding,
      frequency: Frequency,
      time: Double
  ): InterestRate

  fun equivalentRate(
      interestRate: InterestRate,
      dayCounter: DayCounter,
      compounding: Compounding,
      frequency: Frequency,
      start: LocalDate,
      end: LocalDate,
      refStart: LocalDate?,
      refEnd: LocalDate?
  ): InterestRate

}