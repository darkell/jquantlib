package org.jquantlib.calculator

import org.junit.Test

class BlackCalculatorServiceTest {

  private val blackCalculatorService = BlackCalculatorServiceImpl()

  @Test
  fun value() {
    assertEqualsBlackCalculatorParamsExpected("BlackCalculator_value.json") {
      blackCalculatorService.value(it.bc())
    }
  }

  @Test
  fun delta() {
    assertEqualsBlackCalculatorSpotParamsExpected("BlackCalculator_delta.json") {
      blackCalculatorService.delta(it.bc(), it.spot)
    }
  }

  @Test
  fun deltaForward() {
    assertEqualsBlackCalculatorParamsExpected("BlackCalculator_deltaForward.json") {
      blackCalculatorService.deltaForward(it.bc())
    }
  }

  @Test
  fun elasticity() {
    assertEqualsBlackCalculatorSpotParamsExpected("BlackCalculator_elasticity.json") {
      blackCalculatorService.elasticity(it.bc(), it.spot)
    }
  }

  @Test
  fun elasticityForward() {
    assertEqualsBlackCalculatorParamsExpected("BlackCalculator_elasticityForward.json") {
      blackCalculatorService.elasticityForward(it.bc())
    }
  }

  @Test
  fun gamma() {
    assertEqualsBlackCalculatorSpotParamsExpected("BlackCalculator_gamma.json") {
      blackCalculatorService.gamma(it.bc(), it.spot)
    }
  }

  @Test
  fun gammaForward() {
    assertEqualsBlackCalculatorParamsExpected("BlackCalculator_gammaForward.json") {
      blackCalculatorService.gammaForward(it.bc())
    }
  }

  @Test
  fun theta() {
    assertEqualsBlackCalculatorSpotMaturityParamsExpected("BlackCalculator_theta.json") {
      blackCalculatorService.theta(it.bc(), it.spot, it.maturity)
    }
  }

  @Test
  fun thetaPerDay() {
    assertEqualsBlackCalculatorSpotMaturityParamsExpected("BlackCalculator_thetaPerDay.json") {
      blackCalculatorService.thetaPerDay(it.bc(), it.spot, it.maturity)
    }
  }

  @Test
  fun vega() {
    assertEqualsBlackCalculatorSpotParamsExpected("BlackCalculator_vega.json") {
      blackCalculatorService.vega(it.bc(), it.spot)
    }
  }

  @Test
  fun rho() {
    assertEqualsBlackCalculatorMaturityParamsExpected("BlackCalculator_rho.json") {
      blackCalculatorService.rho(it.bc(), it.maturity)
    }
  }

  @Test
  fun dividendRho() {
    assertEqualsBlackCalculatorMaturityParamsExpected("BlackCalculator_dividendRho.json") {
      blackCalculatorService.dividendRho(it.bc(), it.maturity)
    }
  }

  @Test
  fun itmCashProbability() {
    assertEqualsBlackCalculatorParamsExpected("BlackCalculator_itmCashProbability.json") {
      blackCalculatorService.itmCashProbability(it.bc())
    }
  }

  @Test
  fun itmAssetProbability() {
    assertEqualsBlackCalculatorParamsExpected("BlackCalculator_itmAssetProbability.json") {
      blackCalculatorService.itmAssetProbability(it.bc())
    }
  }

  @Test
  fun strikeSensitivity() {
    assertEqualsBlackCalculatorParamsExpected("BlackCalculator_strikeSensitivity.json") {
      blackCalculatorService.strikeSensitivity(it.bc())
    }
  }

  private fun BlackCalculatorParams.bc() =
      blackCalculatorService.create(
          payoff = strikedTypePayoff,
          forward = forward,
          stdDev = stdDev,
          discount = discount
      )

}