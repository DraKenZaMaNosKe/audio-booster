package com.pixora.volumemax

import org.junit.Assert.assertEquals
import org.junit.Test

class GainMathTest {
    @Test fun zeroDbIsOneHundredPercent() = assertEquals(100, GainMath.relativePercent(0))
    @Test fun threeDbIsAboutOneHundredFortyOnePercent() = assertEquals(141, GainMath.relativePercent(3))
    @Test fun sixDbIsAboutTwoHundredPercent() = assertEquals(200, GainMath.relativePercent(6))
    @Test fun fractionalGainKeepsRequestedPreset() = assertEquals(150, GainMath.relativePercent(3.5218f))
    @Test fun negativeGainIsClamped() = assertEquals(100, GainMath.relativePercent(-4))
}
