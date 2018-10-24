package io.github.nfdz.memotext.home

import io.github.nfdz.memotext.common.SortCriteria
import io.github.nfdz.memotext.common.Text

class HomePresenterImpl(var view: HomeView?, var interactor: HomeInteractor?) : HomePresenter {

    private val DEFAULT_SORT_CRITERIA = SortCriteria.TITLE

    private var sortCriteria: SortCriteria = DEFAULT_SORT_CRITERIA

    override fun onCreate() {
        loadTexts()
    }

    private fun loadTexts() {
        interactor?.loadTexts(sortCriteria) {
            view?.setContent(it)
        }
    }

    override fun onDestroy() {
        view = null
        interactor = null
    }

    override fun onSortCriteriaSelected(sortCriteria: SortCriteria) {
        this.sortCriteria = sortCriteria
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
        view?.askSortCriteria()
    }

    override fun onSettingsClick() {
        view?.navigateToSettings()
    }

}