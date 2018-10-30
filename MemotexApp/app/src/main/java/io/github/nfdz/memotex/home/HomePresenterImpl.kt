package io.github.nfdz.memotex.home

import io.github.nfdz.memotex.common.Level
import io.github.nfdz.memotex.common.SortCriteria
import io.github.nfdz.memotex.common.TextRealm

class HomePresenterImpl(var view: HomeView?, var interactor: HomeInteractor?) : HomePresenter {

    override fun onCreate() {
        interactor?.initialize {
            view?.setContent(it)
        }
        if (true == interactor?.showTutorial()) {
            view?.navigateToTutorial()
        }
    }

    override fun onDestroy() {
        view = null
        interactor?.destroy()
        interactor = null
    }

    override fun onSortCriteriaSelected(sortCriteria: SortCriteria) {
        interactor?.setSortCriteria(sortCriteria)
    }

    override fun onAddTextClick() {
        view?.navigateToAddText()
    }

    override fun onEditTextClick(title: String) {
        interactor?.let {
            view?.navigateToEditText(title, it.getTextContent(title))
        }
    }

    override fun onDeleteTextClick(title: String) {
        interactor?.deleteText(title) {
            view?.showDeletedMessage(it)
        }
    }

    override fun onUndoDeleteTextClick(text: TextRealm) {
        interactor?.undoDeleteText(text)
    }

    override fun onLevelIconClick(title: String, level: Level) {
        view?.askLevel(title, level)
    }

    override fun onTextClick(title: String, level: Level) {
        interactor?.let {
            view?.navigateToExercise(title, it.getTextContent(title), level)
        }
    }

    override fun onChangeSortCriteriaClick() {
        interactor?.let {
            view?.askSortCriteria(it.getSortCriteria())
        }
    }

    override fun onLevelSelected(title: String, level: Level) {
        interactor?.changeTextLevel(title, level)
    }

    override fun onSettingsClick() {
        view?.navigateToSettings()
    }

}