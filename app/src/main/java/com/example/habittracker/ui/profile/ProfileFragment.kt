package com.example.habittracker.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentProfileBinding
import com.example.habittracker.shared.utils.ProfileImageListConstants
import com.example.habittracker.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUser()


        binding.ibEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment_navigation)
        }

        binding.btnLogout.setOnClickListener {
            authViewModel.logout {
                findNavController().navigate(R.id.action_profileFragment_to_welcomeScreenFragment_navigation)
            }
        }

        // Handle Back
        fun handleBack() {
            findNavController().navigate(R.id.action_profileFragment_to_dashboardFragment_navigation)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { handleBack() }
        binding.ibBack.setOnClickListener { handleBack() }

    }

    private fun getUser() {
        authViewModel.getUser { user ->
            if (user != null) {
                binding.tvName.text = user.name
                binding.tvEmail.text = user.email
                binding.tvNoteToSelf.text = user.noteToSelf
                // set profile image
                val profileImageId = ProfileImageListConstants.images[user.profileImage]
                    ?: ProfileImageListConstants.images["default"]
                if (profileImageId != null)
                    binding.sivProfileImage.setImageResource(profileImageId)
            }
        }
    }
}