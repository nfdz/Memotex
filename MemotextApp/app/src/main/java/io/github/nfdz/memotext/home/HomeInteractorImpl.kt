package io.github.nfdz.memotext.home

import io.github.nfdz.memotext.common.Level
import io.github.nfdz.memotext.common.SortCriteria
import io.github.nfdz.memotext.common.Text

class HomeInteractorImpl : HomeInteractor {

    override fun deleteText(text: Text, callback: (Text) -> Unit) {
        // TODO
        callback(text)
    }

    override fun undoDeleteText(text: Text, callback: () -> Unit) {
        // TODO
        callback()
    }

    override fun loadTexts(criteria: SortCriteria, callback: (List<Text>) -> Unit) {
        callback((1..90).flatMap { listOf(Text("Test title $it", "Lorem ipsum", Level.SILVER, 10, 0L))} )
    }

}