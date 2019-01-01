package org.jquantlib.dayCounter

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DayCounterInternalActual365FixedTest {

  @Test
  fun dayCount() {
    assertEquals(
        59,
        DayCounterInternalActual365Fixed.dayCount(
            LocalDate.of(2005, 2, 20),
            LocalDate.of(2005, 4, 20)
        )
    )
  }

  @Test
  fun yearFraction() {
    assertEquals(
        0.16164383561643836,
        DayCounterInternalActual365Fixed.yearFraction(
            LocalDate.of(2005, 2, 20),
            LocalDate.of(2005, 4, 20)
        ),
        1e-10
    )
  }

}