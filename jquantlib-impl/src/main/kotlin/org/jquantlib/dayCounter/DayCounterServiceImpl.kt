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

package org.jquantlib.dayCounter

import org.jquantlib.api.data.*
import org.jquantlib.api.service.CalendarService
import org.jquantlib.api.service.DayCounterService
import java.time.LocalDate

class DayCounterServiceImpl(
    private val calendarService: CalendarService
) : DayCounterService {

  override fun dayCount(dayCounter: DayCounter, start: LocalDate, end: LocalDate) =
      dayCounter.toInternal().dayCount(start, end)

  override fun yearFraction(dayCounter: DayCounter,
      start: LocalDate,
      end: LocalDate,
      refPeriodStart: LocalDate?,
      refPeriodEnd: LocalDate?
  ) = dayCounter.toInternal().yearFraction(start, end, refPeriodStart, refPeriodEnd)

  private fun DayCounter.toInternal() = when (this) {
    Actual365Fixed -> DayCounterInternalActualFixed365
    SimpleDayCounter -> DayCounterInternalThirty360Simple
    Actual360 -> DayCounterInternalActualFixed360
    Thirty360USA -> DayCounterInternalThirty360BondBasis
    Thirty360BondBasis -> DayCounterInternalThirty360BondBasis
    Thirty360European -> DayCounterInternalThirty360European
    Thirty360EurobondBasis -> DayCounterInternalThirty360European
    Thirty360Italian -> DayCounterInternalThirty360Italian
    ActualISMA -> DayCounterInternalActualActualISMA
    ActualBond -> DayCounterInternalActualActualISMA
    ActualISDA -> DayCounterInternalActualActualISDA
    ActualHistorical -> DayCounterInternalActualActualISDA
    Actual365 -> DayCounterInternalActualActualISDA
    ActualAFB -> DayCounterInternalActualActualAFB
    ActualEuro -> DayCounterInternalActualActualAFB
    is Business252 -> DayCounterInternalBusiness252(calendarService, calendar)
  }

}