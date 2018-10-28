package io.github.nfdz.memotex.result

import android.content.Context
import io.github.nfdz.memotex.R
import io.github.nfdz.memotex.common.Level

class ResultInteractorImpl(val context: Context) : ResultInteractor {

    override fun getTextToShare(title: String, level: Level, percentage: Int, callback: (String) -> Unit) {
        val levelText: String = when(level) {
            Level.EASY -> { context.getString(R.string.level_easy) }
            Level.MEDIUM -> { context.getString(R.string.level_medium) }
            Level.HARD -> { context.getString(R.string.level_hard) }
        }
        callback(context.getString(R.string.result_share_template, title, percentage, levelText))
    }

    override fun changeTextLevel(title: String, level: Level, callback: () -> Unit) {
        // TODO
    }

}