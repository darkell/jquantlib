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

package org.jquantlib.engine

import org.jquantlib.api.data.*
import org.jquantlib.api.results.AnalyticEuropeanEngineResult
import org.jquantlib.api.service.AnalyticEuropeanEngineService
import org.jquantlib.api.service.CalendarService
import org.jquantlib.api.service.DayCounterService
import java.time.LocalDate
import java.time.Period

class AnalyticEuropeanEngineServiceImpl(
    private val calendarService: CalendarService,
    private val dayCounterService: DayCounterService
) : AnalyticEuropeanEngineService {
  override fun calculate(
      evaluationDate: LocalDate,
      europeanOption: EuropeanOption,
      bsmProcess: BlackScholesMertonProcess
  ): AnalyticEuropeanEngineResult {
    return if (isExpired(evaluationDate, europeanOption)) {
      AnalyticEuropeanEngineResult(
          npv = 0.0,
          errorEstimate = 0.0
      )
    } else {
      val lastDate = europeanOption.exercise.lastDate()
      val strike = europeanOption.payoff.strike

      blackVariance(
          bsmProcess = bsmProcess,
          evaluationDate = evaluationDate,
          maturity = lastDate,
          strike = strike
      )
    }
  }

  private fun blackVariance(
      bsmProcess: BlackScholesMertonProcess,
      evaluationDate: LocalDate,
      maturity: LocalDate,
      strike: Double,
      extrapolate: Boolean = false
  ): AnalyticEuropeanEngineResult {
//    dateEnd = {Date@993} "May 17, 1999"
//    dateStart = {Date@919} "May 17, 1998"

    val t = dayCounterService.yearFraction(
        dayCounter = bsmProcess.blackVolTS.dayCounter,
        start = evaluationDate, // fix
        end = maturity
    )

    val variance: Double = blackVariance(bsmProcess, t, strike)
    //val dividendDiscount: Double = process.dividendYield().currentLink().discount(a.exercise.lastDate())


    return AnalyticEuropeanEngineResult(
        npv = 0.0,
        errorEstimate = 0.0
    )
  }

  private fun blackVariance(
      bsmProcess: BlackScholesMertonProcess,
      maturity: Double,
      strike: Double
  ): Double {
    val volatility = bsmProcess.blackVolTS.volatility.value
    val variance = volatility * volatility * maturity

    return variance
  }

  private fun isExpired(
      evaluationDate: LocalDate,
      europeanOption: EuropeanOption
  ): Boolean {
    return europeanOption.exercise.lastDate().isBefore(evaluationDate)
  }

  private fun blackVariance(evaluationDate: LocalDate, maturity: LocalDate, strike: Double, extrapolate: Boolean = false): Double {
//    checkRange(maturity, extrapolate)
//    checkStrike(strike, extrapolate);
//    val t = timeFromReference(maturity)
//    return blackVarianceImpl(t, strike)
    return 0.0
  }

  private fun checkRange(evaluationDate: LocalDate, d: LocalDate, extrapolate: Boolean) {
//    QL.require(d.isAfter(evaluationDate), "date before reference date"); // TODO: message
//    QL.require(extrapolate || allowsExtrapolation() || d.le(maxDate()) , "date is past max curve"); // TODO: message
  }

  private fun referenceDate(evaluationDate: LocalDate, calendar: Calendar, settlementDays: Int): LocalDate {
    return calendarService.advance(calendar, evaluationDate, Period.ofDays(settlementDays))
  }

}