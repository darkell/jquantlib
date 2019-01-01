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

sealed class Instrument

abstract class Option: Instrument() {
  abstract val payoff: Payoff
  abstract val exercise: Exercise
}

abstract class AbstractOneAssetOption : Option()

data class OneAssetOption(
    override val payoff: Payoff,
    override val exercise: Exercise
): AbstractOneAssetOption()

abstract class AbstractVanillaOption : Option()

data class VanillaOption(
    override val payoff: Payoff,
    override val exercise: Exercise
): AbstractVanillaOption()

abstract class AbstractEuropeanOption : AbstractVanillaOption()

data class EuropeanOption(
    override val payoff: StrikedTypePayoff,
    override val exercise: EuropeanExercise
): AbstractEuropeanOption()

abstract class AbstractDividendVanillaOption : AbstractVanillaOption()

data class DividendVanillaOption(
    override val payoff: Payoff,
    override val exercise: Exercise
): AbstractDividendVanillaOption()
