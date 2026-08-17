package com.balasore360.data

import android.content.Context
import org.json.JSONArray

/** Lightweight local favorites store. No account is required; favorites can be synced later. */
class FavoriteStore(context: Context) {
    private val prefs = context.getSharedPreferences("balasore360_favorites", Context.MODE_PRIVATE)

    fun getFavorites(): Set<String> = prefs.getStringSet(KEY, emptySet())?.toSet() ?: emptySet()

    fun isFavorite(id: String): Boolean = getFavorites().contains(id)

    fun toggle(id: String): Boolean {
        val current = getFavorites().toMutableSet()
        val added = current.add(id)
        if (!added) current.remove(id)
        prefs.edit().putStringSet(KEY, current).apply()
        return added
    }

    fun clear() {
        prefs.edit().remove(KEY).apply()
    }

    fun exportJson(): String = JSONArray(getFavorites().toList()).toString()

    companion object {
        private const val KEY = "favorite_ids"
    }
}
