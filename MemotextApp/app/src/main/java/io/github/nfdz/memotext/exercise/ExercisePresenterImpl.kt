package io.github.nfdz.memotext.exercise

import io.github.nfdz.memotext.common.Exercise
import io.github.nfdz.memotext.common.Level

class ExercisePresenterImpl(var view: ExerciseView?, var interactor: ExerciseInteractor?) : ExercisePresenter {

    var title = ""
    var level = Level.BRONZE

    override fun onCreate(title: String, content: String, level: Level, exercise: Exercise?) {
        this.title = title
        this.level = level
        view?.showLoading()
        if (exercise != null) {
            view?.showExercise(title, exercise)
        } else {
            interactor?.prepareExercise(content, level) {
                view?.showExercise(title, it)
            }
        }
    }

    override fun onDestroy() {
        view = null
        interactor = null
    }

    override fun onChangeAnswerClick(position: Int, currentAnswer: String) {
        view?.showChangeAnswerDialog(position, currentAnswer)
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