package io.github.nfdz.memotex.common

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class TextRealm(@PrimaryKey var title: String = "",
                     var content: String = "",
                     var levelString: String = Level.EASY.name,
                     var percentage: Int = 0,
                     var timestamp: Long = 0) : RealmObject() {

    fun getLevel(): Level {
        return Level.valueOf(levelString)
    }

}

enum class Level { EASY, MEDIUM, HARD }

enum class SortCriteria { TITLE, LEVEL, PERCENTAGE, DATE }