package com.eziosoft.simplecompassnetguru.utils

import org.junit.Test

import org.junit.Assert.*

class UtilsKtTest {

    @Test
    fun `validateCoordinate returns false`() {
        var sampleCoordinates = "0,"
        assertFalse(com.eziosoft.simplecompassnetguru.utils.validateCoordinates(sampleCoordinates))

        sampleCoordinates = "361,2"
        assertFalse(com.eziosoft.simplecompassnetguru.utils.validateCoordinates(sampleCoordinates))
    }

    @Test
    fun `validateCoordinate returns true`() {
        var sampleCoordinates = "0,0"
        assertTrue(com.eziosoft.simplecompassnetguru.utils.validateCoordinates(sampleCoordinates))

        sampleCoordinates = "360,2"
        assertFalse(com.eziosoft.simplecompassnetguru.utils.validateCoordinates(sampleCoordinates))
    }
}