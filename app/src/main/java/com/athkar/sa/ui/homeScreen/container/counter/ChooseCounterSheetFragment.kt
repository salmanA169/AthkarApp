package com.athkar.sa.ui.homeScreen.container.counter

import android.content.DialogInterface
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.athkar.sa.R
import com.athkar.sa.adapters.CounterAlthkerAdapter
import com.athkar.sa.adapters.CounterItemViewHolder
import com.athkar.sa.databinding.CounterBottomFragmentBinding
import com.athkar.sa.databinding.EditTextViewBinding
import com.athkar.sa.db.entity.CounterAlthker
import com.athkar.sa.uitls.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.divider.MaterialDividerItemDecoration.VERTICAL
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseCounterSheetFragment :
    BaseFragment<CounterBottomFragmentBinding>({ inflater, container ->
        CounterBottomFragmentBinding.inflate(inflater, container, false)
    }) {

    private val counterAdapter = CounterAlthkerAdapter {
        viewModel.saveNewCounter(it)
    }
    private val viewModel by viewModels<ChooseCounterViewModel>()
    private var currentList = emptyList<CounterAlthker>()
    private lateinit var inputMethod: InputMethodManager

    override fun CounterBottomFragmentBinding.init() {
        inputMethod = requireActivity().getSystemService(InputMethodManager::class.java)
        ItemTouchHelper(swipeHelper).attachToRecyclerView(binding.rvCounterAlthker)
        binding.toolbar.setNavigationOnClickListener {
            controller.popBackStack()
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.refresh_menu -> {

                    viewModel.refreshCounters(currentList) {
                        Snackbar.make(requireView(), "تم تصفير العداد بنجاح", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    true
                }
                else -> false
            }
        }
        binding.rvCounterAlthker.adapter = counterAdapter
        binding.rvCounterAlthker.addItemDecoration(
            MaterialDividerItemDecoration(
                requireContext(),
                VERTICAL
            )
        )
        binding.floatingActionButton.setOnClickListener {
            val layoutInflater = EditTextViewBinding.inflate(getLayoutInflater())
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle("اضف ذكر جديد")
                setView(layoutInflater.root)
                setPositiveButton("اضف") { dialogInterface: DialogInterface, i: Int ->
                    viewModel.addNewCounter(
                        CounterAlthker(
                            layoutInflater.root.editText!!.text.toString(),
                            0
                        )
                    )
                }
                setNegativeButton("الغاء") { s, _ ->
                    s.dismiss()
                }
            }.create().apply {
                setOnShowListener {
                    layoutInflater.root.requestFocus()
                }
                window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                show()
            }
        }
    }

    private val swipeHelper =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                return true
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val counterViewHolder = viewHolder as? CounterItemViewHolder
                counterViewHolder?.let {
                    val getTag = it.itemView.tag as? CounterAlthker
                    getTag?.let { counterAlthker ->
                        viewModel.removeCounter(counterAlthker)
                        Snackbar.make(requireView(), "تم الحذف", Snackbar.LENGTH_SHORT).apply {
                            setAction("تراجع") {
                                viewModel.addNewCounter(counterAlthker)
                            }
                        }.show()
                    }
                }
            }
        }


    override fun observe() {

        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.counters.collect {
                    currentList = it
                    if (it.isEmpty()) {
                        counterAdapter.submitList(it) {
                            binding.tvEmpty.isVisible = true
                        }
                    } else {
                        counterAdapter.submitList(it) {
                            binding.tvEmpty.isVisible = false
                        }
                    }
                }
            }
        }
        viewModel.popBack.observe {
            if (it) {
                controller.popBackStack()
            }
        }
    }

}