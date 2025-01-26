package com.example.habittracker.ui

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.adapter.HabitAdapter
import com.example.habittracker.databinding.FragmentHabitSettingsBinding
import com.example.habittracker.viewmodel.HabitViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HabitSettingsFragment : Fragment() {
    private val habitViewModel: HabitViewModel by viewModels()
    private var myHabits: MutableList<String> = mutableListOf()
    private var myHabitsOriginal: MutableList<String> = mutableListOf()
    private lateinit var binding: FragmentHabitSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myHabitsOriginal.addAll(arguments?.getStringArrayList("myHabits") ?: emptyList())
        myHabits.addAll(myHabitsOriginal)
        binding = FragmentHabitSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HabitAdapter(myHabits)
        binding.rvHabitList.layoutManager = LinearLayoutManager(activity)
        binding.rvHabitList.adapter = adapter

        // Delete habit handler (onSwipe)
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private val background: GradientDrawable = GradientDrawable().apply {
                setColor(resources.getColor(R.color.danger, null))
                cornerRadius = 24f
                cornerRadii = floatArrayOf(0f, 0f, 16f, 16f, 16f, 16f, 0f, 0f)
            }
            private val deleteIcon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_delete, null)

            private val textPaint = Paint().apply {
                color = Color.WHITE
                textSize = 40f
                isAntiAlias = true
                typeface =
                    ResourcesCompat.getFont(context!!, R.font.poppins_medium) ?: Typeface.DEFAULT
            }

            override fun onMove(
                v: RecyclerView,
                h: RecyclerView.ViewHolder,
                t: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(h: RecyclerView.ViewHolder, dir: Int) {
                val habitIdx: Int = h.bindingAdapterPosition
                myHabits.removeAt(habitIdx)
                binding.rvHabitList.adapter!!.notifyItemRemoved(habitIdx)
                binding.rvHabitList.adapter!!.notifyItemRangeChanged(
                    habitIdx,
                    myHabits.size
                )

            }

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                if (dX < 0) {
                    val itemView = viewHolder.itemView
                    // Draw background
                    background.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    background.draw(c)
                    // Draw icon
                    val metrics = resources.displayMetrics
                    val iconSize = (16 * metrics.density).toInt()
                    val iconMargin = (itemView.height - iconSize) / 2
                    deleteIcon!!.setBounds(
                        itemView.right - iconMargin - iconSize,
                        itemView.top + iconMargin,
                        itemView.right - iconMargin,
                        itemView.bottom - iconMargin
                    )
                    deleteIcon.setTint(Color.WHITE)
                    deleteIcon.draw(c)
                    // Draw text
                    val text = "Delete"
                    val textMargin = 32f
                    val textWidth = textPaint.measureText(text)
                    val textHeight = textPaint.textSize
                    val textX = itemView.right - iconMargin - iconSize - textMargin - textWidth
                    val textY = itemView.top + (itemView.height - textHeight) / 2 + textHeight
                    c.drawText(text, textX, textY, textPaint)
                }
            }
        }).attachToRecyclerView(binding.rvHabitList)

        // Add habit handler
        binding.tilAddHabit.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val newHabit: String = binding.tilAddHabit.editText!!.text.toString()
                myHabits.add(newHabit)
                binding.rvHabitList.adapter!!.notifyItemInserted(myHabits.size)
                binding.tilAddHabit.editText!!.clearFocus()
                binding.tilAddHabit.editText!!.text = null
            }
            return@setOnEditorActionListener false
        }

        // Save habit handler
        binding.btnSaveHabit.setOnClickListener {
            if(myHabits.isEmpty()) return@setOnClickListener
            if(!binding.tilAddHabit.editText?.text.isNullOrBlank()){
                val inputConnection = BaseInputConnection(binding.tilAddHabit, true)
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_DONE)
            }
            binding.btnSaveHabit.isEnabled = false
            binding.btnProgress.visibility = View.VISIBLE
            habitViewModel.storeHabits(myHabits) { success ->
                binding.btnSaveHabit.isEnabled = true
                binding.btnProgress.visibility = View.GONE
                if (success) {
                    findNavController().navigate(R.id.action_habitSettingsFragment_to_dashboardFragment_navigation)
                } else {
                    val builder = AlertDialog.Builder(activity)
                    builder.setTitle("Save Changes Failed")
                    builder.setMessage("Something went wrong. Please try again later.")
                    builder.setPositiveButton("OK", null)
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }

        // Button back
        binding.ibBack.setOnClickListener {
            if (myHabitsOriginal.isEmpty()) {
                findNavController().navigate(R.id.action_habitSettingsFragment_to_dashboardNoDataFragment_navigation)
            } else {
                findNavController().navigate(R.id.action_habitSettingsFragment_to_dashboardFragment_navigation)
            }
        }
    }
}