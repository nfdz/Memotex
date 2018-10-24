package io.github.nfdz.memotext.home

import io.github.nfdz.memotext.common.Level
import io.github.nfdz.memotext.common.SortCriteria
import io.github.nfdz.memotext.common.Text

interface HomeView {
    fun setContent(texts: List<Text>)
    fun showDeletedTextMessage(text: Text)
    fun showLevelText(level: Level)
    fun askSortCriteria()
    fun navigateToAddText()
    fun navigateToEditText(text: Text)
    fun navigateToExercise(text: Text)
    fun navigateToSettings()
}

interface HomePresenter {
    fun onCreate()
    fun onDestroy()
    fun onSortCriteriaSelected(sortCriteria: SortCriteria)
    fun onAddTextClick()
    fun onEditTextClick(text: Text)
    fun onDeleteTextClick(text: Text)
    fun onUndoDeleteTextClick(text: Text)
    fun onLevelIconClick(text: Text)
    fun onTextClick(text: Text)
    fun onChangeSortCriteriaClick()
    fun onSettingsClick()
}

interface HomeInteractor {
    fun loadTexts(criteria: SortCriteria, callback: (List<Text>) -> Unit)
    fun deleteText(text: Text, callback: (Text) -> Unit)
    fun undoDeleteText(text: Text, callback: () -> Unit)
}