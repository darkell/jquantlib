package org.jquantlib.payoff

import org.jquantlib.api.data.*
import org.jquantlib.api.data.OptionType.*
import org.jquantlib.api.service.PayoffService
import java.lang.Math.*

class PayoffServiceImpl : PayoffService {
  override fun get(payoff: Payoff, price: Double) =
      when (payoff) {
        is PlainVanillaPayoff -> payoff.get(price)
        is CashOrNothingPayoff -> payoff.get(price)
        is AssetOrNothingPayoff -> payoff.get(price)
        is GapPayoff -> payoff.get(price)
      }

  private fun PlainVanillaPayoff.get(price: Double) =
      when (type) {
        Call -> max(price - strike, 0.0)
        Put -> max(strike - price, 0.0)
      }

  private fun CashOrNothingPayoff.get(price: Double) =
      when (type) {
        Call -> if (price - strike > 0.0) cashPayoff else 0.0
        Put -> if (strike - price > 0.0) cashPayoff else 0.0
      }

  private fun AssetOrNothingPayoff.get(price: Double) =
      when (type) {
        Call -> if (price - strike > 0.0) price else 0.0
        Put -> if (strike - price > 0.0) price else 0.0
      }

  private fun GapPayoff.get(price: Double) =
      when (type) {
        Call -> if (price - strike >= 0.0) price - secondStrike else 0.0
        Put -> if (strike - price >= 0.0) secondStrike - price else 0.0
      }

}