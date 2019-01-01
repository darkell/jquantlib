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

package org.jquantlib.api.service

import org.jquantlib.api.data.DayCounter
import java.time.LocalDate

interface DayCounterService {

  /**
   * Returns the number of days between two dates
   *
   * @param dayCounter
   * @param start is the starting Date
   * @param end is the ending Date
   * @return the number of days between two dates.
   */
  fun dayCount(
      dayCounter: DayCounter,
      start: LocalDate,
      end: LocalDate
  ): Long

  /**
   * Returns the period between two dates as a fraction of year, considering referencing dates for both.
   *
   * @param dayCounter
   * @param start
   * @param end
   * @param refPeriodStart
   * @param refPeriodEnd
   * @return the period between two dates as a fraction of year, considering referencing dates for both.
   */
  fun yearFraction(
      dayCounter: DayCounter,
      start: LocalDate,
      end: LocalDate,
      refPeriodStart: LocalDate? = null,
      refPeriodEnd: LocalDate? = null
  ): Double

}