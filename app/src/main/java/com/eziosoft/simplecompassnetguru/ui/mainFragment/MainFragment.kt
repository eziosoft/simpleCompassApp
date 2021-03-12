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
    private lateinit var binding: FragmentMainBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)


        binding.setDestinationButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToTargetInputFragment()
            findNavController().navigate(action)
        }


        binding.arrowImageView.isVisible = false
        viewModel.repository.currentHeading().observe(viewLifecycleOwner) { heading ->
            setCompassHeading(heading)
        }

        viewModel.repository.currentBearing().observe(viewLifecycleOwner) { bearing ->
            viewModel.repository.currentHeading().value?.let { heading ->
                setCompassBearing(heading - bearing)
                binding.arrowImageView.isVisible = true
            }
        }

        viewModel.repository.currentDistance().observe(viewLifecycleOwner) { distance ->
            binding.distanceTextView.text = getString(
                R.string.distance_to_the_destination,
                distance
            )
        }

//        viewModel.repository.currentLocation().observe(viewLifecycleOwner) { location ->
//            Toast.makeText(
//                requireContext(),
//                "Location: ${location.longitude},${location.latitude}",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
    }


    private fun setCompassBearing(bearing: Float) {
        rotateImage(binding.arrowImageView, 360 - bearing)
    }

    private fun setCompassHeading(heading: Float) {
        rotateImage(binding.compassImageView, 360 - heading)
    }

    override fun onResume() {
        super.onResume()
        viewModel.repository.start()
    }


    override fun onPause() {
        super.onPause()
        viewModel.repository.stop()
    }


    private fun rotateImage(imageView: ImageView, angle: Float) {
        imageView.rotation = angle
    }
}