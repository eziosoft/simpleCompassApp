package com.eziosoft.simplecompassnetguru

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun validateCoordinatesFunctionTest() {
        var sampleCoordinates = "0,0"
        assertTrue(com.eziosoft.simplecompassnetguru.utils.validateCoordinates(sampleCoordinates))

        sampleCoordinates = "0,"
        assertFalse(com.eziosoft.simplecompassnetguru.utils.validateCoordinates(sampleCoordinates))

        sampleCoordinates = "361,2"
        assertFalse(com.eziosoft.simplecompassnetguru.utils.validateCoordinates(sampleCoordinates))
    }
}