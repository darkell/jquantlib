package org.jquantlib.calculator

import com.fasterxml.jackson.core.type.TypeReference
import org.jquantlib.DataLoader.data
import org.jquantlib.api.data.BlackCalculator
import org.jquantlib.api.data.OptionType
import org.jquantlib.api.data.PlainVanillaPayoff
import org.jquantlib.api.data.StrikedTypePayoff
import org.junit.Assert.assertEquals
import org.junit.Test

class BlackCalculatorServiceTest {

  private val blackCalculatorService = BlackCalculatorServiceImpl()

  private val error = 1e-10

  @Test
  fun value() {
    params1Test("/BlackCalculator_value.json") { blackCalculatorService.value(it) }
  }

  @Test
  fun delta() {
    params2Test("/BlackCalculator_delta.json") { bc, spot -> blackCalculatorService.delta(bc, spot) }
  }

  @Test
  fun deltaForward() {
    params1Test("/BlackCalculator_deltaForward.json") { blackCalculatorService.deltaForward(it) }
  }

  @Test
  fun elasticity() {
    params2Test("/BlackCalculator_elasticity.json") { bc, spot -> blackCalculatorService.elasticity(bc, spot) }
  }

  @Test
  fun elasticityForward() {
    params1Test("/BlackCalculator_elasticityForward.json") { blackCalculatorService.elasticityForward(it) }
  }

  @Test
  fun gamma() {
    params2Test("/BlackCalculator_gamma.json") { bc, spot -> blackCalculatorService.gamma(bc, spot) }
  }

  @Test
  fun gammaForward() {
    params1Test("/BlackCalculator_gammaForward.json") { blackCalculatorService.gammaForward(it) }
  }

  @Test
  fun theta() {
    params3Test("/BlackCalculator_theta.json") { bc, spot, maturity -> blackCalculatorService.theta(bc, spot, maturity) }
  }

  @Test
  fun thetaPerDay() {
    params3Test("/BlackCalculator_thetaPerDay.json") { bc, spot, maturity -> blackCalculatorService.thetaPerDay(bc, spot, maturity) }
  }

  @Test
  fun vega() {
    params2Test("/BlackCalculator_vega.json") { bc, maturity -> blackCalculatorService.vega(bc, maturity) }
  }

  @Test
  fun rho() {
    params4Test("/BlackCalculator_rho.json") { bc, maturity -> blackCalculatorService.rho(bc, maturity) }
  }

  @Test
  fun dividendRho() {
    params4Test("/BlackCalculator_dividendRho.json") { bc, maturity -> blackCalculatorService.dividendRho(bc, maturity) }
  }

  @Test
  fun itmCashProbability() {
    params1Test("/BlackCalculator_itmCashProbability.json") { blackCalculatorService.itmCashProbability(it) }
  }

  @Test
  fun itmAssetProbability() {
    params1Test("/BlackCalculator_itmAssetProbability.json") { blackCalculatorService.itmAssetProbability(it) }
  }

  @Test
  fun strikeSensitivity() {
    params1Test("/BlackCalculator_strikeSensitivity.json") { blackCalculatorService.strikeSensitivity(it) }
  }

  private fun params1Test(filename: String, f: (BlackCalculator) -> Double) {
    data(filename, ListParams1TypeReference).forEach {
      val blackCalculator = blackCalculatorService.create(
          payoff = it.strikedTypePayoff,
          forward = it.forward,
          stdDev = it.stdDev,
          discount = it.discount
      )

      assertEquals(
          "$it",
          it.expected,
          f(blackCalculator),
          error
      )
    }
  }

  private fun params2Test(filename: String, f: (bc: BlackCalculator, spot: Double) -> Double) {
    data(filename, ListParams2TypeReference).forEach {
      val bc = blackCalculatorService.create(
          payoff = it.strikedTypePayoff,
          forward = it.forward,
          stdDev = it.stdDev,
          discount = it.discount
      )

      assertEquals(
          "$it",
          it.expected,
          f(bc, it.spot),
          error
      )
    }
  }

  private fun params3Test(filename: String, f: (bc: BlackCalculator, spot: Double, maturity: Double) -> Double) {
    data(filename, ListParams3TypeReference).forEach {
      val bc = blackCalculatorService.create(
          payoff = it.strikedTypePayoff,
          forward = it.forward,
          stdDev = it.stdDev,
          discount = it.discount
      )

      assertEquals(
          "$it",
          it.expected,
          f(bc, it.spot, it.maturity),
          error
      )
    }
  }

  private fun params4Test(filename: String, f: (bc: BlackCalculator, maturity: Double) -> Double) {
    data(filename, ListParams4TypeReference).forEach {
      val bc = blackCalculatorService.create(
          payoff = it.strikedTypePayoff,
          forward = it.forward,
          stdDev = it.stdDev,
          discount = it.discount
      )

      assertEquals(
          "$it",
          it.expected,
          f(bc, it.maturity),
          error
      )
    }
  }

  object ListParams1TypeReference : TypeReference<List<Params1>>()

  data class Params1(
      val strikedTypePayoff: StrikedTypePayoff,
      val forward: Double,
      val stdDev: Double,
      val discount: Double,
      val expected: Double
  )

  object ListParams2TypeReference : TypeReference<List<Params2>>()

  data class Params2(
      val strikedTypePayoff: StrikedTypePayoff,
      val forward: Double,
      val stdDev: Double,
      val discount: Double,
      val spot: Double,
      val expected: Double
  )

  object ListParams3TypeReference : TypeReference<List<Params3>>()

  data class Params3(
      val strikedTypePayoff: StrikedTypePayoff,
      val forward: Double,
      val stdDev: Double,
      val discount: Double,
      val spot: Double,
      val maturity: Double,
      val expected: Double
  )

  object ListParams4TypeReference : TypeReference<List<Params4>>()

  data class Params4(
      val strikedTypePayoff: StrikedTypePayoff,
      val forward: Double,
      val stdDev: Double,
      val discount: Double,
      val maturity: Double,
      val expected: Double
  )

}