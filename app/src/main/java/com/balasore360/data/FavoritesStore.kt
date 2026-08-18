package com.balasore360.data

import android.content.Context
import androidx.core.content.edit

/** Local saved-items store. Uses one shared preference key for backward-compatible favorites. */
class FavoritesStore(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    init {
        // Migrate the newer saved_ids key into the canonical favorite_ids key once.
        val legacySaved = prefs.getStringSet(LEGACY_KEY, null)
        if (legacySaved != null && prefs.getStringSet(KEY_IDS, null) == null) {
            prefs.edit { putStringSet(KEY_IDS, legacySaved) }
        }
    }

    fun isFavorite(id: String): Boolean = all().contains(id)

    fun toggle(id: String): Boolean {
        val ids = all().toMutableSet()
        val nowFavorite = ids.add(id)
        if (!nowFavorite) ids.remove(id)
        prefs.edit { putStringSet(KEY_IDS, ids) }
        return nowFavorite
    }

    fun all(): Set<String> = prefs.getStringSet(KEY_IDS, emptySet())?.toSet() ?: emptySet()

    fun clear() = prefs.edit { remove(KEY_IDS) }

    private companion object {
        const val PREFS_NAME = "balasore360_favorites"
        const val KEY_IDS = "favorite_ids"
        const val LEGACY_KEY = "saved_ids"
    }
}
