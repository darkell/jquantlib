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

sealed class DayCounter {
  override fun toString(): String {
    return javaClass.simpleName
  }
}

object Actual365Fixed: DayCounter()
object SimpleDayCounter: DayCounter()
object Actual360: DayCounter()

typealias Thirty360 = Thirty360BondBasis

object Thirty360USA: DayCounter()
object Thirty360BondBasis: DayCounter()
object Thirty360European: DayCounter()
object Thirty360EurobondBasis: DayCounter()
object Thirty360Italian: DayCounter()

object ActualISMA: DayCounter()
object ActualBond: DayCounter()
object ActualISDA: DayCounter()
object ActualHistorical: DayCounter()
object Actual365: DayCounter()
object ActualAFB: DayCounter()
object ActualEuro: DayCounter()

data class Business252(
    val calendar: Calendar
): DayCounter()