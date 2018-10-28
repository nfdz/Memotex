package io.github.nfdz.memotex.result

import io.github.nfdz.memotex.common.Level

class ResultPresenterImpl(var view: ResultView?, var interactor: ResultInteractor?) : ResultPresenter {

    var title = ""
    var content = ""
    var level = Level.EASY
    var percentage = 0

    override fun onCreate(title: String, content: String, level: Level, percentage: Int, textSolution: CharSequence) {
        this.title = title
        this.content = content
        this.level = level
        this.percentage = percentage
        view?.showResults(title, level, percentage, textSolution)
    }

    override fun onDestroy() {
        view = null
        interactor = null
    }

    override fun onAnotherExerciseClick() {
        view?.navigateToAnotherExercise(title, content, level)
    }

    override fun onChangeLevelClick() {
        view?.askLevel(level)
    }

    override fun onShareResultsClick() {
        interactor?.getTextToShare(title, level, percentage) {
            view?.shareResults(it)
        }
    }

    override fun onExitExerciseClick() {
        view?.navigateToHome()
    }

    override fun onLevelSelected(level: Level) {
        interactor?.changeTextLevel(title, level) {
            this.level = level
        }
    }

}