package org.jquantlib.dayCounter

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DayCounterInternalActual360Test {

  @Test
  fun dayCount() {
    assertEquals(
        59,
        DayCounterInternalActual360.dayCount(
            LocalDate.of(2005, 2, 20),
            LocalDate.of(2005, 4, 20)
        )
    )
  }

  @Test
  fun yearFraction() {
    assertEquals(
        0.1638888888888889,
        DayCounterInternalActual360.yearFraction(
            LocalDate.of(2005, 2, 20),
            LocalDate.of(2005, 4, 20)
        ),
        1e-10
    )
  }

}