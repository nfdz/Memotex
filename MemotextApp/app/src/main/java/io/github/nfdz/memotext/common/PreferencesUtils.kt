package io.github.nfdz.memotext.common

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


private val LAST_SORT_CRITERIA_KEY = "sort_criteria"
private val DEFAULT_SORT_CRITERIA = SortCriteria.TITLE

private fun getSharedPreferences(context: Context): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(context)
}

fun Context.getSortCriteriaPref(): SortCriteria {
    val result: String? = getSharedPreferences(this).getString(LAST_SORT_CRITERIA_KEY, DEFAULT_SORT_CRITERIA.name)
    return if (result != null) SortCriteria.valueOf(result) else DEFAULT_SORT_CRITERIA
}

fun Context.saveSortCriteriaPref(sortCriteria: SortCriteria) {
    getSharedPreferences(this).edit().putString(LAST_SORT_CRITERIA_KEY, sortCriteria.name).apply()
}