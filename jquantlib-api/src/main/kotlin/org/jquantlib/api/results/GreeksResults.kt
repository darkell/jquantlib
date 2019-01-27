package org.jquantlib.api.results

data class GreeksResults(
    val delta: Double,
    val gamma: Double,
    val theta: Double,
    val vega: Double,
    val rho: Double,
    val dividendRho: Double
)