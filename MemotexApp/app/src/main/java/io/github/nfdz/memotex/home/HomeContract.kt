package io.github.nfdz.memotex.home

import io.github.nfdz.memotex.common.Level
import io.github.nfdz.memotex.common.SortCriteria
import io.github.nfdz.memotex.common.Text

interface HomeView {
    fun setContent(texts: List<Text>)
    fun showDeletedMessage(text: Text)
    fun askSortCriteria(currentSortCriteria: SortCriteria)
    fun askLevel(text: Text)
    fun navigateToAddText()
    fun navigateToEditText(text: Text)
    fun navigateToExercise(text: Text)
    fun navigateToSettings()
}

interface HomePresenter {
    fun onCreate()
    fun onDestroy()
    fun onEditorFinish()
    fun onSortCriteriaSelected(sortCriteria: SortCriteria)
    fun onAddTextClick()
    fun onEditTextClick(text: Text)
    fun onDeleteTextClick(text: Text)
    fun onUndoDeleteTextClick(text: Text)
    fun onLevelIconClick(text: Text)
    fun onTextClick(text: Text)
    fun onChangeSortCriteriaClick()
    fun onLevelSelected(text: Text, level: Level)
    fun onSettingsClick()
}

interface HomeInteractor {
    fun getSortCriteria(): SortCriteria
    fun setSortCriteria(sortCriteria: SortCriteria, callback: () -> Unit)
    fun loadTexts(forceUpdateCache: Boolean, callback: (List<Text>) -> Unit)
    fun deleteText(text: Text, callback: (Text) -> Unit)
    fun undoDeleteText(text: Text, callback: () -> Unit)
    fun changeTextLevel(text: Text, level: Level, callback: () -> Unit)
}