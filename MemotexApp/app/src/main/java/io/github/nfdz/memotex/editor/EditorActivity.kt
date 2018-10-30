package io.github.nfdz.memotex.editor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import io.github.nfdz.memotex.R
import io.github.nfdz.memotex.common.getStringExtra
import io.github.nfdz.memotex.common.logAnalytics
import io.github.nfdz.memotex.common.showSnackbar
import kotlinx.android.synthetic.main.activity_editor.*

fun Context.startAddTextActivity() {
    val starter = Intent(this, EditorActivity::class.java).apply {
        action = ACTION_ADD
    }
    startActivity(starter)
}

fun Context.startEditTextActivity(title: String, content: String) {
    val starter = Intent(this, EditorActivity::class.java).apply {
        action = ACTION_EDIT
        putExtra(EXTRA_TEXT_TITLE, title)
        putExtra(EXTRA_TEXT_CONTENT, content)
    }
    startActivity(starter)
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
        presenter.onCreate(ACTION_EDIT == intent.action)
    }

    private fun setupView() {
        setContentView(R.layout.activity_editor)
        setupActionBar()
        if (ACTION_EDIT == intent.action) {
            editor_tie_title.setText(intent.getStringExtra(EXTRA_TEXT_TITLE, ""))
            editor_til_title.hint = getString(R.string.text_title_hint_unmodifiable)
            editor_tie_title.setHint(R.string.text_title_hint_unmodifiable)
            editor_tie_title.inputType = InputType.TYPE_NULL
            editor_tie_title.keyListener = null
            editor_tie_title.isFocusable = false
            editor_tie_title.isCursorVisible = false
            editor_tie_content.setText(intent.getStringExtra(EXTRA_TEXT_CONTENT, ""))
        } else {
            editor_tie_title.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    btn_start_load.isEnabled = !(s?.isEmpty() ?: true)
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            btn_start_load.isEnabled = !(editor_tie_title.text?.isEmpty() ?: true)
        }
        btn_start_load.setOnClickListener { presenter.onSaveClick(editor_tie_title.text.toString(), editor_tie_content.text.toString()) }
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (ACTION_ADD == intent.action) getString(R.string.add_text_title) else getString(R.string.edit_text_title)
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
        logAnalytics( if (ACTION_EDIT == intent.action) "EDIT_TEXT" else "ADD_TEXT" )
        setResult(Activity.RESULT_OK)
        finish()
    }

}
