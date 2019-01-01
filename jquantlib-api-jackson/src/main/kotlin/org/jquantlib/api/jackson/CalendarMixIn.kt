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
import org.jquantlib.api.data.Target

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(value = [
  (JsonSubTypes.Type(value = Australia::class, name = "Australia")),
  (JsonSubTypes.Type(value = UnitedStatesSettlement::class, name = "UnitedStatesSettlement")),
  (JsonSubTypes.Type(value = UnitedStatesNyse::class, name = "UnitedStatesNyse")),
  (JsonSubTypes.Type(value = UnitedStatesGovernmentBond::class, name = "UnitedStatesGovernmentBond")),
  (JsonSubTypes.Type(value = UnitedStatesNerc::class, name = "UnitedStatesNerc")),
  (JsonSubTypes.Type(value = Target::class, name = "Target"))
])
interface CalendarMixIn

@JsonTypeName("Australia")
@JsonDeserialize(builder = AustraliaBuilder::class)
interface AustraliaMixIn

// This is necessary to ensure the deserialized object has the same identity
// It's a slight variation on what is in the answer to this ticket:
// https://github.com/FasterXML/jackson-module-kotlin/issues/147
class AustraliaBuilder {
  fun build() = Australia
}

@JsonTypeName("UnitedStatesSettlement")
@JsonDeserialize(builder = UnitedStatesSettlementBuilder::class)
interface UnitedStatesSettlementMixIn

class UnitedStatesSettlementBuilder {
  fun build() = UnitedStatesSettlement
}

@JsonTypeName("UnitedStatesNyse")
@JsonDeserialize(builder = UnitedStatesNyseBuilder::class)
interface UnitedStatesNyseMixIn

class UnitedStatesNyseBuilder {
  fun build() = UnitedStatesNyse
}

@JsonTypeName("UnitedStatesGovernmentBond")
@JsonDeserialize(builder = UnitedStatesGovernmentBondBuilder::class)
interface UnitedStatesGovernmentBondMixIn

class UnitedStatesGovernmentBondBuilder {
  fun build() = UnitedStatesGovernmentBond
}

@JsonTypeName("UnitedStatesNerc")
@JsonDeserialize(builder = UnitedStatesNercBuilder::class)
interface UnitedStatesNercMixIn

class UnitedStatesNercBuilder {
  fun build() = UnitedStatesNerc
}

@JsonTypeName("Target")
@JsonDeserialize(builder = TargetBuilder::class)
interface TargetMixIn

class TargetBuilder {
  fun build() = Target
}
