package org.jquantlib

import org.jquantlib.daycounters.Actual365Fixed
import org.jquantlib.exercise.AmericanExercise
import org.jquantlib.exercise.BermudanExercise
import org.jquantlib.exercise.EuropeanExercise
import org.jquantlib.instruments.EuropeanOption
import org.jquantlib.instruments.Option
import org.jquantlib.instruments.Option.*
import org.jquantlib.instruments.Option.Type.*
import org.jquantlib.instruments.PlainVanillaPayoff
import org.jquantlib.instruments.VanillaOption
import org.jquantlib.pricingengines.AnalyticEuropeanEngine
import org.jquantlib.processes.BlackScholesMertonProcess
import org.jquantlib.quotes.Handle
import org.jquantlib.quotes.Quote
import org.jquantlib.quotes.SimpleQuote
import org.jquantlib.termstructures.BlackVolTermStructure
import org.jquantlib.termstructures.YieldTermStructure
import org.jquantlib.termstructures.volatilities.BlackConstantVol
import org.jquantlib.termstructures.yieldcurves.FlatForward
import org.jquantlib.time.Date
import org.jquantlib.time.Month
import org.jquantlib.time.Month.*
import org.jquantlib.time.Period
import org.jquantlib.time.TimeUnit
import org.jquantlib.time.calendars.Target
import org.junit.Test

class EquityOptions2Test {

  @Test
  fun test() {
    // set up dates
    val calendar = Target()
    val todaysDate = Date(15, May, 1998)
    val settlementDate = Date(17, May, 1998)
    Settings().setEvaluationDate(todaysDate)

    // our options
    val type = Put
    val strike = 40.0
    val underlying = 36.0
    val riskFreeRate = 0.06
    val volatility = 0.2
    val dividendYield = 0.00

    val maturity = Date(17, May, 1999)
    val dayCounter = Actual365Fixed()

    // Define exercise for European Options
    val europeanExercise = EuropeanExercise(maturity)

    // bootstrap the yield/dividend/volatility curves
    val underlyingH = Handle<Quote>(SimpleQuote(underlying))
    val flatDividendTS = Handle<YieldTermStructure>(FlatForward(settlementDate, dividendYield, dayCounter))
    val flatTermStructure = Handle<YieldTermStructure>(FlatForward(settlementDate, riskFreeRate, dayCounter))
    val flatVolTS = Handle<BlackVolTermStructure>(BlackConstantVol(settlementDate, calendar, volatility, dayCounter))
    val payoff = PlainVanillaPayoff(type, strike)

    val bsmProcess = BlackScholesMertonProcess(underlyingH, flatDividendTS, flatTermStructure, flatVolTS)

    // European Options
    val europeanOption = EuropeanOption(payoff, europeanExercise)

    europeanOption.setPricingEngine(AnalyticEuropeanEngine(bsmProcess))

    val npv = europeanOption.NPV()

    println("npv = $npv")
    println("delta = ${europeanOption.delta()}")
    println("gamma = ${europeanOption.gamma()}")
    println("theta = ${europeanOption.theta()}")
    println("vega = ${europeanOption.vega()}")
    println("rho = ${europeanOption.rho()}")
    println("dividendRho = ${europeanOption.dividendRho()}")

    println("itmCashProbability = ${europeanOption.itmCashProbability()}")
    println("deltaForward = ${europeanOption.deltaForward()}")
    println("elasticity = ${europeanOption.elasticity()}")
    println("thetaPerDay = ${europeanOption.thetaPerDay()}")
    println("strikeSensitivity = ${europeanOption.strikeSensitivity()}")
  }

}