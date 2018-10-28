package io.github.nfdz.memotext.result

import android.content.Context
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.Level

class ResultInteractorImpl(val context: Context) : ResultInteractor {

    override fun getTextToShare(title: String, level: Level, percentage: Int, callback: (String) -> Unit) {
        val levelText: String = when(level) {
            Level.BRONZE -> { context.getString(R.string.level_bronze) }
            Level.SILVER -> { context.getString(R.string.level_silver) }
            Level.GOLD -> { context.getString(R.string.level_gold) }
        }
        callback(context.getString(R.string.result_share_template, title, percentage, levelText))
    }

    override fun changeTextLevel(title: String, level: Level, callback: () -> Unit) {
        // TODO
    }

}