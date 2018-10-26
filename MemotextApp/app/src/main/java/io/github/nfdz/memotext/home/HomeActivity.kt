package io.github.nfdz.memotext.home

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.*
import io.github.nfdz.memotext.editor.startAddTextActivity
import io.github.nfdz.memotext.editor.startEditTextActivity
import io.github.nfdz.memotext.settings.startSettingsActivity
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), HomeView, AdapterListener {

    val presenter: HomePresenter by lazy { HomePresenterImpl(this, HomeInteractorImpl(this)) }
    val adapter = TextsAdapter(listener = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        presenter.onCreate()
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

    override fun showDeletedTextMessage(text: Text) {
        coordinator_root.showSnackbarWithAction(getString(R.string.text_deleted),
            getString(R.string.text_deleted_action),
            Snackbar.LENGTH_LONG,
            View.OnClickListener {
                presenter.onUndoDeleteTextClick(text)
            })
    }

    override fun showLevelText(level: Level) {
        coordinator_root.showSnackbar(when(level) {
            Level.BRONZE -> getString(R.string.level_bronze_msg)
            Level.SILVER -> getString(R.string.level_silver_msg)
            Level.GOLD -> getString(R.string.level_gold_msg)
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
            title = getString(R.string.action_sort)
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

    override fun navigateToSettings() {
        startSettingsActivity()
    }

    override fun navigateToAddText() {
        startAddTextActivity()
    }

    override fun navigateToEditText(text: Text) {
        startEditTextActivity(text)
    }

    override fun navigateToExercise(text: Text) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
