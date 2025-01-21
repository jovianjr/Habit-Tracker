package com.example.habittracker.ui.auth

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.habittracker.databinding.FragmentAuthLoginBinding
import com.example.habittracker.utils.UiState
import com.example.habittracker.utils.Validator
import com.example.habittracker.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentAuthLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tilEmail.editText?.setOnFocusChangeListener { _, isFocus ->
            if (isFocus) return@setOnFocusChangeListener
            validateEmail()
        }

        binding.tilPassword.editText?.setOnFocusChangeListener { _, isFocus ->
            if (isFocus) return@setOnFocusChangeListener
            validatePassword()
        }

        binding.btnLogin.setOnClickListener {
            if (validateEmail() && validatePassword()) {
                viewModel.login(
                    email = binding.tilEmail.editText?.text.toString(),
                    password = binding.tilPassword.editText?.text.toString()
                )
            }
        }
    }

    private fun validateEmail(): Boolean {
        val value = binding.tilEmail.editText!!.text.toString()
        if (Validator.isEmail(value)) {
            binding.tilEmail.isErrorEnabled = false
            return true
        } else {
            binding.tilEmail.error = "Invalid email address."
            return false
        }
    }

    private fun validatePassword(): Boolean {
        val value = binding.tilPassword.editText!!.text.toString()
        if (Validator.isPassword(value)) {
            binding.tilPassword.isErrorEnabled = false
            return true
        } else {
            binding.tilPassword.error =
                "Password must be at least 6 characters and include 1 uppercase letter, 1 lowercase letter, 1 number, and 1 symbol."
            return false
        }
    }

    private fun observer() {
        viewModel.login.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.tilEmail.isEnabled = false
                    binding.tilPassword.isEnabled = false
                }

                is UiState.Failure -> {
                    binding.tilEmail.isEnabled = true
                    binding.tilPassword.isEnabled = true

                    val builder = AlertDialog.Builder(activity)
                    builder.setTitle("Incorrect Credentials")
                    builder.setMessage("The username or password entered might be incorrect")
                    builder.setPositiveButton("OK", null)

                    val dialog = builder.create()
                    dialog.show()
                }

                is UiState.Success -> {
                    binding.tilEmail.isEnabled = true
                    binding.tilPassword.isEnabled = true
                    findNavController().navigateUp()
                }
            }
        }
    }
}