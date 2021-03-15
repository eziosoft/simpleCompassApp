/*
 * Created by Bartosz Szczygiel on 3/15/21 2:18 PM
 *  Copyright (c) 2021 . All rights reserved.
 *  Last modified 3/15/21 2:17 PM
 */

package com.eziosoft.simplecompassnetguru.ui.mainFragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eziosoft.simplecompassnetguru.R
import com.eziosoft.simplecompassnetguru.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private val viewModel by viewModels<MainFragmentViewModel>()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)
        binding.arrowImageView.isVisible = false


        binding.setDestinationButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToTargetInputFragment()
            findNavController().navigate(action)
        }


        viewModel.currentHeading.observe(viewLifecycleOwner) { heading ->
            setCompassHeading(heading)
        }

        viewModel.currentBearing.observe(viewLifecycleOwner) { bearing ->
            viewModel.currentHeading.value?.let { heading ->
                setCompassBearing(heading - bearing)
                if (!binding.arrowImageView.isVisible) binding.arrowImageView.isVisible = true
            }
        }

        viewModel.currentDistance.observe(viewLifecycleOwner) { distance ->
            binding.distanceTextView.text = getString(
                R.string.distance_to_the_destination,
                distance
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startReceivingData()
    }


    override fun onPause() {
        super.onPause()
        viewModel.stopReceivingData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setCompassBearing(bearing: Float) {
        rotateImage(binding.arrowImageView, 360 - bearing)
    }

    private fun setCompassHeading(heading: Float) {
        rotateImage(binding.compassImageView, 360 - heading)
    }

    private fun rotateImage(imageView: ImageView, angle: Float) {
        imageView.rotation = angle
    }


}