package com.athkar.sa

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.athkar.sa.db.entity.Athkar
import com.athkar.sa.ui.homeScreen.athkar.athkarItem.AthkarItemFragment

class AthkarFragmentPager(f: Fragment) : FragmentStateAdapter(f) {
    private val athkarFragments = mutableListOf<Athkar>()
    override fun getItemCount(): Int {
        return athkarFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        val getItems = athkarFragments[position]
        return AthkarItemFragment().apply {
            arguments = bundleOf(Constants.ATHKAR_BUNDLE_KEY_OBJECT to getItems)
        }
    }

    fun getAthkarByPosition(position: Int) = athkarFragments.get(position)
    fun getPositionByName(nameAlthker: String): Int {
        return athkarFragments.indexOfFirst { it.nameAlthker == nameAlthker }
    }

    fun getSizeAthkr() = athkarFragments.size
    fun addAthkars(athkar: Athkar) {
        athkarFragments.add(athkar)
    }
    fun clearList(){
        athkarFragments.clear()
    }
}