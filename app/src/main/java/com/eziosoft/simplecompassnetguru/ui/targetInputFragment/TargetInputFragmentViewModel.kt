/*
 * Created by Bartosz Szczygiel on 3/15/21 2:18 PM
 *  Copyright (c) 2021 . All rights reserved.
 *  Last modified 3/15/21 2:17 PM
 */

package com.eziosoft.simplecompassnetguru.ui.targetInputFragment

import android.content.Context
import android.location.Location
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eziosoft.simplecompassnetguru.repository.DefaultRepository
import com.eziosoft.simplecompassnetguru.utils.TARGET_POSITION
import com.eziosoft.simplecompassnetguru.utils.dataStore
import com.eziosoft.simplecompassnetguru.utils.validateCoordinates
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TargetInputFragmentViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    state: SavedStateHandle,
    val defaultRepository: DefaultRepository
) : ViewModel() {

    //TODO save coordinates for future use
    // save coordinates to state in case of process death

    fun saveCoordinates(coordinates: String) {
        if (validateCoordinates(coordinates)) {
            val a = coordinates.split(",")
            val lat = a[0].toDouble()
            val lon = a[1].toDouble()

            val location = Location("")
            location.apply {
                latitude = lat
                longitude = lon
                defaultRepository.setTargetLocation(this)
            }

            viewModelScope.launch {
                context.dataStore.edit { settings ->
                    settings[TARGET_POSITION] = coordinates
                }
            }
        }
    }

    fun getLastCoordinates() =
        context.dataStore.data.map {
            it[TARGET_POSITION] ?: ""
        }


}