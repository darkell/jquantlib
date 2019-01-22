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
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.*
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.*
import com.fasterxml.jackson.annotation.JsonTypeName
import org.jquantlib.api.data.*

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes(value = [
  (JsonSubTypes.Type(value = PlainVanillaPayoff::class, name = "PlainVanillaPayoff")),
  (JsonSubTypes.Type(value = CashOrNothingPayoff::class, name = "CashOrNothingPayoff")),
  (JsonSubTypes.Type(value = AssetOrNothingPayoff::class, name = "AssetOrNothingPayoff")),
  (JsonSubTypes.Type(value = GapPayoff::class, name = "GapPayoff"))
])
interface PayoffMixIn

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes(value = [
  (JsonSubTypes.Type(value = PlainVanillaPayoff::class, name = "PlainVanillaPayoff")),
  (JsonSubTypes.Type(value = CashOrNothingPayoff::class, name = "CashOrNothingPayoff")),
  (JsonSubTypes.Type(value = AssetOrNothingPayoff::class, name = "AssetOrNothingPayoff")),
  (JsonSubTypes.Type(value = GapPayoff::class, name = "GapPayoff"))
])
interface TypePayoffMixIn

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes(value = [
  (JsonSubTypes.Type(value = PlainVanillaPayoff::class, name = "PlainVanillaPayoff")),
  (JsonSubTypes.Type(value = CashOrNothingPayoff::class, name = "CashOrNothingPayoff")),
  (JsonSubTypes.Type(value = AssetOrNothingPayoff::class, name = "AssetOrNothingPayoff")),
  (JsonSubTypes.Type(value = GapPayoff::class, name = "GapPayoff"))
])
interface StrikedTypePayoffMixin

@JsonTypeName("PlainVanillaPayoff")
interface PlainVanillaPayoffMixIn

@JsonTypeName("CashOrNothingPayoff")
interface CashOrNothingPayoffMixIn

@JsonTypeName("AssetOrNothingPayoff")
interface AssetOrNothingPayoffMixIn

@JsonTypeName("GapPayoff")
interface GapPayoffMixIn
