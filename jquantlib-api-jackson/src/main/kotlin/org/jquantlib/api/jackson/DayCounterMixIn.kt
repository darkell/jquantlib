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

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.jquantlib.api.data.*

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(value = [
  (JsonSubTypes.Type(value = Actual365Fixed::class, name = "Actual365Fixed")),
  (JsonSubTypes.Type(value = SimpleDayCounter::class, name = "SimpleDayCounter")),
  (JsonSubTypes.Type(value = Actual360::class, name = "Actual360")),
  (JsonSubTypes.Type(value = Thirty360USA::class, name = "Thirty360USA")),
  (JsonSubTypes.Type(value = Thirty360BondBasis::class, name = "Thirty360BondBasis")),
  (JsonSubTypes.Type(value = Thirty360European::class, name = "Thirty360European")),
  (JsonSubTypes.Type(value = Thirty360EurobondBasis::class, name = "Thirty360EurobondBasis")),
  (JsonSubTypes.Type(value = Thirty360Italian::class, name = "Thirty360Italian")),
  (JsonSubTypes.Type(value = ActualISMA::class, name = "ActualISMA")),
  (JsonSubTypes.Type(value = ActualBond::class, name = "ActualBond")),
  (JsonSubTypes.Type(value = ActualISDA::class, name = "ActualISDA")),
  (JsonSubTypes.Type(value = ActualHistorical::class, name = "ActualHistorical")),
  (JsonSubTypes.Type(value = Actual365::class, name = "Actual365")),
  (JsonSubTypes.Type(value = ActualAFB::class, name = "ActualAFB")),
  (JsonSubTypes.Type(value = ActualEuro::class, name = "ActualEuro")),
  (JsonSubTypes.Type(value = Business252::class, name = "Business252"))
])
interface DayCounterMixIn

@JsonTypeName("Actual365Fixed")
@JsonDeserialize(builder = Actual365FixedBuilder::class)
interface Actual365FixedMixIn

// This is necessary to ensure the deserialized object has the same identity
// It's a slight variation on what is in the answer to this ticket:
// https://github.com/FasterXML/jackson-module-kotlin/issues/147
class Actual365FixedBuilder {
  fun build() = Actual365Fixed
}

@JsonTypeName("SimpleDayCounter")
@JsonDeserialize(builder = SimpleDayCounterBuilder::class)
interface SimpleDayCounterMixIn

class SimpleDayCounterBuilder {
  fun build() = SimpleDayCounter
}

@JsonTypeName("Actual360")
@JsonDeserialize(builder = Actual360Builder::class)
interface Actual360MixIn

class Actual360Builder {
  fun build() = Actual360
}

@JsonTypeName("Thirty360USA")
@JsonDeserialize(builder = Thirty360USABuilder::class)
interface Thirty360USAMixIn

class Thirty360USABuilder {
  fun build() = Thirty360USA
}

@JsonTypeName("Thirty360BondBasis")
@JsonDeserialize(builder = Thirty360BondBasisBuilder::class)
interface Thirty360BondBasisMixIn

class Thirty360BondBasisBuilder {
  fun build() = Thirty360BondBasis
}

@JsonTypeName("Thirty360European")
@JsonDeserialize(builder = Thirty360EuropeanBuilder::class)
interface Thirty360EuropeanMixIn

class Thirty360EuropeanBuilder {
  fun build() = Thirty360European
}

@JsonTypeName("Thirty360EurobondBasis")
@JsonDeserialize(builder = Thirty360EurobondBasisBuilder::class)
interface Thirty360EurobondBasisMixIn

class Thirty360EurobondBasisBuilder {
  fun build() = Thirty360EurobondBasis
}

@JsonTypeName("Thirty360Italian")
@JsonDeserialize(builder = Thirty360ItalianBuilder::class)
interface Thirty360ItalianMixIn

class Thirty360ItalianBuilder {
  fun build() = Thirty360Italian
}

@JsonTypeName("ActualISMA")
@JsonDeserialize(builder = ActualISMABuilder::class)
interface ActualISMAMixIn

class ActualISMABuilder {
  fun build() = ActualISMA
}

@JsonTypeName("ActualBond")
@JsonDeserialize(builder = ActualBondBuilder::class)
interface ActualBondMixIn

class ActualBondBuilder {
  fun build() = ActualBond
}

@JsonTypeName("ActualISDA")
@JsonDeserialize(builder = ActualISDABuilder::class)
interface ActualISDAMixIn

class ActualISDABuilder {
  fun build() = ActualISDA
}

@JsonTypeName("ActualHistorical")
@JsonDeserialize(builder = ActualHistoricalBuilder::class)
interface ActualHistoricalMixIn

class ActualHistoricalBuilder {
  fun build() = ActualHistorical
}

@JsonTypeName("Actual365")
@JsonDeserialize(builder = Actual365Builder::class)
interface Actual365MixIn

class Actual365Builder {
  fun build() = Actual365
}

@JsonTypeName("ActualAFB")
@JsonDeserialize(builder = ActualAFBBuilder::class)
interface ActualAFBMixIn

class ActualAFBBuilder {
  fun build() = ActualAFB
}

@JsonTypeName("ActualEuro")
@JsonDeserialize(builder = ActualEuroBuilder::class)
interface ActualEuroMixIn

class ActualEuroBuilder {
  fun build() = ActualEuro
}

@JsonTypeName("Business252")
interface Business252MixIn
