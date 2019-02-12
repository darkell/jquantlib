package org.jquantlib.dayCounter

import com.fasterxml.jackson.core.type.TypeReference
import org.jquantlib.ExpectedAsAny
import org.jquantlib.ExpectedAsDouble
import org.jquantlib.api.data.DayCounter
import java.time.LocalDate

object ListDayCountTypeReference : TypeReference<List<DayCountParams>>()

data class DayCountParams(
    val dayCounter: DayCounter,
    val start: LocalDate,
    val end: LocalDate,
    override val expected: Long
): ExpectedAsAny<Long>

object ListYearFractionParamsTypeReference : TypeReference<List<YearFractionParams>>()

data class YearFractionParams(
    val dayCounter: DayCounter,
    val start: LocalDate,
    val end: LocalDate,
    override val expected: Double
): ExpectedAsDouble

