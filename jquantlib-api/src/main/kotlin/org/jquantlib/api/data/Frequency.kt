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

import org.jquantlib.api.data.Frequency.*

/**
 * Frequency of events
 *
 * @author Richard Gomes
 */
enum class Frequency {
  /** null frequency  */
  NoFrequency,
  /** only once  */
  Once,
  /** once a year  */
  Annual,
  /** twice a year  */
  Semiannual,
  /** every fourth month  */
  EveryFourthMonth,
  /** every third month  */
  Quarterly,
  /** every second month  */
  Bimonthly,
  /** once a month  */
  Monthly,
  /** every fourth week  */
  EveryFourthWeek,
  /** every second week  */
  Biweekly,
  /** once a week  */
  Weekly,
  /** once a day  */
  Daily,
  /** some other unknown frequency  */
  OtherFrequency
}

fun Frequency.toDouble(): Double {
  return when (this) {
    NoFrequency -> -1.0
    Once -> 0.0
    Annual -> 1.0
    Semiannual -> 2.0
    EveryFourthMonth -> 3.0
    Quarterly -> 4.0
    Bimonthly -> 6.0
    Monthly -> 12.0
    EveryFourthWeek -> 13.0
    Biweekly -> 26.0
    Weekly -> 52.0
    Daily -> 365.0
    OtherFrequency -> 999.0
  }
}