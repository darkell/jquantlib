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
import org.jquantlib.daycounters.Thirty360
import java.time.LocalDate
import org.jquantlib.utils.Convertors.toQl

class DayCounterServiceImpl(
    private val calendarService: CalendarService
) : DayCounterService {

  private val dayCounterInternalActualFixed360 = DayCounterInternalActualFixed360()
  private val dayCounterInternalActualFixed365 = DayCounterInternalActualFixed365()
  private val dayCounterInternalThirty360Simple = DayCounterInternalThirty360Simple()
  private val dayCounterInternalThirty360USA = DayCounterInternalThirty360USA()
  private val dayCounterInternalThirty360European = DayCounterInternalThirty360European()
  private val dayCounterInternalThirty360Italian = DayCounterInternalThirty360Italian()
  private val actualISMA = org.jquantlib.daycounters.ActualActual(ActualActual.Convention.ISMA)
  private val actualBond = org.jquantlib.daycounters.ActualActual(ActualActual.Convention.Bond)
  private val actualISDA = org.jquantlib.daycounters.ActualActual(ActualActual.Convention.ISDA)
  private val actualHistorical = org.jquantlib.daycounters.ActualActual(ActualActual.Convention.Historical)
  private val actual365 = org.jquantlib.daycounters.ActualActual(ActualActual.Convention.Actual365)
  private val actualAFB = org.jquantlib.daycounters.ActualActual(ActualActual.Convention.AFB)
  private val actualEuro = org.jquantlib.daycounters.ActualActual(ActualActual.Convention.Euro)

  override fun dayCount(
      dayCounter: DayCounter,
      start: LocalDate,
      end: LocalDate
  ): Long {
    return when (dayCounter) {
      Actual365Fixed -> dayCounterInternalActualFixed365.dayCount(start, end)
      SimpleDayCounter -> dayCounterInternalThirty360Simple.dayCount(start, end)
      Actual360 -> dayCounterInternalActualFixed360.dayCount(start, end)
      Thirty360USA -> dayCounterInternalThirty360USA.dayCount(start, end)
      Thirty360BondBasis -> dayCounterInternalThirty360USA.dayCount(start, end)
      Thirty360European -> dayCounterInternalThirty360European.dayCount(start, end)
      Thirty360EurobondBasis -> dayCounterInternalThirty360European.dayCount(start, end)
      Thirty360Italian -> dayCounterInternalThirty360Italian.dayCount(start, end)
      ActualISMA -> actualISMA.dayCount(start.toQl(), end.toQl())
      ActualBond -> actualBond.dayCount(start.toQl(), end.toQl())
      ActualISDA -> actualISDA.dayCount(start.toQl(), end.toQl())
      ActualHistorical -> actualHistorical.dayCount(start.toQl(), end.toQl())
      Actual365 -> actual365.dayCount(start.toQl(), end.toQl())
      ActualAFB -> actualAFB.dayCount(start.toQl(), end.toQl())
      ActualEuro -> actualEuro.dayCount(start.toQl(), end.toQl())
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
      Thirty360USA -> dayCounterInternalThirty360USA.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      Thirty360BondBasis -> dayCounterInternalThirty360USA.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      Thirty360European -> dayCounterInternalThirty360European.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      Thirty360EurobondBasis -> dayCounterInternalThirty360European.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      Thirty360Italian -> dayCounterInternalThirty360Italian.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      ActualISMA -> actualISMA.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
      ActualBond -> actualBond.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
      ActualISDA -> actualISDA.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
      ActualHistorical -> actualHistorical.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
      Actual365 -> actual365.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
      ActualAFB -> actualAFB.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
      ActualEuro -> actualEuro.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
      is Business252 -> calendarService.businessDaysBetween(dayCounter.calendar, start, end) / 252.0
    }
  }

}