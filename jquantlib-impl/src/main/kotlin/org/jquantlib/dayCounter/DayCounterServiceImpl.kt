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

  private val simpleDayCounter = org.jquantlib.daycounters.SimpleDayCounter()
  private val thirty360USA = org.jquantlib.daycounters.Thirty360(Thirty360.Convention.USA)
  private val thirty360BondBasis = org.jquantlib.daycounters.Thirty360(Thirty360.Convention.BondBasis)
  private val thirty360European = org.jquantlib.daycounters.Thirty360(Thirty360.Convention.European)
  private val thirty360EurobondBasis = org.jquantlib.daycounters.Thirty360(Thirty360.Convention.EurobondBasis)
  private val thirty360Italian = org.jquantlib.daycounters.Thirty360(Thirty360.Convention.Italian)
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
      Actual365Fixed -> DayCounterInternalActual365Fixed.dayCount(start, end)
      SimpleDayCounter -> simpleDayCounter.dayCount(start.toQl(), end.toQl())
      Actual360 -> DayCounterInternalActual360.dayCount(start, end)
      Thirty360USA -> thirty360USA.dayCount(start.toQl(), end.toQl())
      Thirty360BondBasis -> thirty360BondBasis.dayCount(start.toQl(), end.toQl())
      Thirty360European -> thirty360European.dayCount(start.toQl(), end.toQl())
      Thirty360EurobondBasis -> thirty360EurobondBasis.dayCount(start.toQl(), end.toQl())
      Thirty360Italian -> thirty360Italian.dayCount(start.toQl(), end.toQl())
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
      Actual365Fixed -> DayCounterInternalActual365Fixed.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      SimpleDayCounter -> simpleDayCounter.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
      Actual360 -> DayCounterInternalActual360.yearFraction(start, end, refPeriodStart, refPeriodEnd)
      Thirty360USA -> thirty360USA.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
      Thirty360BondBasis -> thirty360BondBasis.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
      Thirty360European -> thirty360European.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
      Thirty360EurobondBasis -> thirty360EurobondBasis.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
      Thirty360Italian -> thirty360Italian.yearFraction(start.toQl(), end.toQl(), refPeriodStart?.toQl(), refPeriodEnd?.toQl())
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