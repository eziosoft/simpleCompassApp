/*
 * Created by Bartosz Szczygiel on 3/15/21 2:18 PM
 *  Copyright (c) 2021 . All rights reserved.
 *  Last modified 3/15/21 2:17 PM
 */

package com.eziosoft.simplecompassnetguru.ui.mainFragment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.eziosoft.simplecompassnetguru.repository.DefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    state: SavedStateHandle,
    private val repository: DefaultRepository
) : ViewModel() {
//TODO save coordinates to state in case of process death

    val currentHeading = repository.currentHeading()
    val currentBearing = repository.currentBearing()
    val currentDistance = repository.currentDistance()

    fun startReceivingData() = repository.start()
    fun stopReceivingData() = repository.stop()
}