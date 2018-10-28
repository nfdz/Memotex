package io.github.nfdz.memotext.exercise

import io.github.nfdz.memotext.common.Exercise
import io.github.nfdz.memotext.common.ExerciseAnswers
import io.github.nfdz.memotext.common.Level
import io.github.nfdz.memotext.common.bound

class ExercisePresenterImpl(var view: ExerciseView?, var interactor: ExerciseInteractor?) : ExercisePresenter {

    var title = ""
    var content = ""
    var level = Level.EASY

    override fun onCreate(title: String, content: String, level: Level, exercise: Exercise?) {
        this.title = title
        this.content = content
        this.level = level
        view?.showLoading()
        if (exercise != null) {
            view?.showExercise(title, exercise)
        } else {
            interactor?.prepareExercise(content, level, {
                view?.showExercise(title, it)
            }, {
                view?.navigateToError()
            })
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
        view?.setExerciseProgress(progress.bound(0, 100))
    }

    override fun onCheckExerciseClick(exercise: Exercise, answers: ExerciseAnswers) {
        view?.showLoading()
        interactor?.checkAnswers(title, content, level, exercise, answers) {
            view?.navigateToResult(it)
        }
    }

}