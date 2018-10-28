package io.github.nfdz.memotex.editor

import com.vicpin.krealmextensions.create
import com.vicpin.krealmextensions.executeTransaction
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.save
import io.github.nfdz.memotex.common.TextRealm
import io.github.nfdz.memotex.common.doAsync
import io.github.nfdz.memotex.common.doMainThread
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import timber.log.Timber

class EditorInteractorImpl : EditorInteractor {

    override fun addText(title: String, content: String, successCallback: () -> Unit, errorCallback: (titleConflict: Boolean) -> Unit) {
        doAsync {
            try {
                TextRealm(title, content).create()
                doMainThread { successCallback() }
            } catch (e: RealmPrimaryKeyConstraintException) {
                doMainThread { errorCallback(true) }
            } catch (e: Exception) {
                Timber.e(e)
                doMainThread { errorCallback(false) }
            }
        }
    }

    override fun editText(title: String, content: String, successCallback: () -> Unit, errorCallback: () -> Unit) {
        doAsync {
            executeTransaction {
                val text = TextRealm().queryFirst { equalTo("title", title) }
                if (text == null) {
                    doMainThread { errorCallback() }
                } else {
                    text.content = content
                    text.save()
                    doMainThread { successCallback() }
                }
            }
        }
    }

}