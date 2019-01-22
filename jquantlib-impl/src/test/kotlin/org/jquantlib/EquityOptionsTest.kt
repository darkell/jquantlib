package org.jquantlib

import org.jquantlib.api.data.*
import org.jquantlib.calendar.CalendarServiceImpl
import org.jquantlib.dayCounter.DayCounterServiceImpl
import org.jquantlib.engine.AnalyticEuropeanEngineServiceImpl
import org.jquantlib.ir.InterestRateServiceImpl
import org.jquantlib.yts.YieldTermStructureServiceImpl
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
  private val analyticEuropeanEngineService = AnalyticEuropeanEngineServiceImpl(
      calendarService = calendarService,
      dayCounterService = dayCounterService,
      yieldTermStructureService = yieldTermStructureService
  )

  @Test
  @Ignore
  fun blah() {

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

    // Define exercise for Bermudan Options
    val bermudanForwards = 4;
    val exerciseDates = listOf(
        settlementDate.plus(Period.ofMonths(3)),
        settlementDate.plus(Period.ofMonths(6)),
        settlementDate.plus(Period.ofMonths(9)),
        settlementDate.plus(Period.ofMonths(12))
    )

    val bermudanExercise: Exercise = BermudanExercise(exerciseDates)

    val americanExercise: Exercise = AmericanExercise(settlementDate, maturity)

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

    analyticEuropeanEngineService.calculate(evaluationDate, europeanOption, bsmProcess)

    // Bermudan options (can be thought as a collection of European Options)
    val bermudanOption: AbstractVanillaOption = VanillaOption(
        payoff = payoff,
        exercise = bermudanExercise
    )

    // American Options
    val americanOption: VanillaOption = VanillaOption(
        payoff = payoff,
        exercise = americanExercise
    )

  }

}