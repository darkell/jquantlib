package org.jquantlib.api.service

import org.jquantlib.api.data.Payoff

interface PayoffService {

  /**
   * Returns the value of an {@link Instrument} at maturity under {@link Payoff} conditions
   */
  fun get(payoff: Payoff, price: Double): Double

}