package com.balasore360.data

/** Stable identifiers used by the local favorites feature. */
object SavedItem {
    fun business(id: String) = "business:$id"
    fun place(name: String) = "place:${name.trim().lowercase()}"
    fun event(id: String) = "event:$id"
    fun news(id: String) = "news:$id"
}
