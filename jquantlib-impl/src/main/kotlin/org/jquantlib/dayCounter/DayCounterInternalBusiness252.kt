package org.jquantlib.dayCounter

import org.jquantlib.api.data.Calendar
import org.jquantlib.api.service.CalendarService
import java.time.LocalDate

class DayCounterInternalBusiness252(
    private val calendarService: CalendarService,
    private val calendar: Calendar
) : DayCounterInternal {

  override fun dayCount(start: LocalDate, end: LocalDate) =
      calendarService.businessDaysBetween(calendar, start, end)

  override fun yearFraction(start: LocalDate, end: LocalDate, refPeriodStart: LocalDate?, refPeriodEnd: LocalDate?) =
      dayCount(start, end) / 252.0

}