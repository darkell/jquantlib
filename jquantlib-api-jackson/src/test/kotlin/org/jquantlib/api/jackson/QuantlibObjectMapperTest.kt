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

import org.jquantlib.api.data.*
import org.junit.Assert.assertEquals
import org.junit.Test

class QuantlibObjectMapperTest {

  private val mapper = QuantlibObjectMapperFactory.build()

  @Test
  fun dayCounterMixIns() {
    val dayCounters: List<DayCounter> = listOf(
        Actual365Fixed,
        SimpleDayCounter,
        Actual360,
        Thirty360USA,
        Thirty360BondBasis,
        Thirty360European,
        Thirty360EurobondBasis,
        Thirty360Italian,
        ActualISMA,
        ActualBond,
        ActualISDA,
        ActualHistorical,
        Actual365,
        ActualAFB,
        ActualEuro,
        Business252(
            calendar = UnitedStates
        )
    )

    val writer = mapper.writerWithDefaultPrettyPrinter().forType(ListDayCounterTypeReference)
    val reader = mapper.reader().forType(ListDayCounterTypeReference)

    val str = writer.writeValueAsString(dayCounters)
    val deserialized = reader.readValue<List<DayCounter>>(str)

    assertEquals(
        dayCounters,
        deserialized
    )
  }

  @Test
  fun calendarMixIns() {
    val calendars: List<Calendar> = listOf(
        Australia,
        UnitedStatesSettlement,
        UnitedStatesNyse,
        UnitedStatesGovernmentBond,
        UnitedStatesNerc,
        Target
    )

    val writer = mapper.writerWithDefaultPrettyPrinter().forType(ListCalendarTypeReference)
    val reader = mapper.reader().forType(ListCalendarTypeReference)

    val str = writer.writeValueAsString(calendars)
    val deserialized = reader.readValue<List<Calendar>>(str)

    assertEquals(
        calendars,
        deserialized
    )
  }

}