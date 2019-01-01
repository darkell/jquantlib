package org.jquantlib.dayCounter

import java.time.LocalDate

interface DayCounterInternal {

  fun dayCount(
      start: LocalDate,
      end: LocalDate
  ): Long

  fun yearFraction(
      start: LocalDate,
      end: LocalDate,
      refPeriodStart: LocalDate? = null,
      refPeriodEnd: LocalDate? = null
  ): Double

}