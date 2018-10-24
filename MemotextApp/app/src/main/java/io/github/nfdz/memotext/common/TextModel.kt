package io.github.nfdz.memotext.common

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

class TextRealm(@PrimaryKey var title: String, var text: String, var level: Int, var percentage: Int, var timestamp: Long) : RealmObject()

data class Text(val title: String, val text: String, val level: Level, val percentage: Int, val timestamp: Long) {
    fun getPercentageBounded(): Int {
        return Math.max(0, Math.min(100, percentage))
    }
}

enum class Level(val levelNumber: Int) { BRONZE(0), SILVER(10), GOLD(20) }

enum class SortCriteria { TITLE, LEVEL, PERCENTAGE, DATE }