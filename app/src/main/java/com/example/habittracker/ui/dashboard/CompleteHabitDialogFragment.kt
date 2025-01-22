package com.example.habittracker.ui.dashboard

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.habittracker.R
import com.example.habittracker.databinding.DialogFragmentCompleteHabitBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KFunction2

@AndroidEntryPoint
class CompleteHabitDialogFragment(
    private val habitName: String,
    private val onConfirm: KFunction2<String, (Boolean) -> Unit, Unit>,
) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogFragmentCompleteHabitBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentCompleteHabitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NO_FRAME, R.style.TransparentDialogTheme)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // used to show the bottom sheet dialog
        dialog?.setOnShowListener {
            binding.cvCompleteHabit.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvHabitName.text = habitName
        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
        binding.btnOk.setOnClickListener {
            dialog?.setCancelable(false)
            binding.btnOk.isEnabled = false
            binding.btnCancel.isEnabled = false

            onConfirm.invoke(habitName) { success ->
                if (success) {
                    dialog?.setCancelable(true)
                    binding.btnOk.isEnabled = true
                    binding.btnCancel.isEnabled = true
                    dialog?.dismiss()
                } else {
                    dialog?.setCancelable(true)
                    binding.btnOk.isEnabled = true
                    binding.btnCancel.isEnabled = true

                    val builder = AlertDialog.Builder(activity)
                    builder.setTitle("Complete Habit Failed")
                    builder.setMessage("Something went wrong. Please try again later")
                    builder.setPositiveButton("OK", null)
                    builder.create().show()
                }
            }
            dialog?.dismiss()
        }
    }

    companion object {
        const val TAG = "CompleteHabitDialogFragment"
    }
}

