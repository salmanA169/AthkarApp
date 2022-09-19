package com.athkar.sa.selection

import androidx.recyclerview.selection.ItemKeyProvider
import com.athkar.sa.adapters.FavoriteAdapter
import com.athkar.sa.db.entity.toFavoriteTracker
import com.athkar.sa.models.FavoriteTracker

class StableKeyFavoriteTracker(private val favoriteAdapter: FavoriteAdapter) : ItemKeyProvider<FavoriteTracker>(SCOPE_CACHED) {
    override fun getKey(position: Int): FavoriteTracker? {
        return favoriteAdapter.currentList[position].toFavoriteTracker()
    }

    override fun getPosition(key: FavoriteTracker): Int {
        return favoriteAdapter.currentList.indexOfFirst { it.toFavoriteTracker() == key }
    }
}