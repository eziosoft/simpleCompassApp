package com.eziosoft.simplecompassnetguru.ui.targetInputFragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eziosoft.simplecompassnetguru.R
import com.eziosoft.simplecompassnetguru.databinding.FragmentTargetInputBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TargetInputFragment : Fragment(R.layout.fragment_target_input) {
    private val viewModel by viewModels<TargetInputFragmentViewModel>()
    private lateinit var binding: FragmentTargetInputBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTargetInputBinding.bind(view)

        binding.targetLocationEditText.doOnTextChanged { text, start, before, count ->
            if (!viewModel.validateCoordinates(text.toString())) binding.targetLocationEditText.error =
                getString(
                    R.string.wrong_values
                )
        }

        binding.okButton.setOnClickListener() {
            if (viewModel.validateCoordinates(binding.targetLocationEditText.text.toString())) {
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



}