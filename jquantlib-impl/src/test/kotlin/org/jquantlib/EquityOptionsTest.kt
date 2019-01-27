package org.jquantlib

import org.jquantlib.api.data.*
import org.jquantlib.calculator.BlackCalculatorServiceImpl
import org.jquantlib.calendar.CalendarServiceImpl
import org.jquantlib.dayCounter.DayCounterServiceImpl
import org.jquantlib.engine.AnalyticEuropeanEngineServiceImpl
import org.jquantlib.ir.InterestRateServiceImpl
import org.jquantlib.yts.YieldTermStructureServiceImpl
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import java.time.LocalDate
import java.time.Period

class EquityOptionsTest {

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
  private val blackCalculatorService = BlackCalculatorServiceImpl()
  private val analyticEuropeanEngineService = AnalyticEuropeanEngineServiceImpl(
      dayCounterService = dayCounterService,
      yieldTermStructureService = yieldTermStructureService,
      blackCalculatorService = blackCalculatorService
  )

  @Test
  fun test() {
    val calendar = Target
    val todaysDate = LocalDate.of(1998, 5, 15)
    val settlementDate = LocalDate.of(1998, 5, 17)

    // Threadlocal
    val evaluationDate = todaysDate

    val type = OptionType.Put
    val strike = 40.0
    val underlying = 36.0
    val riskFreeRate = 0.06
    val volatility = 0.2
    val dividendYield = 0.00

    val maturity = LocalDate.of(1999, 5, 17)
    val dayCounter: DayCounter = Actual365Fixed

    // Define exercise for European Options
    val europeanExercise = EuropeanExercise(maturity)

    val underlyingH: Quote = SimpleQuote(underlying)
    val flatDividendTS: YieldTermStructure = FlatForward(
        referenceDate = settlementDate,
        dayCounter = dayCounter,
        forward = SimpleQuote(dividendYield)
    )
    val flatTermStructure: YieldTermStructure = FlatForward(
        referenceDate = settlementDate,
        forward = SimpleQuote(riskFreeRate),
        dayCounter = dayCounter
    )

    val flatVolTS: BlackVolTermStructure = BlackConstantVol(
        referenceDate = settlementDate,
        volatility = SimpleQuote(volatility),
        calendar = calendar,
        dayCounter = dayCounter
    )

    val payoff = PlainVanillaPayoff(
        type = type,
        strike = strike
    )

    val bsmProcess = BlackScholesMertonProcess(
        underlyingH,
        flatDividendTS,
        flatTermStructure,
        flatVolTS
    )

    // European Options
    val europeanOption = EuropeanOption(
        payoff = payoff,
        exercise = europeanExercise
    )

    val result = analyticEuropeanEngineService.calculate(evaluationDate, europeanOption, bsmProcess)

    assertEquals(
        "npv",
        3.8443077915968398,
        result.value,
        1e-10
    )

    assertEquals(
        "delta",
        -0.5504516724833853,
        result.greeks.delta,
        1e-10
    )

    assertEquals(
        "gamma",
        0.0549649809708038,
        result.greeks.gamma,
        1e-10
    )

    assertEquals(
        "theta",
        -0.00505822670331213,
        result.greeks.theta,
        1e-10
    )

    assertEquals(
        "vega",
        14.246923067632345,
        result.greeks.vega,
        1e-10
    )

    assertEquals(
        "rho",
        -23.660568000998694,
        result.greeks.rho,
        1e-10
    )

    assertEquals(
        "dividendRho",
        19.816260209401854,
        result.greeks.dividendRho,
        1e-10
    )

    assertEquals(
        "itmCashProbability",
        0.37190860461294756,
        result.moreGreeks.itmCashProbability,
        1e-10
    )

    assertEquals(
        "deltaForward",
        -0.5183958625969849,
        result.moreGreeks.deltaForward,
        1e-10
    )

    assertEquals(
        "elasticity",
        -5.154701778228491,
        result.moreGreeks.elasticity,
        1e-10
    )

    assertEquals(
        "thetaPerDay",
        -1.3858155351540082E-5,
        result.moreGreeks.thetaPerDay,
        1e-10
    )

    assertEquals(
        "strikeSensitivity",
        0.5915142000249676,
        result.moreGreeks.strikeSensitivity,
        1e-10
    )
  }

}