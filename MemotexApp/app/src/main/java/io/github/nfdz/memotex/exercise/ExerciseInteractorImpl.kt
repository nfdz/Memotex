package io.github.nfdz.memotex.exercise

import android.content.Context
import androidx.core.text.HtmlCompat
import com.vicpin.krealmextensions.executeTransaction
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.save
import io.github.nfdz.memotex.R
import io.github.nfdz.memotex.common.*

class ExerciseInteractorImpl(val context: Context) : ExerciseInteractor {

    override fun prepareExercise(
        content: String,
        level: Level,
        success: (exercise: Exercise) -> Unit,
        error: () -> Unit
    ) {

        val wordsToHide = when (level) {
            Level.EASY -> {
                context.getStringFromPreferences(R.string.pref_lvl_easy_words_key, R.string.pref_lvl_easy_words_default)
            }
            Level.MEDIUM -> {
                context.getStringFromPreferences(R.string.pref_lvl_medium_words_key, R.string.pref_lvl_medium_words_default)
            }
            Level.HARD -> {
                context.getStringFromPreferences(R.string.pref_lvl_hard_words_key, R.string.pref_lvl_hard_words_default)
            }
        }.toInt()

        doAsync {
            try {
                val result = ExerciseAlgorithmImpl().execute(content, wordsToHide)
                doMainThread { success(result) }
            } catch (e: Exception) {
                reportException(Exception("Cannot generate an exercise", e))
                doMainThread { error() }
            }
        }

    }

    override fun checkAnswers(
        title: String,
        content: String,
        level: Level,
        exercise: Exercise,
        exerciseAnswers: ExerciseAnswers,
        callback: (exerciseResult: ExerciseResult) -> Unit
    ) {
        doAsync {
            var slotsCount = 0
            var validAnswers = 0
            val solutionBld = StringBuilder()
            exercise.elements.forEachIndexed { index, element ->
                when (element) {
                    is TextElement -> {
                        solutionBld.append(element.text.replace("\n", "<br>").replace("\r", "<br>"))
                    }
                    is SpaceElement -> {
                        solutionBld.append(" ")
                    }
                    is SlotElement -> {
                        slotsCount++
                        val answer = exerciseAnswers.answers[index] ?: ""
                        val validAnswer = element.text.equals(answer, ignoreCase = true)
                        if (validAnswer) {
                            validAnswers++
                            solutionBld.append("<b><font color=\"#009900\">${element.text}</font></b>")
                        } else {
                            if (!answer.isEmpty()) {
                                solutionBld.append("<b><strike><font color=\"#CC0000\">$answer</font></strike></b>")
                                solutionBld.append(" ")
                            }
                            solutionBld.append("<b><u>${element.text}</u></b>")
                        }
                    }
                }
            }
            val percentage: Int = Math.ceil(100 * validAnswers.toDouble() / slotsCount).toInt().bound(0, 100)
            val textSolution: CharSequence =
                HtmlCompat.fromHtml(solutionBld.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)

            // Save
            executeTransaction { _ ->
                TextRealm().queryFirst { equalTo("title", title) }?.let {
                    it.percentage = percentage
                    it.timestamp = System.currentTimeMillis()
                    it.save()
                }
            }

            // Notify
            doMainThread {
                callback(ExerciseResult(title, content, level, percentage, textSolution))
            }
        }
    }

}