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

package org.jquantlib.utils

import org.jquantlib.api.data.*
import org.jquantlib.api.data.Compounding.*
import org.jquantlib.api.data.Frequency.*
import org.jquantlib.time.Date
import org.jquantlib.time.TimeUnit
import java.time.LocalDate
import java.time.Period

object Convertors {

  fun LocalDate.toQl(): Date {
    return Date(dayOfMonth, monthValue, year)
  }

  fun Date.fromQl(): LocalDate {
    return LocalDate.of(year(), month().value(), dayOfMonth())
  }

  fun BusinessDayConvention.toQl(): org.jquantlib.time.BusinessDayConvention {
    return when (this) {
      BusinessDayConvention.Following -> org.jquantlib.time.BusinessDayConvention.Following
      BusinessDayConvention.ModifiedFollowing -> org.jquantlib.time.BusinessDayConvention.ModifiedFollowing
      BusinessDayConvention.Preceding -> org.jquantlib.time.BusinessDayConvention.Preceding
      BusinessDayConvention.ModifiedPreceding -> org.jquantlib.time.BusinessDayConvention.ModifiedPreceding
      BusinessDayConvention.Unadjusted -> org.jquantlib.time.BusinessDayConvention.Unadjusted
    }
  }

  fun Period.toQl(): org.jquantlib.time.Period {
    return when {
      years > 0 -> org.jquantlib.time.Period(years, TimeUnit.Years)
      months > 0 -> org.jquantlib.time.Period(years, TimeUnit.Months)
      else -> org.jquantlib.time.Period(days, TimeUnit.Days)
    }
  }

  fun DayCounter.toQl(): org.jquantlib.daycounters.DayCounter {
    return when (this) {
      Actual365Fixed -> org.jquantlib.daycounters.Actual365Fixed()
      SimpleDayCounter -> org.jquantlib.daycounters.SimpleDayCounter()
      Actual360 -> org.jquantlib.daycounters.Actual360()
      Thirty360USA -> org.jquantlib.daycounters.Thirty360(org.jquantlib.daycounters.Thirty360.Convention.USA)
      Thirty360BondBasis -> org.jquantlib.daycounters.Thirty360(org.jquantlib.daycounters.Thirty360.Convention.BondBasis)
      Thirty360European -> org.jquantlib.daycounters.Thirty360(org.jquantlib.daycounters.Thirty360.Convention.European)
      Thirty360EurobondBasis -> org.jquantlib.daycounters.Thirty360(org.jquantlib.daycounters.Thirty360.Convention.EurobondBasis)
      Thirty360Italian -> org.jquantlib.daycounters.Thirty360(org.jquantlib.daycounters.Thirty360.Convention.Italian)
      ActualISMA -> org.jquantlib.daycounters.ActualActual(org.jquantlib.daycounters.ActualActual.Convention.ISMA)
      ActualBond -> org.jquantlib.daycounters.ActualActual(org.jquantlib.daycounters.ActualActual.Convention.Bond)
      ActualISDA -> org.jquantlib.daycounters.ActualActual(org.jquantlib.daycounters.ActualActual.Convention.ISDA)
      ActualHistorical -> org.jquantlib.daycounters.ActualActual(org.jquantlib.daycounters.ActualActual.Convention.Historical)
      Actual365 -> org.jquantlib.daycounters.ActualActual(org.jquantlib.daycounters.ActualActual.Convention.Actual365)
      ActualAFB -> org.jquantlib.daycounters.ActualActual(org.jquantlib.daycounters.ActualActual.Convention.AFB)
      ActualEuro -> org.jquantlib.daycounters.ActualActual(org.jquantlib.daycounters.ActualActual.Convention.Euro)
      is Business252 -> TODO()
    }
  }

  fun Compounding.toQl(): org.jquantlib.termstructures.Compounding {
    return when (this) {
      Simple -> org.jquantlib.termstructures.Compounding.Simple
      Compounded -> org.jquantlib.termstructures.Compounding.Compounded
      Continuous -> org.jquantlib.termstructures.Compounding.Continuous
      SimpleThenCompounded -> org.jquantlib.termstructures.Compounding.SimpleThenCompounded
      CompoundedThenSimple -> TODO("Old QL code doesn't have CompoundedThenSimple")
    }
  }

  fun Frequency.toQl(): org.jquantlib.time.Frequency {
    return when (this) {
      NoFrequency -> org.jquantlib.time.Frequency.NoFrequency
      Once -> org.jquantlib.time.Frequency.Once
      Annual -> org.jquantlib.time.Frequency.Annual
      Semiannual -> org.jquantlib.time.Frequency.Semiannual
      EveryFourthMonth -> org.jquantlib.time.Frequency.EveryFourthMonth
      Quarterly -> org.jquantlib.time.Frequency.Quarterly
      Bimonthly -> org.jquantlib.time.Frequency.Bimonthly
      Monthly -> org.jquantlib.time.Frequency.Monthly
      EveryFourthWeek -> org.jquantlib.time.Frequency.EveryFourthWeek
      Biweekly -> org.jquantlib.time.Frequency.Biweekly
      Weekly -> org.jquantlib.time.Frequency.Weekly
      Daily -> org.jquantlib.time.Frequency.Daily
      OtherFrequency -> org.jquantlib.time.Frequency.OtherFrequency
    }
  }

}