package io.github.nfdz.memotext.editor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.Text
import io.github.nfdz.memotext.common.getStringExtra
import io.github.nfdz.memotext.common.showSnackbar
import kotlinx.android.synthetic.main.activity_editor.*

fun Activity.startAddTextActivity(requestCode: Int) {
    val starter = Intent(this, EditorActivity::class.java).apply {
        action = ACTION_ADD
    }
    ActivityCompat.startActivityForResult(this, starter, requestCode, null)
}

fun Activity.startEditTextActivity(requestCode: Int, text: Text) {
    val starter = Intent(this, EditorActivity::class.java).apply {
        action = ACTION_EDIT
        putExtra(EXTRA_TEXT_TITLE, text.title)
        putExtra(EXTRA_TEXT_CONTENT, text.content)
    }
    ActivityCompat.startActivityForResult(this, starter, requestCode, null)
}

private val ACTION_ADD = "add_text"
private val ACTION_EDIT = "edit_text"
private val EXTRA_TEXT_TITLE = "text_title"
private val EXTRA_TEXT_CONTENT = "text_content"

class EditorActivity : AppCompatActivity(), EditorView {

    private val presenter: EditorPresenter by lazy { EditorPresenterImpl(this, EditorInteractorImpl()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        presenter.onCreate(intent.action == ACTION_EDIT)
    }

    private fun setupView() {
        setContentView(R.layout.activity_editor)
        setupActionBar()
        if (intent.action == ACTION_EDIT) {
            editor_tie_title.setText(intent.getStringExtra(EXTRA_TEXT_TITLE, ""))
            editor_til_title.hint = getString(R.string.text_title_hint_unmodifiable)
            editor_tie_title.setHint(R.string.text_title_hint_unmodifiable)
            editor_tie_title.inputType = InputType.TYPE_NULL
            editor_tie_title.keyListener = null
            editor_tie_title.isFocusable = false
            editor_tie_title.isCursorVisible = false
            editor_tie_content.setText(intent.getStringExtra(EXTRA_TEXT_CONTENT, ""))
        }
        btn_start_load.setOnClickListener { presenter.onSaveClick(editor_tie_title.text.toString(), editor_tie_content.text.toString()) }
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (intent.action == ACTION_ADD) getString(R.string.add_text_title) else getString(R.string.edit_text_title)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showTitleConflictErrorMsg() {
        editor_root.showSnackbar(getString(R.string.editor_save_title_error))
    }

    override fun showSaveErrorMsg() {
        editor_root.showSnackbar(getString(R.string.editor_save_error))
    }

    override fun navigateToFinish() {
        setResult(Activity.RESULT_OK)
        finish()
    }

}
