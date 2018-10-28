package io.github.nfdz.memotex.common

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

class TextRealm(@PrimaryKey var title: String, var content: String, var level: Int, var percentage: Int, var timestamp: Long) : RealmObject()

data class Text(val title: String, val content: String, val level: Level, val percentage: Int, val timestamp: Long)

enum class Level { EASY, MEDIUM, HARD }

enum class SortCriteria { TITLE, LEVEL, PERCENTAGE, DATE }