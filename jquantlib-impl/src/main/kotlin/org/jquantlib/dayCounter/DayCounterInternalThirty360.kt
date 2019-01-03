package org.jquantlib.dayCounter

import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

abstract class DayCounterInternalThirty360: DayCounterInternal {
  override fun dayCount(start: LocalDate, end: LocalDate): Long {
    return 360L * (end.year - start.year) +
        30L * (end.monthValue - start.monthValue) +
        calcDayOfMonthDiff(start, end)
  }

  abstract fun calcDayOfMonthDiff(start: LocalDate, end: LocalDate): Int

  override fun yearFraction(
      start: LocalDate,
      end: LocalDate,
      refPeriodStart: LocalDate?,
      refPeriodEnd: LocalDate?
  ): Double {
    return dayCount(start, end) / 360.0
  }
}

open class DayCounterInternalThirty360BondBasis: DayCounterInternalThirty360() {
  override fun calcDayOfMonthDiff(start: LocalDate, end: LocalDate): Int {
    return when {
      end.dayOfMonth == 31 && start.dayOfMonth == 30 -> 0
      start.dayOfMonth == 31 && end.dayOfMonth < 31 -> end.dayOfMonth - 30
      else -> end.dayOfMonth - start.dayOfMonth
    }
  }
}

class DayCounterInternalThirty360Simple: DayCounterInternalThirty360BondBasis() {
  override fun yearFraction(
      start: LocalDate,
      end: LocalDate,
      refPeriodStart: LocalDate?,
      refPeriodEnd: LocalDate?
  ): Double {
    return when {
      start.dayOfMonth == end.dayOfMonth -> simpleYearFraction(start, end)
      start.dayOfMonth > end.dayOfMonth && isEndOfMonth(end) -> simpleYearFraction(start, end)
      start.dayOfMonth < end.dayOfMonth && isEndOfMonth(start) -> simpleYearFraction(start, end)
      else -> super.yearFraction(start, end, refPeriodStart, refPeriodEnd)
    }
  }

  private fun isEndOfMonth(date: LocalDate): Boolean {
    return date == date.with(TemporalAdjusters.lastDayOfMonth())
  }

  private fun simpleYearFraction(start: LocalDate, end: LocalDate): Double {
    return (end.year - start.year) + (end.monthValue - start.monthValue) / 12.0
  }

}

class DayCounterInternalThirty360European: DayCounterInternalThirty360() {
  override fun calcDayOfMonthDiff(start: LocalDate, end: LocalDate): Int {
    return calcDayOfMonth(end) - calcDayOfMonth(start)
  }

  private fun calcDayOfMonth(date: LocalDate): Int {
    return when {
      date.dayOfMonth == 31 -> 30
      else -> date.dayOfMonth
    }
  }

}

class DayCounterInternalThirty360Italian: DayCounterInternalThirty360() {
  override fun calcDayOfMonthDiff(start: LocalDate, end: LocalDate): Int {
    return dayOfMonth(end) - dayOfMonth(start)
  }

  private fun dayOfMonth(date: LocalDate): Int {
    return when {
      date.monthValue == 2 && date.dayOfMonth > 27 -> 30
      date.dayOfMonth == 31 -> 30
      else -> date.dayOfMonth
    }
  }
}
