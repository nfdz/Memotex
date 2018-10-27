package io.github.nfdz.memotext.home

import android.content.Context
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.*

class HomeInteractorImpl(val context: Context) : HomeInteractor {

    var cachedTexts: List<Text>? = null

    override fun getSortCriteria(): SortCriteria {
        return SortCriteria.valueOf(context.getStringFromPreferences(R.string.pref_sort_criteria_key, R.string.pref_sort_criteria_default))
    }

    override fun setSortCriteria(sortCriteria: SortCriteria, callback: () -> Unit) {
        doAsync {
            context.setStringInPreferences(R.string.pref_sort_criteria_key, sortCriteria.name)
            doMainThread {
                callback()
            }
        }
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

    override fun loadTexts(forceUpdateCache: Boolean, callback: (List<Text>) -> Unit) {
        // TODO
        doAsync {
            if (cachedTexts == null) {
                cachedTexts = (1..90).flatMap { listOf(Text("Test title $it lorem ipsum apsala tumala torpaz camwxmi", "Lorem .e:f- w-w_a sa\n{} [] $% \n ipsum is simply dummy \rtext of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets", Level.SILVER, 10, 0L))}
            }
            when(getSortCriteria()) {
                SortCriteria.TITLE -> cachedTexts!!.sortedBy { it.title }
                SortCriteria.LEVEL -> cachedTexts!!.sortedBy { it.level }
                SortCriteria.PERCENTAGE -> cachedTexts!!.sortedBy { it.percentage }
                SortCriteria.DATE -> cachedTexts!!.sortedBy { it.timestamp }
            }
            doMainThread {
                callback(cachedTexts!!)
            }
        }
    }

    override fun changeTextLevel(text: Text, level: Level, callback: () -> Unit) {
        // TODO
    }

}