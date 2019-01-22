package org.jquantlib.calculator

import com.fasterxml.jackson.core.type.TypeReference
import org.jquantlib.DataLoader.data
import org.jquantlib.api.data.OptionType
import org.jquantlib.api.data.PlainVanillaPayoff
import org.jquantlib.api.data.StrikedTypePayoff
import org.junit.Assert.assertEquals
import org.junit.Test

class BlackCalculatorServiceTest {

  private val blackCalculatorService = BlackCalculatorServiceImpl()

  @Test
  fun value() {
    data("/BlackCalculator_value.json", ListParamsTypeReference).forEach {
      assertEquals(
          "$it",
          it.expected,
          blackCalculatorService.value(
              blackCalculatorService.create(
                  payoff = it.strikedTypePayoff,
                  forward = it.forward,
                  stdDev = it.stdDev,
                  discount = it.discount
              )
          ),
          1e-10
      )
    }
  }

  object ListParamsTypeReference : TypeReference<List<Params>>()

  data class Params(
      val strikedTypePayoff: StrikedTypePayoff,
      val forward: Double,
      val stdDev: Double,
      val discount: Double,
      val expected: Double
  )

}