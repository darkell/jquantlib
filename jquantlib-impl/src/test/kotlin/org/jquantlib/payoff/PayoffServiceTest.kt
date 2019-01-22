package org.jquantlib.payoff

import org.jquantlib.api.data.AssetOrNothingPayoff
import org.jquantlib.api.data.CashOrNothingPayoff
import org.jquantlib.api.data.GapPayoff
import org.jquantlib.api.data.PlainVanillaPayoff
import org.jquantlib.api.data.OptionType.*
import org.junit.Assert.assertEquals
import org.junit.Test

class PayoffServiceTest {

  private val payoffService =  PayoffServiceImpl()

  @Test
  fun plainVanillaPayoff_Call() {
    val plainVanillaPayoff = PlainVanillaPayoff(
        type = Call,
        strike = 10.0
    )

    assertEquals(
        0.0,
        payoffService.get(
            payoff = plainVanillaPayoff,
            price = 7.5
        ),
        1e-10
    )

    assertEquals(
        7.0,
        payoffService.get(
            payoff = plainVanillaPayoff,
            price = 17.0
        ),
        1e-10
    )
  }

  @Test
  fun plainVanillaPayoff_Put() {
    val plainVanillaPayoff = PlainVanillaPayoff(
        type = Put,
        strike = 10.0
    )

    assertEquals(
        2.5,
        payoffService.get(
            payoff = plainVanillaPayoff,
            price = 7.5
        ),
        1e-10
    )

    assertEquals(
        0.0,
        payoffService.get(
            payoff = plainVanillaPayoff,
            price = 17.0
        ),
        1e-10
    )
  }

  @Test
  fun cashOrNothingPayoff_Call() {
    val cashOrNothingPayoff = CashOrNothingPayoff(
        type = Call,
        strike = 10.0,
        cashPayoff = 14.0
    )

    assertEquals(
        0.0,
        payoffService.get(
            payoff = cashOrNothingPayoff,
            price = 7.5
        ),
        1e-10
    )

    assertEquals(
        14.0,
        payoffService.get(
            payoff = cashOrNothingPayoff,
            price = 17.0
        ),
        1e-10
    )
  }

  @Test
  fun cashOrNothingPayoff_Put() {
    val cashOrNothingPayoff = CashOrNothingPayoff(
        type = Put,
        strike = 10.0,
        cashPayoff = 14.0
    )

    assertEquals(
        14.0,
        payoffService.get(
            payoff = cashOrNothingPayoff,
            price = 7.5
        ),
        1e-10
    )

    assertEquals(
        0.0,
        payoffService.get(
            payoff = cashOrNothingPayoff,
            price = 17.0
        ),
        1e-10
    )
  }

  @Test
  fun assetOrNothingPayoff_Call() {
    val assetOrNothingPayoff = AssetOrNothingPayoff(
        type = Call,
        strike = 10.0
    )

    assertEquals(
        0.0,
        payoffService.get(
            payoff = assetOrNothingPayoff,
            price = 7.5
        ),
        1e-10
    )

    assertEquals(
        17.0,
        payoffService.get(
            payoff = assetOrNothingPayoff,
            price = 17.0
        ),
        1e-10
    )
  }

  @Test
  fun assetOrNothingPayoff_Put() {
    val assetOrNothingPayoff = AssetOrNothingPayoff(
        type = Put,
        strike = 10.0
    )

    assertEquals(
        7.5,
        payoffService.get(
            payoff = assetOrNothingPayoff,
            price = 7.5
        ),
        1e-10
    )

    assertEquals(
        0.0,
        payoffService.get(
            payoff = assetOrNothingPayoff,
            price = 17.0
        ),
        1e-10
    )
  }

  @Test
  fun gapPayoff_Call() {
    val gapPayoff = GapPayoff(
        type = Call,
        strike = 10.0,
        secondStrike = 14.0
    )

    assertEquals(
        0.0,
        payoffService.get(
            payoff = gapPayoff,
            price = 7.5
        ),
        1e-10
    )

    assertEquals(
        3.0,
        payoffService.get(
            payoff = gapPayoff,
            price = 17.0
        ),
        1e-10
    )
  }

  @Test
  fun gapPayoff_Put() {
    val gapPayoff = GapPayoff(
        type = Put,
        strike = 10.0,
        secondStrike = 14.0
    )

    assertEquals(
        6.5,
        payoffService.get(
            payoff = gapPayoff,
            price = 7.5
        ),
        1e-10
    )

    assertEquals(
        0.0,
        payoffService.get(
            payoff = gapPayoff,
            price = 17.0
        ),
        1e-10
    )
  }

}