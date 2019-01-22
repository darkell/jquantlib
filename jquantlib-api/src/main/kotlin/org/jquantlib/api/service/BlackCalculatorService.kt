package org.jquantlib.api.service

import org.jquantlib.api.data.BlackCalculator
import org.jquantlib.api.data.StrikedTypePayoff

interface BlackCalculatorService {
  fun create(
      payoff: StrikedTypePayoff,
      forward: Double,
      stdDev: Double,
      discount: Double
  ): BlackCalculator

  fun value(bc: BlackCalculator): Double

  /**
   * Sensitivity to change in the underlying spot price.
   */
  fun delta(bc: BlackCalculator, spot: Double): Double

  /**
   * Sensitivity to change in the underlying forward price.
   */
  fun deltaForward(bc: BlackCalculator): Double

  /**
   * Sensitivity in percent to a percent change in the underlying spot price.
   */
  fun elasticity(bc: BlackCalculator, spot: Double): Double

  /**
   * Sensitivity in percent to a percent change in the underlying forward
   * price.
   */
  fun elasticityForward(bc: BlackCalculator): Double

  /**
   * Second order derivative with respect to change in the underlying spot
   * price.
   */
  fun gamma(bc: BlackCalculator, spot: Double): Double

  /**
   * Second order derivative with respect to change in the underlying forward
   * price.
   */
  fun gammaForward(bc: BlackCalculator): Double

  /**
   * Sensitivity to time to maturity.
   */
  fun theta(bc: BlackCalculator, spot: Double, maturity: Double): Double

  /**
   * Sensitivity to time to maturity per day, assuming 365 day per year.
   */
  fun thetaPerDay(bc: BlackCalculator, spot: Double, maturity: Double): Double

  /**
   * Sensitivity to volatility.
   */
  fun vega(bc: BlackCalculator, maturity: Double): Double

  /**
   * Sensitivity to discounting rate.
   */
  fun rho(bc: BlackCalculator, maturity: Double): Double

  /**
   * Sensitivity to dividend/growth rate.
   */
  fun dividendRho(bc: BlackCalculator, maturity: Double): Double

  /**
   * Probability of being in the money in the bond martingale measure, i.e.
   * N(d2).
   *
   * <p>
   * It is a risk-neutral probability, not the real world one.
   */
  fun itmCashProbability(bc: BlackCalculator): Double

  /**
   * Probability of being in the money in the asset martingale measure, i.e.
   * N(d1).
   *
   * <p>
   * It is a risk-neutral probability, not the real world one.
   */
  fun itmAssetProbability(bc: BlackCalculator): Double

  /**
   * Sensitivity to strike.
   */
  fun strikeSensitivity(bc: BlackCalculator): Double

}