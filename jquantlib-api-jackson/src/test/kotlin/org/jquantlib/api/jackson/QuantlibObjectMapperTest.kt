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

import com.fasterxml.jackson.core.type.TypeReference
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

    testSerialization(ListDayCounterTypeReference, dayCounters)
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

    testSerialization(ListCalendarTypeReference, calendars)
  }

  @Test
  fun quoteMixIns() {
    val quotes: List<Quote> = listOf(
        SimpleQuote(
            value = 1.0
        ),
        SimpleQuote(
            value = 2.0
        )
    )

    testSerialization(ListQuoteTypeReference, quotes)
  }

  @Test
  fun payoffMixIns() {
    val payoffs: List<Payoff> = listOf(
        PlainVanillaPayoff(
            type = OptionType.Put,
            strike = 1.0
        ),
        CashOrNothingPayoff(
            type = OptionType.Put,
            strike = 1.0,
            cashPayoff = 2.0
        ),
        AssetOrNothingPayoff(
            type = OptionType.Put,
            strike = 1.0
        ),
        GapPayoff(
            type = OptionType.Put,
            strike = 1.0,
            secondStrike = 3.0
        )
    )

    testSerialization(ListPayoffTypeReference, payoffs)
  }

  @Test
  fun typePayoffMixIns() {
    val typePayoffs: List<TypePayoff> = listOf(
        PlainVanillaPayoff(
            type = OptionType.Put,
            strike = 1.0
        ),
        CashOrNothingPayoff(
            type = OptionType.Put,
            strike = 1.0,
            cashPayoff = 2.0
        ),
        AssetOrNothingPayoff(
            type = OptionType.Put,
            strike = 1.0
        ),
        GapPayoff(
            type = OptionType.Put,
            strike = 1.0,
            secondStrike = 3.0
        )
    )

    testSerialization(ListTypePayoffTypeReference, typePayoffs)
  }

  @Test
  fun strikedTypePayoffMixIns() {
    val strikedTypePayoff: List<StrikedTypePayoff> = listOf(
        PlainVanillaPayoff(
            type = OptionType.Put,
            strike = 1.0
        ),
        CashOrNothingPayoff(
            type = OptionType.Put,
            strike = 1.0,
            cashPayoff = 2.0
        ),
        AssetOrNothingPayoff(
            type = OptionType.Put,
            strike = 1.0
        ),
        GapPayoff(
            type = OptionType.Put,
            strike = 1.0,
            secondStrike = 3.0
        )
    )

    testSerialization(ListStrikedTypePayoffTypeReference, strikedTypePayoff)
  }

  private fun <T> testSerialization(tr: TypeReference<T>, o: T) {
    val str = wft(tr, o)

    assertEquals(
        o,
        rft(tr, str)
    )
  }

  private fun <T> wft(tr: TypeReference<T>, o: T) =
      mapper.writerWithDefaultPrettyPrinter().forType(tr).writeValueAsString(o)

  private fun <T> rft(tr: TypeReference<T>, str: String) =
      mapper.reader().forType(tr).readValue<T>(str)

}