package io.github.nfdz.memotext.exercise

import io.github.nfdz.memotext.common.Level

class ExercisePresenterImpl(var view: ExerciseView?, var interactor: ExerciseInteractor?) : ExercisePresenter {

    var title = ""
    var level = Level.BRONZE

    override fun onCreate(title: String, content: String, level: Level) {
        this.title = title
        this.level = level
        view?.showLoading()
        interactor?.prepareExercise(content, level) {
            view?.showExercise(title, it)
        }
    }

    override fun onDestroy() {
        view = null
        interactor = null
    }

    override fun onIncreaseFontSizeClick() {
        view?.increaseFontSize()
    }

    override fun onDecreaseFontSizeClick() {
        view?.decreaseFontSize()
    }

    override fun onProgressChanged(progress: Int) {
        view?.setExerciseProgress(progress)
    }

    override fun onCheckExerciseClick() {
        // TODO
    }


}