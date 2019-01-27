package org.jquantlib.api.results

data class MoreGreeksResults(
    val itmCashProbability: Double,
    val deltaForward: Double,
    val elasticity: Double,
    val thetaPerDay: Double,
    val strikeSensitivity: Double
)