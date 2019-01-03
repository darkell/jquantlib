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
import org.jquantlib.daycounters.ActualActual
import java.time.LocalDate
import org.jquantlib.utils.Convertors.toQl

class DayCounterServiceImpl(
    private val calendarService: CalendarService
) : DayCounterService {

  private val dayCounterInternalActualFixed360 = DayCounterInternalActualFixed360()
  private val dayCounterInternalActualFixed365 = DayCounterInternalActualFixed365()
  private val dayCounterInternalThirty360Simple = DayCounterInternalThirty360Simple()
  private val dayCounterInternalThirty360BondBasis = DayCounterInternalThirty360BondBasis()
  private val dayCounterInternalThirty360European = DayCounterInternalThirty360European()
  private val dayCounterInternalThirty360Italian = DayCounterInternalThirty360Italian()
  private val actualISMA = org.jquantlib.daycounters.ActualActual(ActualActual.Convention.ISMA)
  private val actualBond = org.jquantlib.daycounters.ActualActual(ActualActual.Convention.Bond)
  private val dayCounterInternalActualActualISDA = DayCounterInternalActualActualISDA()
  private val dayCounterInternalActualActualAFB = DayCounterInternalActualActualAFB()

  override fun dayCount(
      dayCounter: DayCounter,
      start: LocalDate,
      end: LocalDate
  ): Long {
    return when (dayCounter) {
      Actual365Fixed -> dayCounterInternalActualFixed365.dayCount(start, end)
      SimpleDayCounter -> dayCounterInternalThirty360Simple.dayCount(start, end)
      Actual360 -> dayCounterInternalActualFixed360.dayCount(start, end)
      Thirty360USA -> dayCounterInternalThirty360BondBasis.dayCount(start, end)
      Thirty360BondBasis -> dayCounterInternalThirty360BondBasis.dayCount(start, end)
      Thirty360European -> dayCounterInternalThirty360European.dayCount(start, end)
      Thirty360EurobondBasis -> dayCounterInternalThirty360European.dayCount(start, end)
      Thirty360Italian -> dayCounterInternalThirty360Italian.dayCount(start, end)
      ActualISMA -> actualISMA.dayCount(start.toQl(), end.toQl())
      ActualBond -> actualBond.dayCount(start.toQl(), end.toQl())
      ActualISDA -> dayCounterInternalActualActualISDA.dayCount(start, end)
      ActualHistorical -> dayCounterInternalActualActualISDA.dayCount(start, end)
      Actual365 -> dayCounterInternalActualActualISDA.dayCount(start, end)
      ActualAFB -> dayCounterInternalActualActualAFB.dayCount(start, end)
      ActualEuro -> dayCounterInternalActualActualAFB.dayCount(start, end)
      is Business252 -> calendarService.businessDaysBetween(dayCounter.calendar, start, end)
    }
  }

  override fun yearFraction(
      dayCounter: DayCounter,
      start: LocalDate,
      end: LocalDate,
      refPeriodStart: LocalDate?,
      refPeriodEnd: LocalDate?
  ): Double {
    return when (dayCounter) {
      Actual365Fixed -> dayCounterInternalActualFixed365.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      SimpleDayCounter -> dayCounterInternalThirty360Simple.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      Actual360 -> dayCounterInternalActualFixed360.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      Thirty360USA -> dayCounterInternalThirty360BondBasis.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      Thirty360BondBasis -> dayCounterInternalThirty360BondBasis.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      Thirty360European -> dayCounterInternalThirty360European.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      Thirty360EurobondBasis -> dayCounterInternalThirty360European.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      Thirty360Italian -> dayCounterInternalThirty360Italian.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      ActualISMA -> actualISMA.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
      ActualBond -> actualBond.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
      ActualISDA -> dayCounterInternalActualActualISDA.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      ActualHistorical -> dayCounterInternalActualActualISDA.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      Actual365 -> dayCounterInternalActualActualISDA.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      ActualAFB -> dayCounterInternalActualActualAFB.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      ActualEuro -> dayCounterInternalActualActualAFB.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      is Business252 -> calendarService.businessDaysBetween(dayCounter.calendar, start, end) / 252.0
    }
  }

}