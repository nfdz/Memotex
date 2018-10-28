package io.github.nfdz.memotex.common

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class TextRealm(@PrimaryKey var title: String? = "",
                     var content: String? = "",
                     var level: String = Level.EASY.name,
                     var percentage: Int = 0,
                     var timestamp: Long = 0) : RealmObject() {



}


data class Text(val title: String, val content: String, val level: Level, val percentage: Int, val timestamp: Long)

enum class Level { EASY, MEDIUM, HARD }

enum class SortCriteria { TITLE, LEVEL, PERCENTAGE, DATE }