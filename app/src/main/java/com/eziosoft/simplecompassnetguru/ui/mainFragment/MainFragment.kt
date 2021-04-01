/*
 * Created by Bartosz Szczygiel on 3/15/21 2:18 PM
 *  Copyright (c) 2021 . All rights reserved.
 *  Last modified 3/15/21 2:17 PM
 */

package com.eziosoft.simplecompassnetguru.ui.mainFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eziosoft.simplecompassnetguru.R
import com.eziosoft.simplecompassnetguru.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private val viewModel by viewModels<MainFragmentViewModel>()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.addRepositoryLifeCycle(lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        if (viewModel.currentTarget.value == null) binding.distanceTextView.text =
            getString(R.string.no_target_location)
        binding.arrowImageView.isVisible = false


        binding.setDestinationButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToTargetInputFragment()
            findNavController().navigate(action)
        }

        var first = false
        viewModel.currentHeading.observe(viewLifecycleOwner) { heading ->
            setCompassHeading(heading, !first)
            first = true
        }

        viewModel.currentBearing.observe(viewLifecycleOwner) { bearing ->
            viewModel.currentHeading.value?.let { heading ->
                binding.arrowImageView.isVisible.let { visible ->
                    setCompassBearing(heading - bearing, !visible)
                    if (!visible) binding.arrowImageView.isVisible = true
                }

            }
        }

        viewModel.currentDistance.observe(viewLifecycleOwner) { distance ->
            binding.distanceTextView.text = getString(
                R.string.distance_to_the_destination,
                distance
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setCompassBearing(bearing: Float, withAnimation: Boolean) {
        if (withAnimation)
            rotateImageWithAnimation(binding.arrowImageView, 360 - bearing)
        else
            rotateImage(binding.arrowImageView, 360 - bearing)

    }


    private fun setCompassHeading(heading: Float, withAnimation: Boolean) {
        if (withAnimation)
            rotateImageWithAnimation(binding.compassImageView, 360 - heading)
        else
            rotateImage(binding.compassImageView, 360 - heading)
    }

    private fun rotateImage(imageView: ImageView, angle: Float) {
        imageView.rotation = angle
    }

    private fun rotateImageWithAnimation(imageView: ImageView, angle: Float) {
        imageView.animate()
            .rotation(angle)
            .setInterpolator(DecelerateInterpolator())
            .setDuration(1000)
            .start()
    }


}