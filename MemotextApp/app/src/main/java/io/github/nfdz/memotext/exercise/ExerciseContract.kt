package io.github.nfdz.memotext.exercise

import io.github.nfdz.memotext.common.Level

interface ExerciseView {
    fun showLoading()
    fun showExercise(title: String)
    fun setExerciseProgress(progress: Int)
    fun increaseFontSize()
    fun decreaseFontSize()
    fun navigateToResult()
}

interface ExercisePresenter {
    fun onCreate(title: String, content: String, level: Level)
    fun onDestroy()
    fun onIncreaseFontSizeClick()
    fun onDecreaseFontSizeClick()
    fun onProgressChanged(progress: Int)
    fun onCheckExerciseClick()
}

interface ExerciseInteractor {

}