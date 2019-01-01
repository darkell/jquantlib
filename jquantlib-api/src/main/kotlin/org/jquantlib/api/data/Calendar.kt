package org.jquantlib.api.data

sealed class Calendar{
  override fun toString(): String {
    return javaClass.simpleName
  }
}

object Australia: Calendar()

typealias UnitedStates = UnitedStatesSettlement
object UnitedStatesSettlement: Calendar()
object UnitedStatesNyse: Calendar()
object UnitedStatesGovernmentBond: Calendar()
object UnitedStatesNerc: Calendar()

object Target: Calendar()

