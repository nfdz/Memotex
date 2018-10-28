package io.github.nfdz.memotex.editor

interface EditorView {
    fun showTitleConflictErrorMsg()
    fun showSaveErrorMsg()
    fun navigateToFinish()
}

interface EditorPresenter {
    fun onCreate(editionMode: Boolean)
    fun onDestroy()
    fun onSaveClick(title: String, content: String)
}

interface EditorInteractor {
    fun addText(title: String, content: String, successCallback: () -> Unit, errorCallback: (titleConflict: Boolean) -> Unit)
    fun editText(title: String, content: String, successCallback: () -> Unit, errorCallback: () -> Unit)
}