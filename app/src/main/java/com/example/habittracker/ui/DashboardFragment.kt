package com.example.habittracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.adapter.MyAdapter
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generateRecycleViewTodo()
        generateRecycleViewCompleted()

        binding.ivProfileImage.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_profileFragment_navigation)
        }
    }

    private fun generateRecycleViewTodo() {
        // Data untuk RecyclerView
        val data = List(2) { "Item ${it + 1}" }

        // Temukan RecyclerView di layout
        val recyclerView: RecyclerView = binding.rvDataTodo

        // Atur layout manager
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // Atur adapter
        recyclerView.adapter = MyAdapter(data)
    }

    private fun generateRecycleViewCompleted() {
        // Data untuk RecyclerView
        val data = List(1) { "Item ${it + 1}" }

        // Temukan RecyclerView di layout
        val recyclerView: RecyclerView = binding.rvDataCompleted
        // Atur layout manager
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // Atur adapter
        recyclerView.adapter = MyAdapter(data)
    }
}