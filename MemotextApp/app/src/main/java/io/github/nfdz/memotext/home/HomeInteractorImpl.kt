package io.github.nfdz.memotext.home

import android.content.Context
import io.github.nfdz.memotext.common.*

class HomeInteractorImpl(val context: Context) : HomeInteractor {

    var cachedTexts: List<Text>? = null

    override fun getSortCriteria(): SortCriteria {
        return context.getSortCriteriaPref()
    }

    override fun saveSortCriteria(sortCriteria: SortCriteria) {
        context.saveSortCriteriaPref(sortCriteria)
    }

    override fun deleteText(text: Text, callback: (Text) -> Unit) {
        val mutableList = cachedTexts!!.toMutableList()
        mutableList.remove(text)
        cachedTexts = mutableList.toList()
        callback(text)
    }

    override fun undoDeleteText(text: Text, callback: () -> Unit) {
        val mutableList = cachedTexts!!.toMutableList()
        mutableList.add(text)
        cachedTexts = mutableList.toList()
        callback()
    }

    override fun loadTexts(callback: (List<Text>) -> Unit) {
        if (cachedTexts == null) {
            cachedTexts = (1..90).flatMap { listOf(Text("Test title $it", "Lorem ipsum", Level.SILVER, 10, 0L))}
        }
        when(getSortCriteria()) {
            SortCriteria.TITLE -> cachedTexts!!.sortedBy { it.title }
            SortCriteria.LEVEL -> cachedTexts!!.sortedBy { it.level }
            SortCriteria.PERCENTAGE -> cachedTexts!!.sortedBy { it.percentage }
            SortCriteria.DATE -> cachedTexts!!.sortedBy { it.timestamp }
        }
        callback(cachedTexts!!)
    }

}