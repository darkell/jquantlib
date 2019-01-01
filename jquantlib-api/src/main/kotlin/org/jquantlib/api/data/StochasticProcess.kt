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

package org.jquantlib.api.data

sealed class StochasticProcess

abstract class StochasticProcess1D : StochasticProcess()

abstract class GeneralizedBlackScholesProcess : StochasticProcess1D()

data class BlackScholesMertonProcess(
  val x0: Quote,
  val dividendTS: YieldTermStructure,
  val riskFreeTS: YieldTermStructure,
  val blackVolTS: BlackVolTermStructure,
  val discretization: Discretization1D = EulerDiscretization()
): GeneralizedBlackScholesProcess()
