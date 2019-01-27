package org.jquantlib.api.results

data class OneAssetOptionResults(
    /**
     * Represents the calculated value of an Instrument
     */
    val value: Double,

    /**
     * Contains the estimated error due to floating point error
     */
    val errorEstimate: Double,

    val greeks: GreeksResults,
    val moreGreeks: MoreGreeksResults
)