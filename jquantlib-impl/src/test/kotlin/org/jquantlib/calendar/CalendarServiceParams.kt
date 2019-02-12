package org.jquantlib.calendar

import com.fasterxml.jackson.core.type.TypeReference
import org.jquantlib.DataLoader.assertEqualsAny
import org.jquantlib.ExpectedAsAny
import org.jquantlib.api.data.BusinessDayConvention
import org.jquantlib.api.data.Calendar
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object ListCalendarLocalDateBooleanParamsTypeReference : TypeReference<List<CalendarLocalDateBooleanParams>>()

data class CalendarLocalDateBooleanParams(
    val calendar: Calendar,
    val date: LocalDate,
    override val expected: Boolean
): ExpectedAsAny<Boolean>

fun assertEqualsCalendarLocalDateBooleanParams(filename: String, f: (CalendarLocalDateBooleanParams) -> Boolean) {
  assertEqualsAny(filename, ListCalendarLocalDateBooleanParamsTypeReference) {
    f(it)
  }
}

object ListCalendarDateDateParamsTypeReference : TypeReference<List<CalendarDateDateParams>>()

data class CalendarDateDateParams(
    val calendar: Calendar,
    val date: LocalDate,
    override val expected: LocalDate
): ExpectedAsAny<LocalDate>

fun assertEqualsCalendarDateDateParams(filename: String, f: (CalendarDateDateParams) -> LocalDate) {
  assertEqualsAny(filename, ListCalendarDateDateParamsTypeReference) {
    f(it)
  }
}

object ListCalendarDateBdcDateParamsTypeReference : TypeReference<List<CalendarDateBdcDateParams>>()

data class CalendarDateBdcDateParams(
    val calendar: Calendar,
    val date: LocalDate,
    val businessDayConvention: BusinessDayConvention,
    override val expected: LocalDate
): ExpectedAsAny<LocalDate>

fun assertEqualsCalendarDateBdcDateParams(filename: String, f: (CalendarDateBdcDateParams) -> LocalDate) {
  assertEqualsAny(filename, ListCalendarDateBdcDateParamsTypeReference) {
    f(it)
  }
}

object ListCalendarDateLongUnitBdcBooleanDateParamsTypeReference : TypeReference<List<CalendarDateLongUnitBdcBooleanDateParams>>()

data class CalendarDateLongUnitBdcBooleanDateParams(
    val calendar: Calendar,
    val date: LocalDate,
    val n: Long,
    val unit: ChronoUnit,
    val businessDayConvention: BusinessDayConvention,
    val endOfMonth: Boolean,
    override val expected: LocalDate
): ExpectedAsAny<LocalDate>

fun assertEqualsCalendarDateLongUnitBdcBooleanDateParams(filename: String, f: (CalendarDateLongUnitBdcBooleanDateParams) -> LocalDate) {
  assertEqualsAny(filename, ListCalendarDateLongUnitBdcBooleanDateParamsTypeReference) {
    f(it)
  }
}

object ListCalendarDateDateBooleanBooleanLongParamsTypeReference : TypeReference<List<CalendarDateDateBooleanBooleanLongParams>>()

data class CalendarDateDateBooleanBooleanLongParams(
    val calendar: Calendar,
    val from: LocalDate,
    val to: LocalDate,
    val includeFirst: Boolean,
    val includeLast: Boolean,
    override val expected: Long
): ExpectedAsAny<Long>

fun assertEqualsCalendarDateDateBooleanBooleanLongParams(filename: String, f: (CalendarDateDateBooleanBooleanLongParams) -> Long) {
  assertEqualsAny(filename, ListCalendarDateDateBooleanBooleanLongParamsTypeReference) {
    f(it)
  }
}

