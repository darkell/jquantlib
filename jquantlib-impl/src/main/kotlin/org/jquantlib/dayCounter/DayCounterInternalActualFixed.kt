package org.jquantlib.dayCounter

import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

sealed class DayCounterInternalActualFixed(private val divisor: Double): DayCounterInternal {
  override fun dayCount(start: LocalDate, end: LocalDate): Long {
    return DAYS.between(start, end)
  }

  override fun yearFraction(
      start: LocalDate,
      end: LocalDate,
      refPeriodStart: LocalDate?,
      refPeriodEnd: LocalDate?
  ): Double {
    return dayCount(start, end) / divisor
  }
}

class DayCounterInternalActualFixed360: DayCounterInternalActualFixed(360.0)
class DayCounterInternalActualFixed365: DayCounterInternalActualFixed(365.0)