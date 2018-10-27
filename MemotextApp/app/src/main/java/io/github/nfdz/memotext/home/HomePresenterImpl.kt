package io.github.nfdz.memotext.home

import io.github.nfdz.memotext.common.Level
import io.github.nfdz.memotext.common.SortCriteria
import io.github.nfdz.memotext.common.Text

class HomePresenterImpl(var view: HomeView?, var interactor: HomeInteractor?) : HomePresenter {

    override fun onCreate() {
        loadTexts(false)
    }

    private fun loadTexts(forceUpdateCache: Boolean) {
        interactor?.loadTexts(forceUpdateCache) {
            view?.setContent(it)
        }
    }

    override fun onDestroy() {
        view = null
        interactor = null
    }

    override fun onEditorFinish() {
        loadTexts(true)
    }

    override fun onSortCriteriaSelected(sortCriteria: SortCriteria) {
        interactor?.setSortCriteria(sortCriteria) {
            loadTexts(false)
        }
    }

    override fun onAddTextClick() {
        view?.navigateToAddText()
    }

    override fun onEditTextClick(text: Text) {
        view?.navigateToEditText(text)
    }

    override fun onDeleteTextClick(text: Text) {
        interactor?.deleteText(text) {
            view?.showDeletedMessage(text)
            loadTexts(false)
        }
    }

    override fun onUndoDeleteTextClick(text: Text) {
        interactor?.undoDeleteText(text) {
            loadTexts(false)
        }
    }

    override fun onLevelIconClick(text: Text) {
        view?.askLevel(text)
    }

    override fun onTextClick(text: Text) {
        view?.navigateToExercise(text)
    }

    override fun onChangeSortCriteriaClick() {
        interactor?.let {
            view?.askSortCriteria(it.getSortCriteria())
        }
    }

    override fun onLevelSelected(text: Text, level: Level) {
        interactor?.changeTextLevel(text, level) {
            loadTexts(false)
        }
    }

    override fun onSettingsClick() {
        view?.navigateToSettings()
    }

}