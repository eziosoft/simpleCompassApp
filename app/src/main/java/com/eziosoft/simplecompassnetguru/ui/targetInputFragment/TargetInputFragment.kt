/*
 * Created by Bartosz Szczygiel on 3/15/21 2:18 PM
 *  Copyright (c) 2021 . All rights reserved.
 *  Last modified 3/15/21 2:17 PM
 */

package com.eziosoft.simplecompassnetguru.ui.targetInputFragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.eziosoft.simplecompassnetguru.R
import com.eziosoft.simplecompassnetguru.databinding.FragmentTargetInputBinding
import com.eziosoft.simplecompassnetguru.utils.validateCoordinates
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking



@AndroidEntryPoint
class TargetInputFragment : Fragment(R.layout.fragment_target_input) {
    private val viewModel by viewModels<TargetInputFragmentViewModel>()
    private var _binding: FragmentTargetInputBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTargetInputBinding.bind(view)

        lifecycleScope.launch {
            binding.targetLocationEditText.setText(viewModel.getLastCoordinates().first())
        }

        binding.targetLocationEditText.doOnTextChanged { text, _, _, _ ->
            if (!validateCoordinates(text.toString())) binding.targetLocationEditText.error =
                getString(
                    R.string.wrong_values
                )
        }

        viewModel.currentTargetLocation.value?.let { location ->
            binding.targetLocationEditText.setText("${location.latitude},${location.longitude}")
        }

        binding.okButton.setOnClickListener {
            if (validateCoordinates(binding.targetLocationEditText.text.toString())) {
                viewModel.saveCoordinates(binding.targetLocationEditText.text.toString())
                requireActivity().onBackPressed()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.wrong_coordinates),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}