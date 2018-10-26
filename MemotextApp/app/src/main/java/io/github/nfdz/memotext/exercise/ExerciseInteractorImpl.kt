package io.github.nfdz.memotext.exercise

import android.content.Context
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.*

class ExerciseInteractorImpl(val context: Context) : ExerciseInteractor {

    override fun prepareExercise(content: String, level: Level, callback: (exercise: Exercise) -> Unit) {

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
            val result = ExerciseAlgorithmImpl().execute(content, wordsToHide, lettersToHide)
            doMainThread {
                callback(result)
            }
        }

    }

}