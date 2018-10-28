package io.github.nfdz.memotex.editor

class EditorInteractorImpl : EditorInteractor {

    override fun addText(title: String, content: String, successCallback: () -> Unit, errorCallback: (titleConflict: Boolean) -> Unit) {
        // TODO
        successCallback()
    }

    override fun editText(title: String, content: String, successCallback: () -> Unit, errorCallback: () -> Unit) {
        // TODO
        successCallback()
    }

}