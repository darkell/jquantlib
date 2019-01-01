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

package org.jquantlib.api.jackson

import com.fasterxml.jackson.databind.module.SimpleModule
import org.jquantlib.api.data.*
import org.jquantlib.api.data.Target

class QuantlibModule : SimpleModule("Quantlib") {
  init {
    setMixInAnnotation(DayCounter::class.java, DayCounterMixIn::class.java)
    setMixInAnnotation(Actual365Fixed::class.java, Actual365FixedMixIn::class.java)
    setMixInAnnotation(SimpleDayCounter::class.java, SimpleDayCounterMixIn::class.java)
    setMixInAnnotation(Actual360::class.java, Actual360MixIn::class.java)
    setMixInAnnotation(Thirty360USA::class.java, Thirty360USAMixIn::class.java)
    setMixInAnnotation(Thirty360BondBasis::class.java, Thirty360BondBasisMixIn::class.java)
    setMixInAnnotation(Thirty360European::class.java, Thirty360EuropeanMixIn::class.java)
    setMixInAnnotation(Thirty360EurobondBasis::class.java, Thirty360EurobondBasisMixIn::class.java)
    setMixInAnnotation(Thirty360Italian::class.java, Thirty360ItalianMixIn::class.java)
    setMixInAnnotation(ActualISMA::class.java, ActualISMAMixIn::class.java)
    setMixInAnnotation(ActualBond::class.java, ActualBondMixIn::class.java)
    setMixInAnnotation(ActualISDA::class.java, ActualISDAMixIn::class.java)
    setMixInAnnotation(ActualHistorical::class.java, ActualHistoricalMixIn::class.java)
    setMixInAnnotation(Actual365::class.java, Actual365MixIn::class.java)
    setMixInAnnotation(ActualAFB::class.java, ActualAFBMixIn::class.java)
    setMixInAnnotation(ActualEuro::class.java, ActualEuroMixIn::class.java)
    setMixInAnnotation(Business252::class.java, Business252MixIn::class.java)

    setMixInAnnotation(Calendar::class.java, CalendarMixIn::class.java)
    setMixInAnnotation(Australia::class.java, AustraliaMixIn::class.java)
    setMixInAnnotation(UnitedStatesSettlement::class.java, UnitedStatesSettlementMixIn::class.java)
    setMixInAnnotation(UnitedStatesNyse::class.java, UnitedStatesNyseMixIn::class.java)
    setMixInAnnotation(UnitedStatesGovernmentBond::class.java, UnitedStatesGovernmentBondMixIn::class.java)
    setMixInAnnotation(UnitedStatesNerc::class.java, UnitedStatesNercMixIn::class.java)
    setMixInAnnotation(Target::class.java, TargetMixIn::class.java)
  }
}
