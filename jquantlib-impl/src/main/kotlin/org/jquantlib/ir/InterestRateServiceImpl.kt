/*
 Copyright (C) 2019 David Arkell

 This source code is release under the BSD License.

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.ir

import org.jquantlib.QL
import org.jquantlib.api.data.Compounding.*
import org.jquantlib.api.data.Frequency
import org.jquantlib.api.data.InterestRate
import org.jquantlib.api.data.toDouble
import org.jquantlib.api.service.InterestRateService

class InterestRateServiceImpl : InterestRateService {

  @Strictfp
  override fun discountFactor(interestRate: InterestRate, t: Double): Double {
    val factor = compoundFactor(interestRate, t)
    return 1.0 / factor
  }

  @Strictfp
  override fun compoundFactor(interestRate: InterestRate, time: Double): Double {
    QL.require(time >= 0.0 , "negative time not allowed")
    // QL.require(!Double.isNaN(rate) , "null interest rate")

    // TODO: code review :: please verify against QL/C++ code
    // if (rate<0.0) throw new IllegalArgumentException("null interest rate");

    val freq = interestRate.frequency.toDouble()

    return when (interestRate.compound) {
      Simple -> 1.0 + interestRate.rate * time
      Compounded -> Math.pow((1 + interestRate.rate / freq), (freq * time))
      Continuous -> Math.exp(interestRate.rate * time)
      SimpleThenCompounded -> {
        if (time < (1 / freq)) {
          1.0 + interestRate.rate * time
        } else {
          Math.pow(1 + interestRate.rate / freq, (freq * time))
        }
      }
    }
  }
}