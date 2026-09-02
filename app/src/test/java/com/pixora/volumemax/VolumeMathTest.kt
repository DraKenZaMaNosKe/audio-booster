package com.pixora.volumemax

import org.junit.Assert.assertEquals
import org.junit.Test

class VolumeMathTest {
    @Test fun indexToPercent_handlesLimitsAndRounding() {
        assertEquals(0, VolumeMath.indexToPercent(5, 0))
        assertEquals(0, VolumeMath.indexToPercent(-2, 15))
        assertEquals(53, VolumeMath.indexToPercent(8, 15))
        assertEquals(100, VolumeMath.indexToPercent(99, 15))
    }

    @Test fun percentToIndex_handlesLimitsAndRounding() {
        assertEquals(0, VolumeMath.percentToIndex(50, 0))
        assertEquals(0, VolumeMath.percentToIndex(-5, 15))
        assertEquals(8, VolumeMath.percentToIndex(50, 15))
        assertEquals(15, VolumeMath.percentToIndex(150, 15))
    }
}
