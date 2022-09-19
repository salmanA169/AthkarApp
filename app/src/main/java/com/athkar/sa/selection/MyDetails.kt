package com.athkar.sa.selection

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.athkar.sa.adapters.FavoriteViewHolder
import com.athkar.sa.models.FavoriteTracker

class MyDetails(private val recyclerView: RecyclerView) : ItemDetailsLookup<FavoriteTracker>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<FavoriteTracker>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            //4
            return (recyclerView.getChildViewHolder(view) as FavoriteViewHolder).getItem()
        }
        return null
    }
}
