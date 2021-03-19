/*
 * Created by Bartosz Szczygiel on 3/15/21 2:18 PM
 *  Copyright (c) 2021 . All rights reserved.
 *  Last modified 3/14/21 9:43 PM
 */

package com.eziosoft.simplecompassnetguru.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.eziosoft.simplecompassnetguru.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UtilsKtTest {

//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()


//    @get:Rule
//    var mainCoroutineRule = MainCoroutineRule()

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