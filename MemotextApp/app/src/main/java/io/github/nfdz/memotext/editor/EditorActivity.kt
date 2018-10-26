package io.github.nfdz.memotext.editor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.Text

fun Context.startAddTextActivity() {
    val starter = Intent(this, EditorActivity::class.java)
    starter.action = ACTION_ADD
    startActivity(starter)
}

fun Context.startEditTextActivity(text: Text) {
    val starter = Intent(this, EditorActivity::class.java)
    starter.action = ACTION_EDIT
    starter.putExtra(EXTRA_TEXT_TITLE, text.title)
    starter.putExtra(EXTRA_TEXT_CONTENT, text.content)
    startActivity(starter)
}

private val ACTION_ADD = "add_text"
private val ACTION_EDIT = "edit_text"
private val EXTRA_TEXT_TITLE = "text_title"
private val EXTRA_TEXT_CONTENT = "text_content"

class EditorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setContentView(R.layout.activity_editor)
        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (intent.action == ACTION_ADD) getString(R.string.add_text_title) else getString(R.string.edit_text_title)
    }

}
