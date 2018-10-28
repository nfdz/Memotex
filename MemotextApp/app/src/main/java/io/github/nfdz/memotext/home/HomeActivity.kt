package io.github.nfdz.memotext.home

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.SortCriteria
import io.github.nfdz.memotext.common.Text
import io.github.nfdz.memotext.common.showAskLevelDialog
import io.github.nfdz.memotext.common.showSnackbarWithAction
import io.github.nfdz.memotext.editor.startAddTextActivity
import io.github.nfdz.memotext.editor.startEditTextActivity
import io.github.nfdz.memotext.exercise.startExerciseActivity
import io.github.nfdz.memotext.settings.startSettingsActivity
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), HomeView, AdapterListener {

    private val EDITOR_REQUEST_CODE = 44078

    private val presenter: HomePresenter by lazy { HomePresenterImpl(this, HomeInteractorImpl(this)) }
    private val adapter = TextsAdapter(listener = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        presenter.onCreate()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == EDITOR_REQUEST_CODE) {
            presenter.onEditorFinish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private fun setupView() {
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        rv_texts.adapter = adapter
        ItemTouchHelper(TextSwipeController()).attachToRecyclerView(rv_texts)
        fab_add_text.setOnClickListener { presenter.onAddTextClick() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> { presenter.onSettingsClick(); true }
            R.id.action_sort -> { presenter.onChangeSortCriteriaClick(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setContent(texts: List<Text>) {
        adapter.data = texts
    }

    override fun showDeletedMessage(text: Text) {
        coordinator_root.showSnackbarWithAction(getString(R.string.text_deleted),
            getString(R.string.text_deleted_action),
            Snackbar.LENGTH_LONG,
            View.OnClickListener {
                presenter.onUndoDeleteTextClick(text)
            })
    }

    override fun askSortCriteria(currentSortCriteria: SortCriteria) {
        val options = listOf<String>(getString(R.string.sort_by_title),
            getString(R.string.sort_by_level),
            getString(R.string.sort_by_percentage),
            getString(R.string.sort_by_date))
        val checkedItem = when(currentSortCriteria) {
            SortCriteria.TITLE -> 0
            SortCriteria.LEVEL -> 1
            SortCriteria.PERCENTAGE -> 2
            SortCriteria.DATE -> 3
        }
        AlertDialog.Builder(this).apply {
            setTitle(R.string.action_sort)
        }.setSingleChoiceItems(options.toTypedArray(), checkedItem) { dialog, which ->
            presenter.onSortCriteriaSelected(when(which) {
                1 -> SortCriteria.LEVEL
                2 -> SortCriteria.PERCENTAGE
                3 -> SortCriteria.DATE
                else -> SortCriteria.TITLE
            })
            dialog.dismiss()
        }.show()
    }

    override fun askLevel(text: Text) {
        showAskLevelDialog(text.level) {
            presenter.onLevelSelected(text, it)
        }
    }

    override fun navigateToSettings() {
        startSettingsActivity()
    }

    override fun navigateToAddText() {
        startAddTextActivity(EDITOR_REQUEST_CODE)
    }

    override fun navigateToEditText(text: Text) {
        startEditTextActivity(EDITOR_REQUEST_CODE, text)
    }

    override fun navigateToExercise(text: Text) {
        startExerciseActivity(text.title, text.content, text.level)
    }

    override fun onTextClick(text: Text) {
        presenter.onTextClick(text)
    }

    override fun onLevelIconClick(text: Text) {
        presenter.onLevelIconClick(text)
    }

    override fun onEditTextClick(text: Text) {
        presenter.onEditTextClick(text)
    }

    override fun onDeleteClick(text: Text) {
        presenter.onDeleteTextClick(text)
    }

}
