package com.example.habittracker.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.habittracker.R
import com.example.habittracker.adapter.ProfileImageAdapter
import com.example.habittracker.databinding.FragmentEditProfileImageBinding
import com.example.habittracker.shared.utils.ProfileImageListConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileImageFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileImageBinding
    private lateinit var currentProfileImage: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        currentProfileImage = arguments?.getString("profileImage").toString()
        binding = FragmentEditProfileImageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ProfileImageListConstants.images.keys.toList()
        val adapter = ProfileImageAdapter(imageList)
        binding.rvImageList.layoutManager = GridLayoutManager(activity, 2)
        binding.rvImageList.adapter = adapter
        adapter.selectedImage = imageList.indexOf(currentProfileImage)

        binding.btnDone.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("profileImage", imageList[adapter.selectedImage])
            setFragmentResult("getFinalProfileImage", bundle)
            findNavController().navigateUp()
        }

        binding.ibBack.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileImageFragment_to_editProfileFragment_navigation)
        }
    }
}