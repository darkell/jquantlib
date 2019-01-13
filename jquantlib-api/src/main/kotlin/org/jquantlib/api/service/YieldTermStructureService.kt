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

import org.jquantlib.api.data.*
import org.jquantlib.api.data.Compounding.*
import org.jquantlib.api.data.Frequency.*
import java.time.LocalDate

interface YieldTermStructureService {

  fun referenceDate(
      calendar: Calendar,
      evaluationDate: LocalDate,
      settlementDays: Long
  ): LocalDate

  fun createFlatForward(
      calendar: Calendar,
      evaluationDate: LocalDate,
      settlementDays: Long,
      dayCounter: DayCounter,
      forward: Quote,
      compounding: Compounding = Continuous,
      frequency: Frequency = Annual
  ): FlatForward

  fun discount(
      yts: YieldTermStructure,
      d: LocalDate,
      extrapolate: Boolean = false
  ): Double

  fun discount(
      yts: YieldTermStructure,
      time: Double,
      extrapolate: Boolean = false
  ): Double

}