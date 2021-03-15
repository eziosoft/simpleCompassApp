/*
 * Created by Bartosz Szczygiel on 3/15/21 2:18 PM
 *  Copyright (c) 2021 . All rights reserved.
 *  Last modified 3/14/21 9:43 PM
 */

package com.eziosoft.simplecompassnetguru.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class UtilsKtTest {

    @Test
    fun `validateCoordinate returns false`() {
        var sampleCoordinates = "0,"
        assertThat(validateCoordinates(sampleCoordinates)).isFalse()

        sampleCoordinates = "0,190"
        assertThat(validateCoordinates(sampleCoordinates)).isFalse()

        sampleCoordinates = "0,-190"
        assertThat(validateCoordinates(sampleCoordinates)).isFalse()

        sampleCoordinates = "-100,0"
        assertThat(validateCoordinates(sampleCoordinates)).isFalse()

        sampleCoordinates = "100,0"
        assertThat(validateCoordinates(sampleCoordinates)).isFalse()
    }

    @Test
    fun `validateCoordinate returns true`() {
        var sampleCoordinates = "0,0"
        assertThat(validateCoordinates(sampleCoordinates)).isTrue()

        sampleCoordinates = "90,180"
        assertThat(validateCoordinates(sampleCoordinates)).isTrue()

        sampleCoordinates = "-90,-180"
        assertThat(validateCoordinates(sampleCoordinates)).isTrue()
    }
}