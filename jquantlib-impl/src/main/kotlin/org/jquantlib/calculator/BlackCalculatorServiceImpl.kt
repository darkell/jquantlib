package org.jquantlib.calculator

import org.apache.commons.math3.distribution.NormalDistribution
import org.jquantlib.api.data.BlackCalculator
import org.jquantlib.api.data.OptionType
import org.jquantlib.api.data.OptionType.*
import org.jquantlib.api.data.StrikedTypePayoff
import org.jquantlib.api.service.BlackCalculatorService
import java.lang.Math.*

class BlackCalculatorServiceImpl : BlackCalculatorService {
  override fun create(
      payoff: StrikedTypePayoff,
      forward: Double,
      stdDev: Double,
      discount: Double
  ): BlackCalculator {
    val strike = payoff.strike
    val variance = stdDev * stdDev

    require(forward > 0.0) { "positive forward value required" }
    require(stdDev >= 0.0) { "non-negative standard deviation required" }
    require(discount > 0.0) { "positive discount required" }

    val d1 = calcD1(stdDev, strike, forward)
    val d2 = calcD2(stdDev, strike, d1)
    val n_d1 = calcN_d1(stdDev, strike, d1)
    val n_d2 = calcN_d2(stdDev, strike, d2)
    val cum_d1 = calcCum_d1(stdDev, strike, forward, d1)
    val cum_d2 = calcCum_d2(stdDev, strike, forward, d2)

    return BlackCalculator(
        strike = strike,
        forward = forward,
        stdDev = stdDev,
        discount = discount,
        variance = variance,
        dX_dS = 0.0,
        n_d1 = n_d1,
        cum_d1 = cum_d1,
        n_d2 = n_d2,
        cum_d2 = cum_d2,
        d1 = d1,
        d2 = d2,
        alpha = calcAlpha(payoff.type, cum_d1),
        beta = calcBeta(payoff.type, cum_d2),
        dAlpha_dD1 = n_d1,
        dBeta_dD2 = -n_d2,
        x = strike,
        dx_dStrike = 1.0
    )
  }

  override fun value(bc: BlackCalculator) =
      bc.discount * (bc.forward * bc.alpha + bc.x * bc.beta)

  override fun delta(bc: BlackCalculator, spot: Double): Double {
    require(spot > 0.0) { "positive spot value required" }

    val dForwardDs = bc.forward / spot
    val temp = bc.stdDev * spot
    val dAlphaDs = bc.dAlpha_dD1 / temp
    val dBetaDs = bc.dBeta_dD2 / temp
    val temp2 = dAlphaDs * bc.forward + bc.alpha * dForwardDs + dBetaDs * bc.x + bc.beta * bc.dX_dS

    return bc.discount * temp2
  }

  override fun deltaForward(bc: BlackCalculator): Double {
    val temp = bc.stdDev * bc.forward
    val dAlphaDforward = bc.dAlpha_dD1 / temp
    val dBetaDforward = bc.dBeta_dD2 / temp
    val temp2 = dAlphaDforward * bc.forward + bc.alpha + dBetaDforward * bc.x

    return bc.discount * temp2
  }

  override fun elasticity(bc: BlackCalculator, spot: Double): Double {
    val value = value(bc)
    val del = delta(bc, spot)
    return when {
      value > QL_EPSILON -> del / value * spot
      abs(del) < QL_EPSILON -> 0.0
      del > 0.0 -> Double.MAX_VALUE
      else -> Double.MIN_VALUE
    }
  }

  override fun elasticityForward(bc: BlackCalculator): Double {
    val value = value(bc)
    val del = deltaForward(bc)
    return when {
      value > QL_EPSILON -> del / value * bc.forward
      abs(del) < QL_EPSILON -> 0.0
      del > 0.0 -> Double.MAX_VALUE
      else -> Double.MIN_VALUE
    }
  }

  override fun gamma(bc: BlackCalculator, spot: Double): Double {
    require(spot > 0.0) { "positive spot value required" }
    val DforwardDs = bc.forward / spot
    val temp = bc.stdDev * spot
    val DalphaDs = bc.dAlpha_dD1 / temp
    val DbetaDs = bc.dBeta_dD2 / temp
    val D2alphaDs2 = -DalphaDs / spot * (1 + bc.d1 / bc.stdDev)
    val D2betaDs2 = -DbetaDs / spot * (1 + bc.d2 / bc.stdDev)
    val temp2 = D2alphaDs2 * bc.forward + 2.0 * DalphaDs * DforwardDs + D2betaDs2 * bc.x + 2.0 * DbetaDs * bc.dX_dS

    return bc.discount * temp2
  }

  override fun gammaForward(bc: BlackCalculator): Double {
    val temp = bc.stdDev * bc.forward
    val DalphaDforward = bc.dAlpha_dD1 / temp
    val DbetaDforward = bc.dBeta_dD2 / temp

    val D2alphaDforward2 = -DalphaDforward / bc.forward * (1 + bc.d1 / bc.stdDev)
    val D2betaDforward2 = -DbetaDforward / bc.forward * (1 + bc.d2 / bc.stdDev)

    val temp2 = D2alphaDforward2 * bc.forward + 2.0 * DalphaDforward + D2betaDforward2 * bc.x

    return bc.discount * temp2
  }

