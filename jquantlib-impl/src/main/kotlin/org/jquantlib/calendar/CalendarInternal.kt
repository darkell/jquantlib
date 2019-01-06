package org.jquantlib.calendar

import org.jquantlib.api.data.BusinessDayConvention
import org.jquantlib.api.data.BusinessDayConvention.*
import java.time.DayOfWeek.*
import java.time.LocalDate
import java.time.Month.*
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.*
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalAdjusters.*

sealed class CalendarInternal(
    private val isWeekendFun: (LocalDate) -> Boolean,
    private val isHolidayFuns: List<(LocalDate) -> Boolean>
) {

  fun isBusinessDay(date: LocalDate) = !isHoliday(date)

  fun isHoliday(date: LocalDate) = isHolidayFuns.any { it(date) } || isWeekendFun(date)

  fun isWeekend(date: LocalDate) = isWeekendFun(date)

  fun isEndOfMonth(date: LocalDate) = date.dayOfMonth >= endOfMonth(date).dayOfMonth

  fun endOfMonth(date: LocalDate) = adjust(date.with(lastDayOfMonth()), Preceding)

  fun adjust(
      date: LocalDate,
      c: BusinessDayConvention
  ): LocalDate {
    return when (c) {
      Following -> adjustFollowing(date)
      ModifiedFollowing -> adjustModifiedFollowing(date)
      Preceding -> adjustPreceding(date)
      ModifiedPreceding -> adjustModifiedPreceding(date)
      Unadjusted -> date
      HalfMonthModifiedFollowing -> adjustHalfMonthModifiedFollowing(date)
      Nearest -> adjustNearest(date)
    }
  }

  fun advance(
      date: LocalDate,
      n: Long,
      unit: ChronoUnit,
      c: BusinessDayConvention = Following,
      endOfMonth: Boolean = false
  ): LocalDate {
    validateChronoUnit(unit)
    return if (n == 0L) {
      adjust(date, c)
    } else {
      advanceNonZero(date, n, unit, c, endOfMonth)
    }
  }

  fun businessDaysBetween(
      from: LocalDate,
      to: LocalDate,
      includeFirst: Boolean = true,
      includeLast: Boolean = false
  ): Long {
    return when {
      from == to -> 0L
      to.isBefore(from) -> -businessDaysBetween(to, from, includeLast, includeFirst)
      else -> {
        val start = if (includeFirst) from else from.plusDays(1L)
        val end = if (includeLast) to.plusDays(1L) else to

        generateSequence(start) { it.plusDays(1) }
            .takeWhile { it.isBefore(end) }
            .filter(this::isBusinessDay)
            .count()
            .toLong()
      }
    }
  }

  private fun validateChronoUnit(unit: ChronoUnit) {
    require(unit == DAYS || unit == WEEKS || unit == MONTHS || unit == YEARS) {
      "ChronoUnit must be one of: $DAYS, $WEEKS, $MONTHS, $YEARS"
    }
  }

  private fun advanceNonZero(
      date: LocalDate,
      n: Long,
      unit: ChronoUnit,
      c: BusinessDayConvention = Following,
      endOfMonth: Boolean = false
  ): LocalDate {
    return when (unit) {
      DAYS -> plusOrMinusDats(date, n)
      WEEKS -> adjust(date.plusWeeks(n), c)
      MONTHS -> endOfMonthCalc(endOfMonth, date, date.plusMonths(n), c)
      YEARS -> endOfMonthCalc(endOfMonth, date, date.plusYears(n), c)
      else -> LocalDate.MIN // This is never reached
    }
  }

  private fun plusOrMinusDats(date: LocalDate, days: Long): LocalDate {
    return if (days > 0) {
      plusDays(date.plusDays(1L), days - 1L)
    } else {
      minusDays(date.minusDays(1L), days + 1L)
    }
  }

  private tailrec fun plusDays(date: LocalDate, days: Long): LocalDate {
    return when {
      isHoliday(date) -> plusDays(date.plusDays(1L), days)
      days == 0L -> date
      else -> plusDays(date.plusDays(1L), days - 1L)
    }
  }

  private tailrec fun minusDays(date: LocalDate, days: Long): LocalDate {
    return when {
      isHoliday(date) -> minusDays(date.minusDays(1L), days)
      days == 0L -> date
      else -> minusDays(date.minusDays(1L), days + 1L)
    }
  }

  private fun endOfMonthCalc(
      endOfMonth: Boolean,
      initialDate: LocalDate,
      finalDate: LocalDate,
      c: BusinessDayConvention
  ): LocalDate {
    return if (endOfMonth && isEndOfMonth(initialDate)) {
      endOfMonth(finalDate)
    } else {
      adjust(finalDate, c)
    }
  }

  private fun adjustModifiedFollowing(date: LocalDate): LocalDate {
    val adjusted = adjustFollowing(date)
    return if (date.month == adjusted.month) {
      adjusted
    } else {
      adjustPreceding(date)
    }
  }

  private tailrec fun adjustFollowing(date: LocalDate): LocalDate {
    return if (isHoliday(date)) {
      adjustFollowing(date.plusDays(1L))
    } else {
      date
    }
  }

  private fun adjustModifiedPreceding(date: LocalDate): LocalDate {
    val adjusted = adjustPreceding(date)
    return if (date.month == adjusted.month) {
      adjusted
    } else {
      adjustFollowing(date)
    }
  }

  private tailrec fun adjustPreceding(date: LocalDate): LocalDate {
    return if (isHoliday(date)) {
      adjustPreceding(date.minusDays(1L))
    } else {
      date
    }
  }

  private fun adjustHalfMonthModifiedFollowing(date: LocalDate): LocalDate {
    return if (date.dayOfMonth <= 15) {
      val adjusted = adjustFollowing(date)
      if (adjusted.month == date.month && adjusted.dayOfMonth <= 15) {
        adjusted
      } else {
        adjustPreceding(date)
      }
    } else {
      adjustModifiedFollowing(date)
    }
  }

  private fun adjustNearest(date: LocalDate): LocalDate {
    val preceding = adjustPreceding(date)
    val following = adjustFollowing(date)

    return if (DAYS.between(preceding, date) < DAYS.between(date, following)) {
      preceding
    } else {
      following
    }
  }

  companion object {
    val isSaturdayOrSunday: (LocalDate) -> Boolean = { it.dayOfWeek == SATURDAY || it.dayOfWeek == SUNDAY }

    val isJanuaryFirst: (LocalDate) -> Boolean = { it.month == JANUARY && it.dayOfMonth == 1 }
    val isJanuaryFirstMovedFromSundayToMonday: (LocalDate) -> Boolean = { it.month == JANUARY && it.dayOfMonth == 2 && it.dayOfWeek == MONDAY }
    val isJanuaryFirstMovedFromSaturdayToFriday: (LocalDate) -> Boolean = { it.month == DECEMBER && it.dayOfMonth == 31 && it.dayOfWeek == FRIDAY }

    // Martin Luther King's Birthday (third Monday in January)
    val isMartinLutherKingsBirthday: (LocalDate) -> Boolean = { it.month == JANUARY && it.dayOfMonth >= 15 && it.dayOfMonth <= 21 && it.dayOfWeek == MONDAY }

    // Washington's birthday (third Monday in February)
    val isWashingtonsBirthday: (LocalDate) -> Boolean = { it.month == FEBRUARY && it.dayOfMonth >= 15 && it.dayOfMonth <= 21 && it.dayOfWeek == MONDAY }

    // Memorial Day (last Monday in May)
    val isMemorialDay: (LocalDate) -> Boolean = { it.month == MAY && it.dayOfMonth >= 25 && it.dayOfWeek == MONDAY }

    val isIndependenceDay: (LocalDate) -> Boolean = { it.month == JULY && it.dayOfMonth == 4 }
    val isIndependenceDayMovedFromSundayToMonday: (LocalDate) -> Boolean = { it.month == JULY && it.dayOfMonth == 5 && it.dayOfWeek == MONDAY }
    val isIndependenceDayMovedFromSaturdayToFriday: (LocalDate) -> Boolean = { it.month == JULY && it.dayOfMonth == 3 && it.dayOfWeek == FRIDAY }

    val isLaborDay: (LocalDate) -> Boolean = { it.month == SEPTEMBER && it.dayOfMonth <= 7 && it.dayOfWeek == MONDAY }
    val isLaborDayOnOrAfterYear2000: (LocalDate) -> Boolean = { it.year >= 2000 && it.month == MAY && it.dayOfMonth == 1 }

    val isColumbusDay: (LocalDate) -> Boolean = { it.month == OCTOBER && it.dayOfMonth >= 8 && it.dayOfMonth <= 14 && it.dayOfWeek == MONDAY }

    val isVeteransDay: (LocalDate) -> Boolean = { it.month == NOVEMBER && it.dayOfMonth == 11 }
    val isVeteransDayMovedFromSundayToMonday: (LocalDate) -> Boolean = { it.month == NOVEMBER && it.dayOfMonth == 12 && it.dayOfWeek == MONDAY }
    val isVeteransDayMovedFromSaturdayToFriday: (LocalDate) -> Boolean = { it.month == NOVEMBER && it.dayOfMonth == 10 && it.dayOfWeek == FRIDAY }

    val isThanksgivingDay: (LocalDate) -> Boolean = { it.month == NOVEMBER && it.dayOfMonth >= 22 && it.dayOfMonth <= 28 && it.dayOfWeek == THURSDAY }

    val isChristmasDay: (LocalDate) -> Boolean = { it.month == DECEMBER && it.dayOfMonth == 25 }
    val isChristmasDayMovedFromSundayToMonday: (LocalDate) -> Boolean = { it.month == DECEMBER && it.dayOfMonth == 26 && it.dayOfWeek == MONDAY }
    val isChristmasDayMovedFromSaturdayToFriday: (LocalDate) -> Boolean = { it.month == DECEMBER && it.dayOfMonth == 24 && it.dayOfWeek == FRIDAY }

    // Australia Specific
    val isAustraliaDay: (LocalDate) -> Boolean = { it.month == JANUARY && it.dayOfMonth == 26 }
    val isAustraliaDayMovedFromWeekendToMonday: (LocalDate) -> Boolean = { it.month == JANUARY && (it.dayOfMonth == 27 || it.dayOfMonth == 28) && it.dayOfWeek == MONDAY }

    // ANZAC Day, April 25th (possibly moved to Monday)
    val isAnzacDay: (LocalDate) -> Boolean = { it.month == APRIL && it.dayOfMonth == 25 }
    val isAnzacDayMovedFromSundayToMonday: (LocalDate) -> Boolean = { it.month == APRIL && it.dayOfMonth == 26 && it.dayOfWeek == MONDAY }

    // Queen's Birthday, second Monday in June
    val isAustraliaQueenBirthday: (LocalDate) -> Boolean = { it.month == JUNE && it.dayOfMonth >= 8 && it.dayOfMonth <= 14 && it.dayOfWeek == MONDAY }

    // Bank Holiday, first Monday in August
    val isAustraliaBankHoliday: (LocalDate) -> Boolean = { it.month == AUGUST && it.dayOfMonth <= 7 && it.dayOfWeek == MONDAY }

    // Labour Day, first Monday in October
    val isAustraliaLabourDay: (LocalDate) -> Boolean = { it.month == OCTOBER && it.dayOfMonth <= 7 && it.dayOfWeek == MONDAY }

    // Christmas, December 25th (possibly Monday or Tuesday)
    val isChristmasDayMovedFromWeekendToMonday: (LocalDate) -> Boolean = {
      it.month == DECEMBER && it.dayOfWeek == MONDAY && (it.dayOfMonth == 26 || it.dayOfMonth == 27)
    }

    val isBoxingDay: (LocalDate) -> Boolean = { it.month == DECEMBER && it.dayOfMonth == 26 }
    val isBoxingDayMovedFromSaturdayToMonday: (LocalDate) -> Boolean = {
      it.month == DECEMBER && it.dayOfWeek == MONDAY && it.dayOfMonth == 28
    }
    val isBoxingDayMovedFromSundayOrMondayToTuesday: (LocalDate) -> Boolean = {
      it.month == DECEMBER && it.dayOfWeek == TUESDAY && (it.dayOfMonth == 27 || it.dayOfMonth == 28)
    }

    val isDayOfGoodwillOnOrAfterYear2000: (LocalDate) -> Boolean = { it.year >= 2000 && it.month == DECEMBER && it.dayOfMonth == 26 }
    val isDecember31On1998or1999or2001: (LocalDate) -> Boolean = { (it.year == 1998 || it.year == 1999 || it.year == 2001 ) && it.month == DECEMBER && it.dayOfMonth == 31 }

    val isWesternEasterMonday: (LocalDate) -> Boolean = { it in westernEasterMondays }
    val isWesternGoodFriday: (LocalDate) -> Boolean = { it in westernGoodFridays }

    val isWesternEasterMondayOnOrAfter2000: (LocalDate) -> Boolean = { it.year >= 2000 && it in westernEasterMondays }
    val isWesternGoodFridayOnOrAfter2000: (LocalDate) -> Boolean = { it.year >= 2000 && it in westernGoodFridays }

    val isOrthodoxEasterMonday: (LocalDate) -> Boolean = { it in orthodoxEasterMondays }
    val isOrthodoxGoodFriday: (LocalDate) -> Boolean = { it in orthodoxGoodFridays }

    val westernEasterMondays = setOf(
        LocalDate.of(1901, 4, 8), LocalDate.of(1902, 3, 31), LocalDate.of(1903, 4, 13), LocalDate.of(1904, 4, 4), LocalDate.of(1905, 4, 24), LocalDate.of(1906, 4, 16), LocalDate.of(1907, 4, 1), LocalDate.of(1908, 4, 20), LocalDate.of(1909, 4, 12),
        LocalDate.of(1910, 3, 28), LocalDate.of(1911, 4, 17), LocalDate.of(1912, 4, 8), LocalDate.of(1913, 3, 24), LocalDate.of(1914, 4, 13), LocalDate.of(1915, 4, 5), LocalDate.of(1916, 4, 24), LocalDate.of(1917, 4, 9), LocalDate.of(1918, 4, 1), LocalDate.of(1919, 4, 21),
        LocalDate.of(1920, 4, 5), LocalDate.of(1921, 3, 28), LocalDate.of(1922, 4, 17), LocalDate.of(1923, 4, 2), LocalDate.of(1924, 4, 21), LocalDate.of(1925, 4, 13), LocalDate.of(1926, 4, 5), LocalDate.of(1927, 4, 18), LocalDate.of(1928, 4, 9), LocalDate.of(1929, 4, 1),
        LocalDate.of(1930, 4, 21), LocalDate.of(1931, 4, 6), LocalDate.of(1932, 3, 28), LocalDate.of(1933, 4, 17), LocalDate.of(1934, 4, 2), LocalDate.of(1935, 4, 22), LocalDate.of(1936, 4, 13), LocalDate.of(1937, 3, 29), LocalDate.of(1938, 4, 18), LocalDate.of(1939, 4, 10),
        LocalDate.of(1940, 3, 25), LocalDate.of(1941, 4, 14), LocalDate.of(1942, 4, 6), LocalDate.of(1943, 4, 26), LocalDate.of(1944, 4, 10), LocalDate.of(1945, 4, 2), LocalDate.of(1946, 4, 22), LocalDate.of(1947, 4, 7), LocalDate.of(1948, 3, 29), LocalDate.of(1949, 4, 18),
        LocalDate.of(1950, 4, 10), LocalDate.of(1951, 3, 26), LocalDate.of(1952, 4, 14), LocalDate.of(1953, 4, 6), LocalDate.of(1954, 4, 19), LocalDate.of(1955, 4, 11), LocalDate.of(1956, 4, 2), LocalDate.of(1957, 4, 22), LocalDate.of(1958, 4, 7), LocalDate.of(1959, 3, 30),
        LocalDate.of(1960, 4, 18), LocalDate.of(1961, 4, 3), LocalDate.of(1962, 4, 23), LocalDate.of(1963, 4, 15), LocalDate.of(1964, 3, 30), LocalDate.of(1965, 4, 19), LocalDate.of(1966, 4, 11), LocalDate.of(1967, 3, 27), LocalDate.of(1968, 4, 15), LocalDate.of(1969, 4, 7),
        LocalDate.of(1970, 3, 30), LocalDate.of(1971, 4, 12), LocalDate.of(1972, 4, 3), LocalDate.of(1973, 4, 23), LocalDate.of(1974, 4, 15), LocalDate.of(1975, 3, 31), LocalDate.of(1976, 4, 19), LocalDate.of(1977, 4, 11), LocalDate.of(1978, 3, 27), LocalDate.of(1979, 4, 16),
        LocalDate.of(1980, 4, 7), LocalDate.of(1981, 4, 20), LocalDate.of(1982, 4, 12), LocalDate.of(1983, 4, 4), LocalDate.of(1984, 4, 23), LocalDate.of(1985, 4, 8), LocalDate.of(1986, 3, 31), LocalDate.of(1987, 4, 20), LocalDate.of(1988, 4, 4), LocalDate.of(1989, 3, 27),
        LocalDate.of(1990, 4, 16), LocalDate.of(1991, 4, 1), LocalDate.of(1992, 4, 20), LocalDate.of(1993, 4, 12), LocalDate.of(1994, 4, 4), LocalDate.of(1995, 4, 17), LocalDate.of(1996, 4, 8), LocalDate.of(1997, 3, 31), LocalDate.of(1998, 4, 13), LocalDate.of(1999, 4, 5),
        LocalDate.of(2000, 4, 24), LocalDate.of(2001, 4, 16), LocalDate.of(2002, 4, 1), LocalDate.of(2003, 4, 21), LocalDate.of(2004, 4, 12), LocalDate.of(2005, 3, 28), LocalDate.of(2006, 4, 17), LocalDate.of(2007, 4, 9), LocalDate.of(2008, 3, 24), LocalDate.of(2009, 4, 13),
        LocalDate.of(2010, 4, 5), LocalDate.of(2011, 4, 25), LocalDate.of(2012, 4, 9), LocalDate.of(2013, 4, 1), LocalDate.of(2014, 4, 21), LocalDate.of(2015, 4, 6), LocalDate.of(2016, 3, 28), LocalDate.of(2017, 4, 17), LocalDate.of(2018, 4, 2), LocalDate.of(2019, 4, 22),
        LocalDate.of(2020, 4, 13), LocalDate.of(2021, 4, 5), LocalDate.of(2022, 4, 18), LocalDate.of(2023, 4, 10), LocalDate.of(2024, 4, 1), LocalDate.of(2025, 4, 21), LocalDate.of(2026, 4, 6), LocalDate.of(2027, 3, 29), LocalDate.of(2028, 4, 17), LocalDate.of(2029, 4, 2),
        LocalDate.of(2030, 4, 22), LocalDate.of(2031, 4, 14), LocalDate.of(2032, 3, 29), LocalDate.of(2033, 4, 18), LocalDate.of(2034, 4, 10), LocalDate.of(2035, 3, 26), LocalDate.of(2036, 4, 14), LocalDate.of(2037, 4, 6), LocalDate.of(2038, 4, 26), LocalDate.of(2039, 4, 11),
        LocalDate.of(2040, 4, 2), LocalDate.of(2041, 4, 22), LocalDate.of(2042, 4, 7), LocalDate.of(2043, 3, 30), LocalDate.of(2044, 4, 18), LocalDate.of(2045, 4, 10), LocalDate.of(2046, 3, 26), LocalDate.of(2047, 4, 15), LocalDate.of(2048, 4, 6), LocalDate.of(2049, 4, 19),
        LocalDate.of(2050, 4, 11), LocalDate.of(2051, 4, 3), LocalDate.of(2052, 4, 22), LocalDate.of(2053, 4, 7), LocalDate.of(2054, 3, 30), LocalDate.of(2055, 4, 19), LocalDate.of(2056, 4, 3), LocalDate.of(2057, 4, 23), LocalDate.of(2058, 4, 15), LocalDate.of(2059, 3, 31),
        LocalDate.of(2060, 4, 19), LocalDate.of(2061, 4, 11), LocalDate.of(2062, 3, 27), LocalDate.of(2063, 4, 16), LocalDate.of(2064, 4, 7), LocalDate.of(2065, 3, 30), LocalDate.of(2066, 4, 12), LocalDate.of(2067, 4, 4), LocalDate.of(2068, 4, 23), LocalDate.of(2069, 4, 15),
        LocalDate.of(2070, 3, 31), LocalDate.of(2071, 4, 20), LocalDate.of(2072, 4, 11), LocalDate.of(2073, 3, 27), LocalDate.of(2074, 4, 16), LocalDate.of(2075, 4, 8), LocalDate.of(2076, 4, 20), LocalDate.of(2077, 4, 12), LocalDate.of(2078, 4, 4), LocalDate.of(2079, 4, 24),
        LocalDate.of(2080, 4, 8), LocalDate.of(2081, 3, 31), LocalDate.of(2082, 4, 20), LocalDate.of(2083, 4, 5), LocalDate.of(2084, 3, 27), LocalDate.of(2085, 4, 16), LocalDate.of(2086, 4, 1), LocalDate.of(2087, 4, 21), LocalDate.of(2088, 4, 12), LocalDate.of(2089, 4, 4),
        LocalDate.of(2090, 4, 17), LocalDate.of(2091, 4, 9), LocalDate.of(2092, 3, 31), LocalDate.of(2093, 4, 13), LocalDate.of(2094, 4, 5), LocalDate.of(2095, 4, 25), LocalDate.of(2096, 4, 16), LocalDate.of(2097, 4, 1), LocalDate.of(2098, 4, 21), LocalDate.of(2099, 4, 13),
        LocalDate.of(2100, 3, 29), LocalDate.of(2101, 4, 18), LocalDate.of(2102, 4, 10), LocalDate.of(2103, 3, 26), LocalDate.of(2104, 4, 14), LocalDate.of(2105, 4, 6), LocalDate.of(2106, 4, 19), LocalDate.of(2107, 4, 11), LocalDate.of(2108, 4, 2), LocalDate.of(2109, 4, 22),
        LocalDate.of(2110, 4, 7), LocalDate.of(2111, 3, 30), LocalDate.of(2112, 4, 18), LocalDate.of(2113, 4, 3), LocalDate.of(2114, 4, 23), LocalDate.of(2115, 4, 15), LocalDate.of(2116, 3, 30), LocalDate.of(2117, 4, 19), LocalDate.of(2118, 4, 11), LocalDate.of(2119, 3, 27),
        LocalDate.of(2120, 4, 15), LocalDate.of(2121, 4, 7), LocalDate.of(2122, 3, 30), LocalDate.of(2123, 4, 12), LocalDate.of(2124, 4, 3), LocalDate.of(2125, 4, 23), LocalDate.of(2126, 4, 15), LocalDate.of(2127, 3, 31), LocalDate.of(2128, 4, 19), LocalDate.of(2129, 4, 11),
        LocalDate.of(2130, 3, 27), LocalDate.of(2131, 4, 16), LocalDate.of(2132, 4, 7), LocalDate.of(2133, 4, 20), LocalDate.of(2134, 4, 12), LocalDate.of(2135, 4, 4), LocalDate.of(2136, 4, 23), LocalDate.of(2137, 4, 8), LocalDate.of(2138, 3, 31), LocalDate.of(2139, 4, 20),
        LocalDate.of(2140, 4, 4), LocalDate.of(2141, 3, 27), LocalDate.of(2142, 4, 16), LocalDate.of(2143, 4, 1), LocalDate.of(2144, 4, 20), LocalDate.of(2145, 4, 12), LocalDate.of(2146, 4, 4), LocalDate.of(2147, 4, 17), LocalDate.of(2148, 4, 8), LocalDate.of(2149, 3, 31),
        LocalDate.of(2150, 4, 13), LocalDate.of(2151, 4, 5), LocalDate.of(2152, 4, 24), LocalDate.of(2153, 4, 16), LocalDate.of(2154, 4, 1), LocalDate.of(2155, 4, 21), LocalDate.of(2156, 4, 12), LocalDate.of(2157, 3, 28), LocalDate.of(2158, 4, 17), LocalDate.of(2159, 4, 9),
        LocalDate.of(2160, 3, 24), LocalDate.of(2161, 4, 13), LocalDate.of(2162, 4, 5), LocalDate.of(2163, 4, 25), LocalDate.of(2164, 4, 9), LocalDate.of(2165, 4, 1), LocalDate.of(2166, 4, 21), LocalDate.of(2167, 4, 6), LocalDate.of(2168, 3, 28), LocalDate.of(2169, 4, 17),
        LocalDate.of(2170, 4, 2), LocalDate.of(2171, 4, 22), LocalDate.of(2172, 4, 13), LocalDate.of(2173, 4, 5), LocalDate.of(2174, 4, 18), LocalDate.of(2175, 4, 10), LocalDate.of(2176, 4, 1), LocalDate.of(2177, 4, 21), LocalDate.of(2178, 4, 6), LocalDate.of(2179, 3, 29),
        LocalDate.of(2180, 4, 17), LocalDate.of(2181, 4, 2), LocalDate.of(2182, 4, 22), LocalDate.of(2183, 4, 14), LocalDate.of(2184, 3, 29), LocalDate.of(2185, 4, 18), LocalDate.of(2186, 4, 10), LocalDate.of(2187, 3, 26), LocalDate.of(2188, 4, 14), LocalDate.of(2189, 4, 6),
        LocalDate.of(2190, 4, 26), LocalDate.of(2191, 4, 11), LocalDate.of(2192, 4, 2), LocalDate.of(2193, 4, 22), LocalDate.of(2194, 4, 7), LocalDate.of(2195, 3, 30), LocalDate.of(2196, 4, 18), LocalDate.of(2197, 4, 10), LocalDate.of(2198, 3, 26), LocalDate.of(2199, 4, 15)
    )

    val westernGoodFridays = westernEasterMondays.map { it.minusDays(3) }

    val orthodoxEasterMondays = setOf(
        LocalDate.of(1901, 4, 15), LocalDate.of(1902, 4, 28), LocalDate.of(1903, 4, 20), LocalDate.of(1904, 4, 11), LocalDate.of(1905, 5, 1), LocalDate.of(1906, 4, 16), LocalDate.of(1907, 5, 6), LocalDate.of(1908, 4, 27), LocalDate.of(1909, 4, 12),
        LocalDate.of(1910, 5, 2), LocalDate.of(1911, 4, 24), LocalDate.of(1912, 4, 8), LocalDate.of(1913, 4, 28), LocalDate.of(1914, 4, 20), LocalDate.of(1915, 4, 5), LocalDate.of(1916, 4, 24), LocalDate.of(1917, 4, 16), LocalDate.of(1918, 5, 6), LocalDate.of(1919, 4, 21),
        LocalDate.of(1920, 4, 12), LocalDate.of(1921, 5, 2), LocalDate.of(1922, 4, 17), LocalDate.of(1923, 4, 9), LocalDate.of(1924, 4, 28), LocalDate.of(1925, 4, 20), LocalDate.of(1926, 5, 3), LocalDate.of(1927, 4, 25), LocalDate.of(1928, 4, 16), LocalDate.of(1929, 5, 6),
        LocalDate.of(1930, 4, 21), LocalDate.of(1931, 4, 13), LocalDate.of(1932, 5, 2), LocalDate.of(1933, 4, 17), LocalDate.of(1934, 4, 9), LocalDate.of(1935, 4, 29), LocalDate.of(1936, 4, 13), LocalDate.of(1937, 5, 3), LocalDate.of(1938, 4, 25), LocalDate.of(1939, 4, 10),
        LocalDate.of(1940, 4, 29), LocalDate.of(1941, 4, 21), LocalDate.of(1942, 4, 6), LocalDate.of(1943, 4, 26), LocalDate.of(1944, 4, 17), LocalDate.of(1945, 5, 7), LocalDate.of(1946, 4, 22), LocalDate.of(1947, 4, 14), LocalDate.of(1948, 5, 3), LocalDate.of(1949, 4, 25),
        LocalDate.of(1950, 4, 10), LocalDate.of(1951, 4, 30), LocalDate.of(1952, 4, 21), LocalDate.of(1953, 4, 6), LocalDate.of(1954, 4, 26), LocalDate.of(1955, 4, 18), LocalDate.of(1956, 5, 7), LocalDate.of(1957, 4, 22), LocalDate.of(1958, 4, 14), LocalDate.of(1959, 5, 4),
        LocalDate.of(1960, 4, 18), LocalDate.of(1961, 4, 10), LocalDate.of(1962, 4, 30), LocalDate.of(1963, 4, 15), LocalDate.of(1964, 5, 4), LocalDate.of(1965, 4, 26), LocalDate.of(1966, 4, 11), LocalDate.of(1967, 5, 1), LocalDate.of(1968, 4, 22), LocalDate.of(1969, 4, 14),
        LocalDate.of(1970, 4, 27), LocalDate.of(1971, 4, 19), LocalDate.of(1972, 4, 10), LocalDate.of(1973, 4, 30), LocalDate.of(1974, 4, 15), LocalDate.of(1975, 5, 5), LocalDate.of(1976, 4, 26), LocalDate.of(1977, 4, 11), LocalDate.of(1978, 5, 1), LocalDate.of(1979, 4, 23),
        LocalDate.of(1980, 4, 7), LocalDate.of(1981, 4, 27), LocalDate.of(1982, 4, 19), LocalDate.of(1983, 5, 9), LocalDate.of(1984, 4, 23), LocalDate.of(1985, 4, 15), LocalDate.of(1986, 5, 5), LocalDate.of(1987, 4, 20), LocalDate.of(1988, 4, 11), LocalDate.of(1989, 5, 1),
        LocalDate.of(1990, 4, 16), LocalDate.of(1991, 4, 8), LocalDate.of(1992, 4, 27), LocalDate.of(1993, 4, 19), LocalDate.of(1994, 5, 2), LocalDate.of(1995, 4, 24), LocalDate.of(1996, 4, 15), LocalDate.of(1997, 4, 28), LocalDate.of(1998, 4, 20), LocalDate.of(1999, 4, 12),
        LocalDate.of(2000, 5, 1), LocalDate.of(2001, 4, 16), LocalDate.of(2002, 5, 6), LocalDate.of(2003, 4, 28), LocalDate.of(2004, 4, 12), LocalDate.of(2005, 5, 2), LocalDate.of(2006, 4, 24), LocalDate.of(2007, 4, 9), LocalDate.of(2008, 4, 28), LocalDate.of(2009, 4, 20),
        LocalDate.of(2010, 4, 5), LocalDate.of(2011, 4, 25), LocalDate.of(2012, 4, 16), LocalDate.of(2013, 5, 6), LocalDate.of(2014, 4, 21), LocalDate.of(2015, 4, 13), LocalDate.of(2016, 5, 2), LocalDate.of(2017, 4, 17), LocalDate.of(2018, 4, 9), LocalDate.of(2019, 4, 29),
        LocalDate.of(2020, 4, 20), LocalDate.of(2021, 5, 3), LocalDate.of(2022, 4, 25), LocalDate.of(2023, 4, 17), LocalDate.of(2024, 5, 6), LocalDate.of(2025, 4, 21), LocalDate.of(2026, 4, 13), LocalDate.of(2027, 5, 3), LocalDate.of(2028, 4, 17), LocalDate.of(2029, 4, 9),
        LocalDate.of(2030, 4, 29), LocalDate.of(2031, 4, 14), LocalDate.of(2032, 5, 3), LocalDate.of(2033, 4, 25), LocalDate.of(2034, 4, 10), LocalDate.of(2035, 4, 30), LocalDate.of(2036, 4, 21), LocalDate.of(2037, 4, 6), LocalDate.of(2038, 4, 26), LocalDate.of(2039, 4, 18),
        LocalDate.of(2040, 5, 7), LocalDate.of(2041, 4, 22), LocalDate.of(2042, 4, 14), LocalDate.of(2043, 5, 4), LocalDate.of(2044, 4, 25), LocalDate.of(2045, 4, 10), LocalDate.of(2046, 4, 30), LocalDate.of(2047, 4, 22), LocalDate.of(2048, 4, 6), LocalDate.of(2049, 4, 26),
        LocalDate.of(2050, 4, 18), LocalDate.of(2051, 5, 8), LocalDate.of(2052, 4, 22), LocalDate.of(2053, 4, 14), LocalDate.of(2054, 5, 4), LocalDate.of(2055, 4, 19), LocalDate.of(2056, 4, 10), LocalDate.of(2057, 4, 30), LocalDate.of(2058, 4, 15), LocalDate.of(2059, 5, 5),
        LocalDate.of(2060, 4, 26), LocalDate.of(2061, 4, 11), LocalDate.of(2062, 5, 1), LocalDate.of(2063, 4, 23), LocalDate.of(2064, 4, 14), LocalDate.of(2065, 4, 27), LocalDate.of(2066, 4, 19), LocalDate.of(2067, 4, 11), LocalDate.of(2068, 4, 30), LocalDate.of(2069, 4, 15),
        LocalDate.of(2070, 5, 5), LocalDate.of(2071, 4, 20), LocalDate.of(2072, 4, 11), LocalDate.of(2073, 5, 1), LocalDate.of(2074, 4, 23), LocalDate.of(2075, 4, 8), LocalDate.of(2076, 4, 27), LocalDate.of(2077, 4, 19), LocalDate.of(2078, 5, 9), LocalDate.of(2079, 4, 24),
        LocalDate.of(2080, 4, 15), LocalDate.of(2081, 5, 5), LocalDate.of(2082, 4, 20), LocalDate.of(2083, 4, 12), LocalDate.of(2084, 5, 1), LocalDate.of(2085, 4, 16), LocalDate.of(2086, 4, 8), LocalDate.of(2087, 4, 28), LocalDate.of(2088, 4, 19), LocalDate.of(2089, 5, 2),
        LocalDate.of(2090, 4, 24), LocalDate.of(2091, 4, 9), LocalDate.of(2092, 4, 28), LocalDate.of(2093, 4, 20), LocalDate.of(2094, 4, 12), LocalDate.of(2095, 4, 25), LocalDate.of(2096, 4, 16), LocalDate.of(2097, 5, 6), LocalDate.of(2098, 4, 28), LocalDate.of(2099, 4, 13),
        LocalDate.of(2100, 5, 3), LocalDate.of(2101, 4, 25), LocalDate.of(2102, 4, 10), LocalDate.of(2103, 4, 30), LocalDate.of(2104, 4, 21), LocalDate.of(2105, 4, 6), LocalDate.of(2106, 4, 26), LocalDate.of(2107, 4, 18), LocalDate.of(2108, 5, 7), LocalDate.of(2109, 4, 22),
        LocalDate.of(2110, 4, 14), LocalDate.of(2111, 5, 4), LocalDate.of(2112, 4, 18), LocalDate.of(2113, 4, 10), LocalDate.of(2114, 4, 30), LocalDate.of(2115, 4, 15), LocalDate.of(2116, 5, 4), LocalDate.of(2117, 4, 26), LocalDate.of(2118, 4, 18), LocalDate.of(2119, 5, 1),
        LocalDate.of(2120, 4, 22), LocalDate.of(2121, 4, 14), LocalDate.of(2122, 5, 4), LocalDate.of(2123, 4, 19), LocalDate.of(2124, 4, 10), LocalDate.of(2125, 4, 30), LocalDate.of(2126, 4, 15), LocalDate.of(2127, 5, 5), LocalDate.of(2128, 4, 26), LocalDate.of(2129, 4, 11),
        LocalDate.of(2130, 5, 1), LocalDate.of(2131, 4, 23), LocalDate.of(2132, 4, 7), LocalDate.of(2133, 4, 27), LocalDate.of(2134, 4, 19), LocalDate.of(2135, 5, 9), LocalDate.of(2136, 4, 23), LocalDate.of(2137, 4, 15), LocalDate.of(2138, 5, 5), LocalDate.of(2139, 4, 20),
        LocalDate.of(2140, 4, 11), LocalDate.of(2141, 5, 1), LocalDate.of(2142, 4, 23), LocalDate.of(2143, 4, 8), LocalDate.of(2144, 4, 27), LocalDate.of(2145, 4, 19), LocalDate.of(2146, 5, 9), LocalDate.of(2147, 4, 24), LocalDate.of(2148, 4, 15), LocalDate.of(2149, 5, 5),
        LocalDate.of(2150, 4, 20), LocalDate.of(2151, 4, 12), LocalDate.of(2152, 5, 1), LocalDate.of(2153, 4, 16), LocalDate.of(2154, 5, 6), LocalDate.of(2155, 4, 28), LocalDate.of(2156, 4, 12), LocalDate.of(2157, 5, 2), LocalDate.of(2158, 4, 24), LocalDate.of(2159, 4, 9),
        LocalDate.of(2160, 4, 28), LocalDate.of(2161, 4, 20), LocalDate.of(2162, 4, 12), LocalDate.of(2163, 4, 25), LocalDate.of(2164, 4, 16), LocalDate.of(2165, 5, 6), LocalDate.of(2166, 4, 21), LocalDate.of(2167, 4, 13), LocalDate.of(2168, 5, 2), LocalDate.of(2169, 4, 24),
        LocalDate.of(2170, 4, 9), LocalDate.of(2171, 4, 29), LocalDate.of(2172, 4, 20), LocalDate.of(2173, 5, 10), LocalDate.of(2174, 4, 25), LocalDate.of(2175, 4, 17), LocalDate.of(2176, 5, 6), LocalDate.of(2177, 4, 21), LocalDate.of(2178, 4, 13), LocalDate.of(2179, 5, 3),
        LocalDate.of(2180, 4, 17), LocalDate.of(2181, 4, 9), LocalDate.of(2182, 4, 29), LocalDate.of(2183, 4, 14), LocalDate.of(2184, 5, 3), LocalDate.of(2185, 4, 25), LocalDate.of(2186, 4, 10), LocalDate.of(2187, 4, 30), LocalDate.of(2188, 4, 21), LocalDate.of(2189, 4, 13),
        LocalDate.of(2190, 4, 26), LocalDate.of(2191, 4, 18), LocalDate.of(2192, 5, 7), LocalDate.of(2193, 4, 29), LocalDate.of(2194, 4, 14), LocalDate.of(2195, 5, 4), LocalDate.of(2196, 4, 25), LocalDate.of(2197, 4, 10), LocalDate.of(2198, 4, 30), LocalDate.of(2199, 4, 22)
    )

    val orthodoxGoodFridays = orthodoxEasterMondays.map { it.minusDays(3) }

  }

}

