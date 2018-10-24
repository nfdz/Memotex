package io.github.nfdz.memotext.home

import io.github.nfdz.memotext.common.SortCriteria
import io.github.nfdz.memotext.common.Text

class HomePresenterImpl(var view: HomeView?, var interactor: HomeInteractor?) : HomePresenter {

    override fun onCreate() {
        loadTexts()
    }

    private fun loadTexts() {
        interactor?.loadTexts {
            view?.setContent(it)
        }
    }

    override fun onDestroy() {
        view = null
        interactor = null
    }

    override fun onSortCriteriaSelected(sortCriteria: SortCriteria) {
        interactor?.saveSortCriteria(sortCriteria)
        loadTexts()
    }

    override fun onAddTextClick() {
        view?.navigateToAddText()
    }

    override fun onEditTextClick(text: Text) {
        view?.navigateToEditText(text)
    }

    override fun onDeleteTextClick(text: Text) {
        interactor?.deleteText(text) {
            view?.showDeletedTextMessage(text)
            loadTexts()
        }
    }

    override fun onUndoDeleteTextClick(text: Text) {
        interactor?.undoDeleteText(text) {
            loadTexts()
        }
    }

    override fun onLevelIconClick(text: Text) {
        view?.showLevelText(text.level)
    }

    override fun onTextClick(text: Text) {
        view?.navigateToExercise(text)
    }

    override fun onChangeSortCriteriaClick() {
        interactor?.let {
            view?.askSortCriteria(it.getSortCriteria())
        }
    }

    override fun onSettingsClick() {
        view?.navigateToSettings()
    }

}