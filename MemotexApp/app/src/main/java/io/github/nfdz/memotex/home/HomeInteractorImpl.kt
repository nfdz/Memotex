package io.github.nfdz.memotex.home

import android.content.Context
import android.preference.PreferenceManager
import io.github.nfdz.memotex.R
import io.github.nfdz.memotex.common.*
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults

class HomeInteractorImpl(val context: Context) : HomeInteractor, RealmChangeListener<RealmResults<TextRealm>> {

    var listener: ((List<TextRealm>) -> Unit)? = null
    var cachedTexts: List<TextRealm> = emptyList()
    var realm: Realm? = null

    override fun initialize(listener: (List<TextRealm>) -> Unit) {
        this.listener = listener
        realm = Realm.getDefaultInstance()
        realm?.where(TextRealm::class.java)?.findAllAsync()?.addChangeListener(this)
    }

    override fun destroy() {
        realm?.removeAllChangeListeners()
        realm?.close()
        realm = null
    }

    override fun showTutorial(): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return if (prefs.getBoolean("FIRST_TIME", true)) {
            prefs.edit().putBoolean("FIRST_TIME", false).apply()
            true
        } else {
            false
        }
    }

    override fun onChange(t: RealmResults<TextRealm>) {
        cachedTexts = t
        notifyTexts()
    }

    private fun notifyTexts() {
        listener?.invoke(when(getSortCriteria()) {
            SortCriteria.TITLE -> cachedTexts.sortedBy { it.title.toUpperCase() }
            SortCriteria.LEVEL -> cachedTexts.sortedWith(compareBy({it.getLevel()},{it.title.toUpperCase()}))
            SortCriteria.PERCENTAGE -> cachedTexts.sortedWith(compareBy({it.percentage},{it.title.toUpperCase()}))
            SortCriteria.DATE -> cachedTexts.sortedWith(compareBy({-it.timestamp},{it.title.toUpperCase()}))
        })
    }


    override fun getSortCriteria(): SortCriteria {
        return SortCriteria.valueOf(context.getStringFromPreferences(R.string.pref_sort_criteria_key, R.string.pref_sort_criteria_default))
    }

    override fun setSortCriteria(sortCriteria: SortCriteria) {
        doAsync {
            context.setStringInPreferences(R.string.pref_sort_criteria_key, sortCriteria.name)
            doMainThread {
                notifyTexts()
            }
        }
    }

    override fun deleteText(title: String, callback: (TextRealm) -> Unit) {
        realm?.beginTransaction()
        cachedTexts.find { it.title == title }?.let {
            val copy = TextRealm(it.title, it.content, it.levelString, it.percentage, it.timestamp)
            it.deleteFromRealm()
            callback(copy)
        }
        realm?.commitTransaction()
    }

    override fun undoDeleteText(text: TextRealm) {
        realm?.beginTransaction()
        realm?.copyToRealmOrUpdate(text)
        realm?.commitTransaction()
    }

    override fun changeTextLevel(title: String, level: Level) {
        realm?.beginTransaction()
        cachedTexts.find { it.title == title }?.levelString = level.name
        realm?.commitTransaction()
    }

    override fun getTextContent(title: String): String {
        return cachedTexts.find { it.title == title }?.content ?: ""
    }

}