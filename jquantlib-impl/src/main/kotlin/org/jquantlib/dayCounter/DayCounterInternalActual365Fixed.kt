package org.jquantlib.dayCounter

import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

object DayCounterInternalActual365Fixed: DayCounterInternal {
  override fun dayCount(start: LocalDate, end: LocalDate): Long {
    return DAYS.between(start, end)
  }

  override fun yearFraction(
      start: LocalDate,
      end: LocalDate,
      refPeriodStart: LocalDate?,
      refPeriodEnd: LocalDate?
  ): Double {
    return dayCount(start, end) / 365.0
  }
}