object CalendarInternalAustralia : CalendarInternal(
    isWeekendFun = isSaturdayOrSunday,
    isHolidayFuns = listOf(
        isJanuaryFirst,
        isAustraliaDay,
        isAustraliaDayMovedFromWeekendToMonday,
        isAnzacDay,
        isAnzacDayMovedFromSundayToMonday,
        isWesternEasterMonday,
        isWesternGoodFriday,
        isAustraliaQueenBirthday,
        isAustraliaBankHoliday,
        isAustraliaLabourDay,
        isChristmasDay,
        isChristmasDayMovedFromWeekendToMonday,
        isBoxingDay,
        isBoxingDayMovedFromSaturdayToMonday,
        isBoxingDayMovedFromSundayOrMondayToTuesday
    )
)

object CalendarInternalUnitedStatesSettlement : CalendarInternal(
    isWeekendFun = isSaturdayOrSunday,
    isHolidayFuns = listOf(
        isJanuaryFirst,
        isJanuaryFirstMovedFromSundayToMonday,
        isJanuaryFirstMovedFromSaturdayToFriday,
        isMartinLutherKingsBirthday,
        isWashingtonsBirthday,
        isMemorialDay,
        isIndependenceDay,
        isIndependenceDayMovedFromSundayToMonday,
        isIndependenceDayMovedFromSaturdayToFriday,
        isLaborDay,
        isColumbusDay,
        isVeteransDay,
        isVeteransDayMovedFromSundayToMonday,
        isVeteransDayMovedFromSaturdayToFriday,
        isThanksgivingDay,
        isChristmasDay,
        isChristmasDayMovedFromSundayToMonday,
        isChristmasDayMovedFromSaturdayToFriday
    )
)

object CalendarInternalTarget : CalendarInternal(
    isWeekendFun = isSaturdayOrSunday,
    isHolidayFuns = listOf(
        isJanuaryFirst,
        isWesternEasterMondayOnOrAfter2000,
        isWesternGoodFridayOnOrAfter2000,
        isLaborDayOnOrAfterYear2000,
        isChristmasDay,
        isDayOfGoodwillOnOrAfterYear2000,
        isDecember31On1998or1999or2001
    )
)
