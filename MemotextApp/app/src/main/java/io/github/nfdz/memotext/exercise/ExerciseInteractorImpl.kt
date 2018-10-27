package io.github.nfdz.memotext.exercise

import android.content.Context
import android.support.v4.text.HtmlCompat
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.*
import timber.log.Timber

class ExerciseInteractorImpl(val context: Context) : ExerciseInteractor {

    override fun prepareExercise(content: String, level: Level, success: (exercise: Exercise) -> Unit, error: () -> Unit) {

        val settings: Pair<String,String> = when (level) {
            Level.BRONZE -> {
                Pair(context.getStringFromPreferences(R.string.pref_lvl_bronze_words_key, R.string.pref_lvl_bronze_words_default),
                    context.getStringFromPreferences(R.string.pref_lvl_bronze_letters_key, R.string.pref_lvl_bronze_letters_default))
            }
            Level.SILVER -> {
                Pair(context.getStringFromPreferences(R.string.pref_lvl_silver_words_key, R.string.pref_lvl_silver_words_default),
                    context.getStringFromPreferences(R.string.pref_lvl_silver_letters_key, R.string.pref_lvl_silver_letters_default))
            }
            Level.GOLD -> {
                Pair(context.getStringFromPreferences(R.string.pref_lvl_gold_words_key, R.string.pref_lvl_gold_words_default),
                    context.getStringFromPreferences(R.string.pref_lvl_gold_letters_key, R.string.pref_lvl_gold_letters_default))
            }
        }

        val wordsToHide = settings.first.toInt()
        val lettersToHide = settings.second.toInt()

        doAsync {
            try {
                val result = ExerciseAlgorithmImpl().execute(content, wordsToHide, lettersToHide)
                doMainThread { success(result) }
            } catch (e: Exception) {
                Timber.e(e, "Cannot generate exercise")
                doMainThread { error() }
            }
        }

    }

    override fun checkAnswers(title: String, level: Level, exercise: Exercise, exerciseAnswers: ExerciseAnswers, callback: (exerciseResult: ExerciseResult) -> Unit) {
        doAsync {
            var slotsCount = 0
            var validAnswers = 0
            val solutionBld = StringBuilder()
            exercise.elements.forEachIndexed { index, element ->
                when(element) {
                    is TextElement -> { solutionBld.append(element.text) }
                    is SpaceElement -> { solutionBld.append(" ") }
                    is SlotElement -> {
                        slotsCount++
                        val answer = exerciseAnswers.answers[index] ?: ""
                        val validAnswer = element.text == answer
                        if (validAnswer) {
                            validAnswers++
                            solutionBld.append("<font color=\"#00FF00\">${element.text}</font>")
                        } else {
                            solutionBld.append("<strike><font color=\"#DC143C\">$answer</font></strike> <font color=\"#00FF00\">${element.text}</font>")
                        }
                    }
                }
            }

            val percentage: Int = Math.ceil(100*validAnswers.toDouble()/slotsCount).toInt().bound(0, 100)
            val textSolution: CharSequence = HtmlCompat.fromHtml(solutionBld.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
            doMainThread {
                callback(ExerciseResult(title, level, percentage, textSolution))
            }
        }
    }

}