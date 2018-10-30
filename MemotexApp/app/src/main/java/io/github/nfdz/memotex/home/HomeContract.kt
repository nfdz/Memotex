package io.github.nfdz.memotex.home

import io.github.nfdz.memotex.common.Level
import io.github.nfdz.memotex.common.SortCriteria
import io.github.nfdz.memotex.common.TextRealm

interface HomeView {
    fun setContent(texts: List<TextRealm>)
    fun showDeletedMessage(text: TextRealm)
    fun askSortCriteria(currentSortCriteria: SortCriteria)
    fun askLevel(title: String, level: Level)
    fun navigateToAddText()
    fun navigateToEditText(title: String, content: String)
    fun navigateToExercise(title: String, content: String, level: Level)
    fun navigateToSettings()
    fun navigateToTutorial()
}

interface HomePresenter {
    fun onCreate()
    fun onDestroy()
    fun onSortCriteriaSelected(sortCriteria: SortCriteria)
    fun onAddTextClick()
    fun onEditTextClick(title: String)
    fun onDeleteTextClick(title: String)
    fun onUndoDeleteTextClick(text: TextRealm)
    fun onLevelIconClick(title: String, level: Level)
    fun onTextClick(title: String, level: Level)
    fun onChangeSortCriteriaClick()
    fun onLevelSelected(title: String, level: Level)
    fun onSettingsClick()
}

interface HomeInteractor {
    fun initialize(listener: (List<TextRealm>) -> Unit)
    fun destroy()
    fun showTutorial(): Boolean
    fun getTextContent(title: String): String
    fun getSortCriteria(): SortCriteria
    fun setSortCriteria(sortCriteria: SortCriteria)
    fun deleteText(title: String, callback: (TextRealm) -> Unit)
    fun undoDeleteText(text: TextRealm)
    fun changeTextLevel(title: String, level: Level)
}