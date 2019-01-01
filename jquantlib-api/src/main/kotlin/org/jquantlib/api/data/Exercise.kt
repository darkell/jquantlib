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

package org.jquantlib.api.data

import java.time.LocalDate

sealed class Exercise

interface EarlyExercise {
  val payoffAtExpiry: Boolean
}

data class EuropeanExercise(
    val date: LocalDate
): Exercise()

data class BermudanExercise(
    val dates: List<LocalDate>,
    override val payoffAtExpiry: Boolean = false
): Exercise(), EarlyExercise

data class AmericanExercise(
    val earliestDate: LocalDate,
    val latestDate: LocalDate,
    override val payoffAtExpiry: Boolean = false
): Exercise(), EarlyExercise

fun Exercise.lastDate(): LocalDate {
  return when (this) {
    is EuropeanExercise -> date
    is BermudanExercise -> dates.last()
    is AmericanExercise -> latestDate
  }
}
