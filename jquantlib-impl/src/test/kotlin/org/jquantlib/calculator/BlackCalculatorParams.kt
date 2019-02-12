package org.jquantlib.calculator

import com.fasterxml.jackson.core.type.TypeReference
import org.jquantlib.DataLoader
import org.jquantlib.ExpectedAsDouble
import org.jquantlib.api.data.StrikedTypePayoff

interface BlackCalculatorParams {
  val strikedTypePayoff: StrikedTypePayoff
  val forward: Double
  val stdDev: Double
  val discount: Double
}

object ListBlackCalculatorParamsExpectedTypeReference : TypeReference<List<BlackCalculatorParamsExpected>>()

data class BlackCalculatorParamsExpected (
    override val strikedTypePayoff: StrikedTypePayoff,
    override val forward: Double,
    override val stdDev: Double,
    override val discount: Double,
    override val expected: Double
): BlackCalculatorParams, ExpectedAsDouble

fun assertEqualsBlackCalculatorParamsExpected(filename: String, f: (BlackCalculatorParamsExpected) -> Double) {
  DataLoader.assertEqualsDouble(filename, ListBlackCalculatorParamsExpectedTypeReference) {
    f(it)
  }
}

object ListBlackCalculatorSpotParamsExpectedTypeReference : TypeReference<List<BlackCalculatorSpotParamsExpected>>()

data class BlackCalculatorSpotParamsExpected(
    override val strikedTypePayoff: StrikedTypePayoff,
    override val forward: Double,
    override val stdDev: Double,
    override val discount: Double,
    val spot: Double,
    override val expected: Double
): BlackCalculatorParams, ExpectedAsDouble

fun assertEqualsBlackCalculatorSpotParamsExpected(filename: String, f: (BlackCalculatorSpotParamsExpected) -> Double) {
  DataLoader.assertEqualsDouble(filename, ListBlackCalculatorSpotParamsExpectedTypeReference) {
    f(it)
  }
}

object ListBlackCalculatorSpotMaturityParamsExpectedTypeReference : TypeReference<List<BlackCalculatorSpotMaturityParamsExpected>>()

data class BlackCalculatorSpotMaturityParamsExpected(
    override val strikedTypePayoff: StrikedTypePayoff,
    override val forward: Double,
    override val stdDev: Double,
    override val discount: Double,
    val spot: Double,
    val maturity: Double,
    override val expected: Double
): BlackCalculatorParams, ExpectedAsDouble

fun assertEqualsBlackCalculatorSpotMaturityParamsExpected(filename: String, f: (BlackCalculatorSpotMaturityParamsExpected) -> Double) {
  DataLoader.assertEqualsDouble(filename, ListBlackCalculatorSpotMaturityParamsExpectedTypeReference) {
    f(it)
  }
}

object ListBlackCalculatorMaturityParamsExpectedTypeReference : TypeReference<List<BlackCalculatorMaturityParamsExpected>>()

data class BlackCalculatorMaturityParamsExpected(
    override val strikedTypePayoff: StrikedTypePayoff,
    override val forward: Double,
    override val stdDev: Double,
    override val discount: Double,
    val maturity: Double,
    override val expected: Double
): BlackCalculatorParams, ExpectedAsDouble

fun assertEqualsBlackCalculatorMaturityParamsExpected(filename: String, f: (BlackCalculatorMaturityParamsExpected) -> Double) {
  DataLoader.assertEqualsDouble(filename, ListBlackCalculatorMaturityParamsExpectedTypeReference) {
    f(it)
  }
}


