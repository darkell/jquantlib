package org.jquantlib.dayCounter

import org.jquantlib.daycounters.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import org.jquantlib.utils.Convertors.toQl

class DayCounterInternalTest {

  private val dayCounterPairs = listOf(
      ActualActual(ActualActual.Convention.AFB) to DayCounterInternalActualActualAFB(),
      ActualActual(ActualActual.Convention.ISDA) to DayCounterInternalActualActualISDA(),
      SimpleDayCounter() to DayCounterInternalThirty360Simple(),
      Thirty360() to DayCounterInternalThirty360USA(),
      Thirty360(Thirty360.Convention.European) to DayCounterInternalThirty360European(),
      Thirty360(Thirty360.Convention.Italian) to DayCounterInternalThirty360Italian(),
      Actual360() to DayCounterInternalActualFixed360(),
      Actual365Fixed() to DayCounterInternalActualFixed365()
  )

  @Test
  fun dayCount() {
    for (dayCounterPair in dayCounterPairs) {
      var oldDayCounter = dayCounterPair.first
      var newDayCounter = dayCounterPair.second

      for (i in 0L..1000L) {
        val start = LocalDate.of(2004, 1, 1).plusDays(i)
        for (j in -1000L..1000L) {
          val end = start.plusDays(j)

          assertEquals(
              "${newDayCounter.javaClass.simpleName} $start $end",
              oldDayCounter.dayCount(start.toQl(), end.toQl()),
              newDayCounter.dayCount(start, end)
          )
        }
      }
    }
  }

  @Test
  fun yearFraction() {
    for (dayCounterPair in dayCounterPairs) {
      var oldDayCounter = dayCounterPair.first
      var newDayCounter = dayCounterPair.second

      for (i in 0L..1000L) {
        val start = LocalDate.of(2004, 1, 1).plusDays(i)
        for (j in -1000L..1000L) {
          val end = start.plusDays(j)

          assertEquals(
              "${oldDayCounter.name()} ${newDayCounter.javaClass.simpleName} $start $end",
              oldDayCounter.yearFraction(start.toQl(), end.toQl()),
              newDayCounter.yearFraction(start, end),
              1e-10
          )
        }
      }
    }
  }

}