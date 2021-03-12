package com.eziosoft.simplecompassnetguru.ui.mainFragment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.eziosoft.simplecompassnetguru.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    state: SavedStateHandle,
    val repository: Repository
) : ViewModel() {
//TODO save coordinates to state in case of process death


}