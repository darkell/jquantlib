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
import org.jquantlib.api.results.GreeksResults
import org.jquantlib.api.results.MoreGreeksResults
import org.jquantlib.api.results.OneAssetOptionResults
import org.jquantlib.api.service.*
import java.time.LocalDate
import kotlin.Double.Companion.NaN

class AnalyticEuropeanEngineServiceImpl(
    private val dayCounterService: DayCounterService,
    private val yieldTermStructureService: YieldTermStructureService,
    private val blackCalculatorService: BlackCalculatorService
) : AnalyticEuropeanEngineService {
  override fun calculate(
      evaluationDate: LocalDate,
      europeanOption: EuropeanOption,
      bsmProcess: BlackScholesMertonProcess
  ): OneAssetOptionResults {
    return if (isExpired(evaluationDate, europeanOption)) {
      OneAssetOptionResults(
          value = 0.0,
          errorEstimate = 0.0,
          greeks = GreeksResults(
              delta = 0.0,
              gamma = 0.0,
              theta = 0.0,
              vega = 0.0,
              rho = 0.0,
              dividendRho = 0.0
          ),
          moreGreeks = MoreGreeksResults(
              itmCashProbability = 0.0,
              deltaForward = 0.0,
              elasticity = 0.0,
              thetaPerDay = 0.0,
              strikeSensitivity = 0.0
          )
      )
    } else {
      blackVariance(bsmProcess, europeanOption)
    }
  }

  private fun blackVariance(
      bsmProcess: BlackScholesMertonProcess,
      europeanOption: EuropeanOption
  ): OneAssetOptionResults {
    val spot = bsmProcess.x0.value
    require(spot > 0.0) { "Negative underlying given" }

    val lastDate = europeanOption.exercise.lastDate()
    val bc = blackCalculator(bsmProcess, lastDate, europeanOption, spot)
    val time = bsmProcess.blackVolTS.yearFraction(lastDate)

    return OneAssetOptionResults(
        value = blackCalculatorService.value(bc),
        errorEstimate = NaN,
        greeks = calcGreeksResults(bc, spot, time, bsmProcess, lastDate),
        moreGreeks = calcMoreGreeksResults(bc, spot, time)
    )
  }

  private fun blackCalculator(
      bsmProcess: BlackScholesMertonProcess,
      lastDate: LocalDate,
      europeanOption: EuropeanOption,
      spot: Double
  ): BlackCalculator {
    val variance = blackVariance(bsmProcess, lastDate)
    val dividendDiscount = yieldTermStructureService.discount(bsmProcess.dividendTS, lastDate)
    val riskFreeDiscount = yieldTermStructureService.discount(bsmProcess.riskFreeTS, lastDate)

    val forwardPrice = spot * dividendDiscount / riskFreeDiscount

    return blackCalculatorService.create(
        payoff = europeanOption.payoff,
        forward = forwardPrice,
        stdDev = Math.sqrt(variance),
        discount = riskFreeDiscount
    )
  }

  private fun calcGreeksResults(
      bc: BlackCalculator,
      spot: Double,
      time: Double,
      bsmProcess: BlackScholesMertonProcess,
      lastDate: LocalDate
  ): GreeksResults {
    val theta = try {
      blackCalculatorService.theta(bc, spot, time)
    } catch (e: Exception) {
      NaN
    }

    return GreeksResults(
        delta = blackCalculatorService.delta(bc, spot),
        gamma = blackCalculatorService.gamma(bc, spot),
        theta = theta,
        vega = blackCalculatorService.vega(bc, time),
        rho = calcGreeksRho(bc, bsmProcess, lastDate),
        dividendRho = calcGreeksDividendRho(bc, bsmProcess, lastDate)
    )
  }

  private fun calcMoreGreeksResults(bc: BlackCalculator, spot: Double, time: Double): MoreGreeksResults {
    val thetaPerDay = try {
      blackCalculatorService.thetaPerDay(bc, spot, time)
    } catch (e: Exception) {
      NaN
    }

    return MoreGreeksResults(
        itmCashProbability = blackCalculatorService.itmCashProbability(bc),
        deltaForward = blackCalculatorService.deltaForward(bc),
        elasticity = blackCalculatorService.elasticity(bc, spot),
        thetaPerDay = thetaPerDay,
        strikeSensitivity = blackCalculatorService.strikeSensitivity(bc)
    )
  }

  private fun calcGreeksRho(bc: BlackCalculator, bsmProcess: BlackScholesMertonProcess, lastDate: LocalDate): Double {
    return blackCalculatorService.rho(
        bc = bc,
        maturity = bsmProcess.riskFreeTS.yearFraction(lastDate)
    )
  }

  private fun calcGreeksDividendRho(
      bc: BlackCalculator,
      bsmProcess: BlackScholesMertonProcess,
      lastDate: LocalDate
  ): Double {
    return blackCalculatorService.dividendRho(
        bc = bc,
        maturity = bsmProcess.dividendTS.yearFraction(lastDate)
    )
  }

  private fun blackVariance(
      bsmProcess: BlackScholesMertonProcess,
      maturity: LocalDate
  ): Double {
    val time = bsmProcess.blackVolTS.yearFraction(maturity)

    return blackVariance(
        bsmProcess = bsmProcess,
        maturity = time
    )
  }

  private fun TermStructure.yearFraction(end: LocalDate) =
      dayCounterService.yearFraction(
          dayCounter = dayCounter,
          start = referenceDate,
          end = end
      )

  private fun blackVariance(
      bsmProcess: BlackScholesMertonProcess,
      maturity: Double
  ): Double {
    val volatility = bsmProcess.blackVolTS.volatility.value

    return volatility * volatility * maturity
  }

  private fun isExpired(
      evaluationDate: LocalDate,
      europeanOption: EuropeanOption
  ): Boolean {
    return europeanOption.exercise.lastDate().isBefore(evaluationDate)
  }

}