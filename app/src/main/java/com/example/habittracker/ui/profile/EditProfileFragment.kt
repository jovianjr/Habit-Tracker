package com.example.habittracker.ui.profile

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.data.model.User
import com.example.habittracker.databinding.FragmentEditProfileBinding
import com.example.habittracker.shared.utils.ProfileImageListConstants
import com.example.habittracker.viewmodel.AuthViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var user: User
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUser()
        validateField(binding.tilName)
        validateField(binding.tilNoteToSelf)
        setProfileImage(user.profileImage)

        binding.btnSaveProfile.setOnClickListener {
            binding.btnSaveProfile.isEnabled = false
            binding.btnProgress.visibility = View.VISIBLE
            if (!binding.tilName.isErrorEnabled && !binding.tilNoteToSelf.isErrorEnabled) {
                user.name = binding.tilName.editText?.text.toString()
                user.noteToSelf = binding.tilNoteToSelf.editText?.text.toString()
                authViewModel.updateUser(user) { success ->
                    if (success) {
                        findNavController().navigateUp()
                    } else {
                        val builder = AlertDialog.Builder(activity)
                        builder.setTitle("Save Changes Failed")
                        builder.setMessage("Something went wrong. Please try again later.")
                        builder.setPositiveButton("OK", null)
                        val dialog = builder.create()
                        dialog.show()
                    }
                }
                binding.btnSaveProfile.isEnabled = true
                binding.btnProgress.visibility = View.GONE
            }
        }

        binding.ibEditProfile.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("profileImage", user.profileImage)

            setFragmentResultListener("getFinalProfileImage") { _, resBundle ->
                val result = resBundle.getString("profileImage")
                user.profileImage = result
                setProfileImage(result)
                toggleEnableBtnSaveProfiles()
            }

            findNavController().navigate(
                R.id.action_editProfileFragment_to_editProfileImageFragment_navigation,
                bundle
            )
        }

        binding.ibBack.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment_navigation)
        }

    }

    private fun getUser() {
        authViewModel.getUser {
            if (it != null) {
                binding.tilName.editText?.setText(it.name)
                binding.tilNoteToSelf.editText?.setText(it.noteToSelf)
                user = it
            }
        }
    }

    private fun setProfileImage(profileImageName: String?) {
        val profileImageId = ProfileImageListConstants.images[profileImageName]
            ?: ProfileImageListConstants.images["default"]
        if (profileImageId != null)
            binding.sivProfileImage.setImageResource(profileImageId)
    }

    private fun validateField(til: TextInputLayout) {
        til.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                til.error = null
                if (til.editText?.text.isNullOrBlank()) {
                    til.error = "This field can’t be left blank."
                }

                toggleEnableBtnSaveProfiles()
            }
        })
    }

    private fun toggleEnableBtnSaveProfiles() {
        binding.btnSaveProfile.isEnabled =
            !binding.tilName.isErrorEnabled
                    && !binding.tilNoteToSelf.isErrorEnabled
                    && !binding.tilName.editText?.text.isNullOrBlank()
                    && !binding.tilNoteToSelf.editText?.text.isNullOrBlank()
    }
}