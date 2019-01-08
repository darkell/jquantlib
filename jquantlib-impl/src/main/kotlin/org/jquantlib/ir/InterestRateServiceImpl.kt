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

import org.jquantlib.api.data.*
import org.jquantlib.api.data.Compounding.*
import org.jquantlib.api.data.Frequency.*
import org.jquantlib.api.service.DayCounterService
import org.jquantlib.api.service.InterestRateService
import java.time.LocalDate

class InterestRateServiceImpl(
    private val dayCounterService: DayCounterService
) : InterestRateService {
  override fun discountFactor(interestRate: InterestRate, time: Double): Double {
    val factor = compoundFactor(interestRate, time)
    return 1.0 / factor
  }

  override fun compoundFactor(interestRate: InterestRate, time: Double): Double {
    require(time >= 0.0) { "negative time not allowed" }
    // QL.require(!Double.isNaN(rate) , "null interest rate")

    // TODO: code review :: please verify against QL/C++ code
    // if (rate<0.0) throw new IllegalArgumentException("null interest rate");

    val freq = interestRate.frequency.toDouble()

    return when (interestRate.compounding) {
      Simple -> simple(interestRate.rate, time)
      Compounded -> compounded(interestRate.rate, freq, time)
      Continuous -> continuous(interestRate.rate, time)
      SimpleThenCompounded -> if (isFirstPeriod(time, freq)) simple(interestRate.rate, time) else compounded(interestRate.rate, freq, time)
      CompoundedThenSimple -> if (isFirstPeriod(time, freq)) compounded(interestRate.rate, freq, time) else simple(interestRate.rate, time)
    }
  }

  override fun impliedRate(
      compound: Double,
      dayCounter: DayCounter,
      compounding: Compounding,
      frequency: Frequency,
      time: Double
  ): InterestRate {
    require(compound > 0.0) { "positive compound factor required" }

    val rate =
        when (compound) {
          1.0 -> {
            require(time >= 0.0) { "non negative time ($time) required" }
            0.0
          }
          else -> calcRate(compound, compounding, frequency, time)
        }

    return InterestRate(rate, dayCounter, compounding, frequency)
  }

  override fun impliedRate(
      compound: Double,
      dayCounter: DayCounter,
      compounding: Compounding,
      frequency: Frequency,
      start: LocalDate,
      end: LocalDate,
      refStart: LocalDate?,
      refEnd: LocalDate?
  ): InterestRate {
    require(!start.isAfter(end)) { "start ($start) later than end ($end)" }

    val time = dayCounterService.yearFraction(dayCounter, start, end, refStart, refEnd)
    return impliedRate(compound, dayCounter, compounding, frequency, time)
  }

  private fun calcRate(
      compound: Double,
      compounding: Compounding,
      frequency: Frequency,
      time: Double
  ): Double {
    require(time > 0.0) { "positive time ($time) required" }

    val freq = frequency.toDouble()

    return when (compounding) {
      Simple -> compoundSimple(compound, time)
      Compounded -> compoundCompounded(compound, freq, time)
      Continuous ->  compoundContinuous(compound, time)
      SimpleThenCompounded -> if (isFirstPeriod(time, freq)) compoundSimple(compound, time) else compoundCompounded(compound, freq, time)
      CompoundedThenSimple -> if (isFirstPeriod(time, freq)) compoundCompounded(compound, freq, time) else compoundSimple(compound, time)
    }
  }

  override fun equivalentRate(
      interestRate: InterestRate,
      compounding: Compounding,
      frequency: Frequency,
      time: Double
  ): InterestRate {
    return impliedRate(
        compoundFactor(interestRate, time),
        interestRate.dayCounter,
        compounding,
        frequency,
        time
    )
  }

  override fun equivalentRate(
      interestRate: InterestRate,
      dayCounter: DayCounter,
      compounding: Compounding,
      frequency: Frequency,
      start: LocalDate,
      end: LocalDate,
      refStart: LocalDate?,
      refEnd: LocalDate?
  ): InterestRate {
    val time1 = dayCounterService.yearFraction(interestRate.dayCounter, start, end, refStart, refEnd)
    val time2 = dayCounterService.yearFraction(dayCounter, start, end, refStart, refEnd)

    return impliedRate(
        compoundFactor(interestRate, time1),
        dayCounter,
        compounding,
        frequency,
        time2
    )
  }

  private fun isFirstPeriod(time: Double, frequency: Double) = time <= (1.0 / frequency)

  private fun simple(rate: Double, time: Double) = 1.0 + rate * time
  private fun compounded(rate: Double, frequency: Double, time: Double) = Math.pow(1 + rate / frequency, frequency * time)
  private fun continuous(rate: Double, time: Double) = Math.exp(rate * time)

  private fun compoundSimple(compound: Double, time: Double) = (compound - 1.0) / time
  private fun compoundCompounded(compound: Double, frequency: Double, time: Double) = (Math.pow(compound, 1.0 / (frequency * time)) - 1.0) * frequency
  private fun compoundContinuous(compound: Double, time: Double) = Math.log(compound) / time

}