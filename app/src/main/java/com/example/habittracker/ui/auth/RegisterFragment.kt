package com.example.habittracker.ui.auth

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittracker.R
import com.example.habittracker.adapter.StepAdapter
import com.example.habittracker.data.model.User
import com.example.habittracker.databinding.FragmentAuthRegisterBinding
import com.example.habittracker.shared.utils.RegisterStepConstants
import com.example.habittracker.shared.utils.Validator
import com.example.habittracker.viewmodel.AuthViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentAuthRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()
    private val registerStep = RegisterStepConstants.steps

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set Step Adapter
        val adapter = StepAdapter(registerStep)
        binding.rvStep.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvStep.adapter = adapter

        // Validate Field
        validateFieldListener(binding.layoutStep1.tilName)
        validateFieldListener(binding.layoutStep1.tilEmail)
        validateFieldListener(binding.layoutStep1.tilPassword) { validateConfirmPassword() }
        validateFieldListener(binding.layoutStep1.tilConfirmPassword) { validateConfirmPassword() }

        // CheckBox Handler
        binding.layoutStep2.tvAgreeTos.setOnClickListener {
            binding.layoutStep2.cbAgreeTos.performClick()
        }
        binding.layoutStep2.cbAgreeTos.setOnCheckedChangeListener { _, checked ->
            binding.btnNextStep.isEnabled = checked
        }

        // Next Step Handler
        binding.btnNextStep.setOnClickListener {
            if (adapter.currentStep == 1) {
                if (!validateField(binding.layoutStep1.tilName)
                    || !validateField(binding.layoutStep1.tilEmail)
                    || !validateField(binding.layoutStep1.tilPassword)
                    || !validateField(binding.layoutStep1.tilConfirmPassword)
                    || !validateConfirmPassword()
                ) {
                    return@setOnClickListener
                }

                binding.layoutStep1.clStepOne.visibility = View.GONE
                binding.layoutStep2.clStepTwo.visibility = View.VISIBLE
                binding.btnNextStep.text = getString(R.string.submit)
                binding.btnNextStep.isEnabled = false
                adapter.nextStep()
            } else if (adapter.currentStep == 2) {
                val user = User(
                    name = binding.layoutStep1.tilName.editText!!.text.toString(),
                    email = binding.layoutStep1.tilEmail.editText!!.text.toString(),
                    password = binding.layoutStep1.tilPassword.editText!!.text.toString(),
                )
                binding.btnNextStep.isEnabled = false
                binding.btnProgress.visibility = View.VISIBLE
                authViewModel.register(user) { success ->
                    if (success) {
                        findNavController().navigate(R.id.action_registerFragment_to_registerSuccessFragment_navigation)
                    } else {
                        val builder = AlertDialog.Builder(activity)
                        builder.setTitle("Register Failed")
                        builder.setMessage("Something went wrong. Please try again later.")
                        builder.setPositiveButton("OK", null)

                        val dialog = builder.create()
                        dialog.show()
                    }
                    binding.btnNextStep.icon = null
                    binding.btnNextStep.isEnabled = true
                    binding.btnProgress.visibility = View.GONE
                }
            }
        }

        // Back Handler
        binding.ibBack.setOnClickListener {
            if (adapter.currentStep == 1) {
                findNavController().navigateUp()
            } else if (adapter.currentStep == 2) {
                binding.layoutStep1.clStepOne.visibility = View.VISIBLE
                binding.layoutStep2.clStepTwo.visibility = View.GONE
                binding.btnNextStep.text = getString(R.string.next_step)
                adapter.previousStep()
                binding.rvStep.adapter?.notifyItemChanged(adapter.currentStep)
                binding.btnNextStep.isEnabled = true
            }
        }
    }

    private fun validateConfirmPassword(): Boolean {
        val password = binding.layoutStep1.tilPassword.editText?.text.toString()
        val confirmPassword = binding.layoutStep1.tilConfirmPassword.editText?.text.toString()
        if (password != confirmPassword) {
            binding.layoutStep1.tilConfirmPassword.error = "Must match the password."
            return false
        }
        binding.layoutStep1.tilConfirmPassword.isErrorEnabled = false
        return true
    }

    private fun validateField(til: TextInputLayout): Boolean {
        val et: EditText = til.editText!!
        if (et.text.isNullOrBlank()) {
            til.error = "This field can’t be left blank."
            return false
        }

        val isEmailAddress =
            et.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        val isPassword =
            et.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        if (isEmailAddress && !Validator.isEmail(et.text.toString())) {
            til.error = "Invalid email address."
            return false
        } else if (isPassword && !Validator.isPassword(et.text.toString())) {
            til.error =
                "Password must be at least 6 characters and include 1 uppercase letter, 1 lowercase letter, 1 number, and 1 symbol."
            return false
        }
        til.isErrorEnabled = false
        return true
    }

    private fun validateFieldListener(
        til: TextInputLayout,
        customFunc: (() -> Unit)? = null
    ) {
        if (til.editText == null) return
        val handler = Handler(Looper.getMainLooper())
        var runnable: Runnable? = null

        til.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                runnable?.let { handler.removeCallbacks(it) }
                runnable = Runnable {
                    til.error = null
                    if (!validateField(til)) return@Runnable
                    customFunc?.invoke()
                }
                handler.postDelayed(runnable!!, 500)

            }
        })
    }
}