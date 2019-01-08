package org.jquantlib.dayCounter

import org.jquantlib.daycounters.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import org.jquantlib.utils.Convertors.toQl
import org.junit.Ignore

class DayCounterInternalTest2 {

  @Test
  @Ignore
  fun yearFraction() {
    val oldDayCounter = ActualActual(ActualActual.Convention.AFB)
    val newDayCounter = DayCounterInternalActualActualAFB

    val start = LocalDate.of(2004, 1, 1)
    val end = LocalDate.of(2005, 1, 2)

    assertEquals(
        "${oldDayCounter.name()} ${newDayCounter.javaClass.simpleName} $start $end",
        oldDayCounter.yearFraction(start.toQl(), end.toQl()),
        newDayCounter.yearFraction(start, end),
        1e-10
    )
  }

}