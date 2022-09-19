package com.athkar.sa.ui.homeScreen.container.favorite

import android.view.*
import androidx.appcompat.view.ActionMode
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.selection.*
import com.athkar.sa.MainActivity
import com.athkar.sa.R
import com.athkar.sa.adapters.FavoriteAdapter
import com.athkar.sa.databinding.FavoriteFragmentBinding
import com.athkar.sa.models.FavoriteTracker
import com.athkar.sa.models.toFavoriteAlthker
import com.athkar.sa.selection.MyDetails
import com.athkar.sa.selection.StableKeyFavoriteTracker
import com.athkar.sa.uitls.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment :
    BaseFragment<FavoriteFragmentBinding>({ inflater: LayoutInflater, container: ViewGroup? ->
        FavoriteFragmentBinding.inflate(inflater, container, false)
    }), ActionMode.Callback {

    private val TAG = javaClass.simpleName
    private val viewModel by viewModels<FavoriteViewModel>()
    private val favoriteAdapter = FavoriteAdapter(click = { athkarCategory, nameAlthker ->
        controller.navigate(
            FavoriteFragmentDirections.actionFavoriteFragmentToAthkarFragment(
                athkarCategory,
                nameAlthker
            )
        )
    })


    override fun onDestroyView() {
        super.onDestroyView()
        actionMode?.finish()
    }

    private lateinit var tracker: SelectionTracker<FavoriteTracker>
    private var actionMode: ActionMode? = null

    override fun FavoriteFragmentBinding.init() {

        rvFavorite.adapter = favoriteAdapter
        tracker = SelectionTracker.Builder(
            "s",
            rvFavorite,
            StableKeyFavoriteTracker(favoriteAdapter),
            MyDetails(rvFavorite),
            StorageStrategy.createParcelableStorage(FavoriteTracker::class.java)
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        favoriteAdapter.tracker = tracker

        tracker.addObserver(object : SelectionTracker.SelectionObserver<FavoriteTracker>() {

            override fun onSelectionChanged() {
                super.onSelectionChanged()
                if (actionMode == null) {
                    val currentActivity = requireActivity() as MainActivity
                    actionMode = currentActivity.startSupportActionMode(this@FavoriteFragment)
                }

                val items = tracker.selection.size()
                if (items > 0) {
                    actionMode?.title = items.toString()
                } else {
                    actionMode?.finish()
                }
            }
        })
        toolbar.setNavigationOnClickListener {
            controller.popBackStack()
        }
    }

    override fun observe() {
        viewModel.favorites.observeFlow {
            favoriteAdapter.submitList(it)

        }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.delete_menu, menu)
        binding.toolbar.isVisible = false
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.delete -> {
                tracker.selection.forEach {
                    viewModel.removeAlthker(it.toFavoriteAlthker())
                }
                actionMode?.finish()
                true
            }
            else -> false
        }
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        tracker.clearSelection()
        actionMode?.finish()
        actionMode = null
        binding.toolbar.isVisible = true


    }
}