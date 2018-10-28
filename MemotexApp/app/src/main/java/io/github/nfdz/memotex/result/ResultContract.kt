package io.github.nfdz.memotex.result

import io.github.nfdz.memotex.common.Level

interface ResultView {
    fun showResults(title: String, level: Level, percentage: Int, textSolution: CharSequence)
    fun askLevel(level: Level)
    fun shareResults(textToShare: String)
    fun navigateToAnotherExercise(title: String, content: String, level: Level)
    fun navigateToHome()
}

interface ResultPresenter {
    fun onCreate(title: String, content: String, level: Level, percentage: Int, textSolution: CharSequence)
    fun onDestroy()
    fun onAnotherExerciseClick()
    fun onChangeLevelClick()
    fun onShareResultsClick()
    fun onExitExerciseClick()
    fun onLevelSelected(level: Level)
}

interface ResultInteractor {
    fun getTextToShare(title: String, level: Level, percentage: Int, callback: (String) -> Unit)
    fun changeTextLevel(title: String, level: Level, callback: () -> Unit)
}