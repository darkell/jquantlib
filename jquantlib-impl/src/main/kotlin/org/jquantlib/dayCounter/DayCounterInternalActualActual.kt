package org.jquantlib.dayCounter

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters


abstract class DayCounterInternalActualActual: DayCounterInternal {
  override fun dayCount(start: LocalDate, end: LocalDate): Long {
    return ChronoUnit.DAYS.between(start, end)
  }

}

class DayCounterInternalActualActualISDA: DayCounterInternalActualActual() {
  override fun yearFraction(
      start: LocalDate,
      end: LocalDate,
      refPeriodStart: LocalDate?,
      refPeriodEnd: LocalDate?
  ): Double {
    return when {
      start == end -> 0.0
      start.isAfter(end) -> -yearFraction(end, start, refPeriodStart, refPeriodEnd)
      else -> end.year - start.year - 1 + startYearFraction(start) + endYearFraction(end)
    }
  }

  private fun startYearFraction(start: LocalDate): Double {
    val divisor = divisor(start)
    return (divisor - start.dayOfYear + 1) / divisor
  }

  private fun endYearFraction(end: LocalDate) =
      (end.dayOfYear - 1) / divisor(end)

  private fun divisor(date: LocalDate) =
      if (date.isLeapYear) 366.0 else 365.0
}

class DayCounterInternalActualActualAFB: DayCounterInternalActualActual() {
  override fun yearFraction(
      start: LocalDate,
      end: LocalDate,
      refPeriodStart: LocalDate?,
      refPeriodEnd: LocalDate?
  ): Double {
    return when {
      start == end -> 0.0
      start.isAfter(end) -> -yearFraction(end, start, refPeriodStart, refPeriodEnd)
      else -> {
        val wholeYears = wholeYearsDifference(start, end)
        val updatedEnd = if (wholeYears > 0) minusYearsAndAdjust(end, wholeYears) else end
        var divisor = if (containsFebruary29(start, updatedEnd)) 366.0 else 365.0

        wholeYears + dayCount(start, updatedEnd) / divisor
      }
    }
  }

  private fun containsFebruary29(start: LocalDate, end: LocalDate): Boolean {
    return when {
      start.isLeapYear -> {
        val feb29 = LocalDate.of(start.year, 2, 29)
        end.isAfter(feb29) && !start.isAfter(feb29)
      }
      end.isLeapYear -> {
        val feb29 = LocalDate.of(end.year, 2, 29)
        end.isAfter(feb29) && !start.isAfter(feb29)
      }
      else -> false
    }
  }

  private fun wholeYearsDifference(start: LocalDate, end: LocalDate): Int {
    return when {
      start.year == end.year -> 0
      start.monthValue > end.monthValue -> end.year - start.year - 1
      start.monthValue == end.monthValue && isEndOfMonth(start) && isEndOfMonth(end) -> end.year - start.year
      start.monthValue == end.monthValue && start.dayOfMonth > end.dayOfMonth -> end.year - start.year - 1
      else -> end.year - start.year
    }
  }

  private fun minusYearsAndAdjust(date: LocalDate, years: Int): LocalDate {
    val unadjusted = date.minusYears(years.toLong())
    return if (unadjusted.isLeapYear && unadjusted.monthValue == 2 && unadjusted.dayOfMonth == 28) {
      return LocalDate.of(unadjusted.year, 2, 29)
    } else {
      unadjusted
    }
  }

  private fun isEndOfMonth(date: LocalDate): Boolean {
    return date == date.with(TemporalAdjusters.lastDayOfMonth())
  }
}