  override fun theta(bc: BlackCalculator, spot: Double, maturity: Double): Double {
    require(maturity > 0.0) { "non negative maturity required" }

    return if (maturity == 0.0) {
      0.0
    } else {
      -(Math.log(bc.discount) * value(bc) + Math.log(bc.forward / spot) * spot * delta(bc, spot) + 0.5 * bc.variance * spot * spot * gamma(bc, spot)) / maturity
    }
  }

  override fun thetaPerDay(bc: BlackCalculator, spot: Double, maturity: Double): Double {
    return theta(bc, spot, maturity) / 365.0
  }

  override fun vega(bc: BlackCalculator, maturity: Double): Double {
    require(maturity >= 0.0) { "negative maturity not allowed" }

    val temp = Math.log(bc.strike / bc.forward) / bc.variance
    val DalphaDsigma = bc.dAlpha_dD1 * (temp + 0.5)
    val DbetaDsigma = bc.dBeta_dD2 * (temp - 0.5)
    val temp2 = DalphaDsigma * bc.forward + DbetaDsigma * bc.x

    return bc.discount * Math.sqrt(maturity) * temp2
  }

  override fun rho(bc: BlackCalculator, maturity: Double): Double {
    require(maturity >= 0.0) { "negative maturity not allowed" }

    val DalphaDr = bc.dAlpha_dD1 / bc.stdDev
    val DbetaDr = bc.dBeta_dD2 / bc.stdDev
    val temp = DalphaDr * bc.forward + bc.alpha * bc.forward + DbetaDr * bc.x

    return maturity * (bc.discount * temp - value(bc))
  }

  override fun dividendRho(bc: BlackCalculator, maturity: Double): Double {
    require(maturity >= 0.0) { "negative maturity not allowed" }

    // actually DalphaDq / T
    val DalphaDq = -bc.dAlpha_dD1 / bc.stdDev
    val DbetaDq = -bc.dBeta_dD2 / bc.stdDev
    val temp = DalphaDq * bc.forward - bc.alpha * bc.forward + DbetaDq * bc.x

    return maturity * bc.discount * temp
  }

  override fun itmCashProbability(bc: BlackCalculator): Double {
    return bc.cum_d2
  }

  override fun itmAssetProbability(bc: BlackCalculator): Double {
    return bc.cum_d1
  }

  override fun strikeSensitivity(bc: BlackCalculator): Double {
    val temp = bc.stdDev * bc.strike
    val DalphaDstrike = -bc.dAlpha_dD1 / temp
    val DbetaDstrike = -bc.dBeta_dD2 / temp
    val temp2 = DalphaDstrike * bc.forward + DbetaDstrike * bc.x + bc.beta * bc.dx_dStrike

    return bc.discount * temp2
  }

  private fun calcD1(stdDev: Double, strike: Double, forward: Double) =
      if (stdDev >= QL_EPSILON && strike != 0.0) {
        log(forward / strike) / stdDev + 0.5 * stdDev
      } else {
        0.0
      }

  private fun calcD2(stdDev: Double, strike: Double, d1: Double) =
      if (stdDev >= QL_EPSILON && strike != 0.0) {
          d1 - stdDev
      } else {
        0.0
      }

  private fun calcN_d1(stdDev: Double, strike: Double, d1: Double) =
      if (stdDev >= QL_EPSILON && strike != 0.0) {
        nd.density(d1)
      } else {
        0.0
      }

  private fun calcN_d2(stdDev: Double, strike: Double, d2: Double) =
      if (stdDev >= QL_EPSILON && strike != 0.0) {
        nd.density(d2)
      } else {
        0.0
      }

  private fun calcCum_d1(stdDev: Double, strike: Double, forward: Double, d1: Double) =
      if (stdDev >= QL_EPSILON) {
        if (strike == 0.0) {
          1.0
        } else {
          nd.cumulativeProbability(d1)
        }
      } else {
        if (forward > strike) {
          1.0
        } else {
          0.0
        }
      }

  private fun calcCum_d2(stdDev: Double, strike: Double, forward: Double, d2: Double) =
      if (stdDev >= QL_EPSILON) {
        if (strike == 0.0) {
          1.0
        } else {
          nd.cumulativeProbability(d2)
        }
      } else {
        if (forward > strike) {
          1.0
        } else {
          0.0
        }
      }


  private fun calcAlpha(type: OptionType, cum_d1: Double) = when (type) {
    Call -> cum_d1
    Put -> -1.0 + cum_d1
  }

  private fun calcBeta(type: OptionType, cum_d2: Double) = when (type) {
    Call -> -cum_d2
    Put -> 1.0 - cum_d2
  }

  companion object {
    val QL_EPSILON = ulp(1.0)
    val nd = NormalDistribution()
  }
  
}