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

package org.jquantlib.api.service

import org.jquantlib.api.data.BlackScholesMertonProcess
import org.jquantlib.api.data.EuropeanOption
import org.jquantlib.api.results.AnalyticEuropeanEngineResult
import org.jquantlib.api.results.OneAssetOptionResults
import java.time.LocalDate

interface AnalyticEuropeanEngineService {

  fun calculate(
      evaluationDate: LocalDate,
      europeanOption: EuropeanOption,
      bsmProcess: BlackScholesMertonProcess
  ): OneAssetOptionResults

}