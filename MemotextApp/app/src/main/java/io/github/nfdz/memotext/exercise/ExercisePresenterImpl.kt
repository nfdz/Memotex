package io.github.nfdz.memotext.exercise

import android.os.Handler
import io.github.nfdz.memotext.common.Level

class ExercisePresenterImpl(var view: ExerciseView?, var interactor: ExerciseInteractor?) : ExercisePresenter {

    override fun onCreate(title: String, content: String, level: Level) {
        view?.showLoading()
        Handler().postDelayed({
            view?.showExercise(title)
        }, 5000)
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