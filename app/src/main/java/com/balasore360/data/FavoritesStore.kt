package com.balasore360.data

import android.content.Context
import androidx.core.content.edit

/** Lightweight local saved-items store. Cloud sync can be added after authentication. */
class FavoritesStore(context: Context) {
    private val prefs = context.getSharedPreferences("balasore360_favorites", Context.MODE_PRIVATE)

    fun isFavorite(id: String): Boolean = prefs.getStringSet(KEY_IDS, emptySet())?.contains(id) == true

    fun toggle(id: String): Boolean {
        val ids = prefs.getStringSet(KEY_IDS, emptySet())?.toMutableSet() ?: mutableSetOf()
        val nowFavorite = ids.add(id)
        if (!nowFavorite) ids.remove(id)
        prefs.edit { putStringSet(KEY_IDS, ids) }
        return nowFavorite
    }

    fun all(): Set<String> = prefs.getStringSet(KEY_IDS, emptySet())?.toSet() ?: emptySet()

    private companion object { const val KEY_IDS = "saved_ids" }
}
