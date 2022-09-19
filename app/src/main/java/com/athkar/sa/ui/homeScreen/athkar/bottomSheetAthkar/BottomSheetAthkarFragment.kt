package com.athkar.sa.ui.homeScreen.athkar.bottomSheetAthkar

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.athkar.sa.Constants
import com.athkar.sa.adapters.BottomAthkarAdapter
import com.athkar.sa.databinding.FragmentBottomSheetAthkarBinding
import com.athkar.sa.ui.homeScreen.athkar.AthkarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetAthkarFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetAthkarBinding? = null

    val binding get() = _binding!!
    private val bottomAdapter = BottomAthkarAdapter {
        viewModel.moveToPosition(it)
        dismiss()
    }
    private val viewModel by viewModels<AthkarViewModel>({ requireParentFragment() })
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetAthkarBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvBottomItems.adapter = bottomAdapter
        val namesAlthker = requireArguments().getStringArrayList(Constants.ITEM_BOTTOM_KEY)!!
        val colorAlther  = requireArguments().getString(Constants.COLOR_ALTHKER_BOTTOM_KEY)
        val getTtile = requireArguments().getString(Constants.TITLE_BOTTOM_KEY,"")
        bottomAdapter.submitList(namesAlthker)
        binding.nameAlthker.text = getTtile
        binding.rvBottomItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                binding.divider.isVisible = recyclerView.canScrollVertically(-1)
            }
        })
        binding.divider.dividerColor = Color.parseColor(colorAlther)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}