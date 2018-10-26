package io.github.nfdz.memotext.editor

class EditorPresenterImpl(var view: EditorView?, var interactor: EditorInteractor?) : EditorPresenter {

    var editionMode: Boolean = false

    override fun onCreate(editionMode: Boolean) {
        this.editionMode = editionMode
    }

    override fun onDestroy() {
        view = null
        interactor = null
    }

    override fun onSaveClick(title: String, content: String) {
        val successCallback = {
            view?.navigateToFinish()
            Unit
        }
        if (editionMode) {
            interactor?.editText(title, content, successCallback) {
                view?.showSaveErrorMsg()
                Unit
            }
        } else {
            interactor?.addText(title, content, successCallback) { titleConflict ->
                if (titleConflict) view?.showTitleConflictErrorMsg()
                else view?.showSaveErrorMsg()
                Unit
            }
        }
    }

}