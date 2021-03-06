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

package org.jquantlib.api.jackson

import com.fasterxml.jackson.core.type.TypeReference
import org.jquantlib.api.data.*

object ListDayCounterTypeReference : TypeReference<List<DayCounter>>()
object ListCalendarTypeReference : TypeReference<List<Calendar>>()
object ListQuoteTypeReference : TypeReference<List<Quote>>()
object ListPayoffTypeReference : TypeReference<List<Payoff>>()
object ListTypePayoffTypeReference : TypeReference<List<TypePayoff>>()
object ListStrikedTypePayoffTypeReference : TypeReference<List<StrikedTypePayoff>>()
