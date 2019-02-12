/*
 Copyright (C) 2019 David Arkell

 This source code is release under the BSD License.

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.dayCounter

import org.jquantlib.DataLoader.assertEqualsAny
import org.jquantlib.DataLoader.assertEqualsDouble
import org.jquantlib.calendar.CalendarServiceImpl
import org.junit.Test

class DayCounterServiceTest {

  private val dayCounterService = DayCounterServiceImpl(
      calendarService = CalendarServiceImpl()
  )

  @Test
  fun dayCount() {
    assertEqualsAny("DayCounter_dayCount.jso", ListDayCountTypeReference) {
      dayCounterService.dayCount(it.dayCounter, it.start, it.end)
    }
  }

  @Test
  fun yearFraction() {
    assertEqualsDouble("DayCounter_yearFraction.jso", ListYearFractionParamsTypeReference) {
      dayCounterService.yearFraction(it.dayCounter, it.start, it.end)
    }
  }

}