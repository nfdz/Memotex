package io.github.nfdz.memotext.exercise

import io.github.nfdz.memotext.common.Exercise
import io.github.nfdz.memotext.common.Level

interface ExerciseView {
    fun showLoading()
    fun showExercise(title: String, exercise: Exercise)
    fun setExerciseProgress(progress: Int)
    fun showChangeAnswerDialog(position: Int, currentAnswer: String)
    fun increaseFontSize()
    fun decreaseFontSize()
    fun navigateToResult()
}

interface ExercisePresenter {
    fun onCreate(title: String, content: String, level: Level, exercise: Exercise?)
    fun onDestroy()
    fun onChangeAnswerClick(position: Int, currentAnswer: String)
    fun onIncreaseFontSizeClick()
    fun onDecreaseFontSizeClick()
    fun onProgressChanged(progress: Int)
    fun onCheckExerciseClick()
}

interface ExerciseInteractor {
    fun prepareExercise(content: String, level: Level, callback: (exercise: Exercise) -> Unit)
}