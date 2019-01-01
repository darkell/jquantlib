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

sealed class Payoff

abstract class TypePayoff : Payoff()

abstract class StrikedTypePayoff : TypePayoff() {
  abstract val type: OptionType
  abstract val strike: Double
}

data class PlainVanillaPayoff(
    override val type: OptionType,
    override val strike: Double
): StrikedTypePayoff()

