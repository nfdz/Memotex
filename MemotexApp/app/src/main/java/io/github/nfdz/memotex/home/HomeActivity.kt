package io.github.nfdz.memotex.home

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.github.nfdz.memotex.R
import io.github.nfdz.memotex.common.*
import io.github.nfdz.memotex.editor.startAddTextActivity
import io.github.nfdz.memotex.editor.startEditTextActivity
import io.github.nfdz.memotex.exercise.startExerciseActivity
import io.github.nfdz.memotex.settings.startSettingsActivity
import io.github.nfdz.memotex.tutorial.startTutorialActivity
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), HomeView, AdapterListener {

    private val presenter: HomePresenter by lazy { HomePresenterImpl(this, HomeInteractorImpl(this)) }
    private val adapter = TextsAdapter(listener = this)

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
//        home_av_banner.loadAd(AdRequest.Builder().build())
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

    override fun setContent(texts: List<TextRealm>) {
        adapter.data = texts.map {
            AdapterEntryData(it.title, it.getLevel(), it.percentage)
        }
    }

    override fun showDeletedMessage(text: TextRealm) {
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
            logAnalytics("CHANGE_SORT_CRITERIA")
            presenter.onSortCriteriaSelected(when(which) {
                1 -> SortCriteria.LEVEL
                2 -> SortCriteria.PERCENTAGE
                3 -> SortCriteria.DATE
                else -> SortCriteria.TITLE
            })
            dialog.dismiss()
        }.show()
    }

    override fun askLevel(title: String, level: Level) {
        showAskLevelDialog(level) {
            logAnalytics("HOME_CHANGE_LEVEL")
            presenter.onLevelSelected(title, it)
        }
    }

    override fun navigateToSettings() {
        startSettingsActivity()
    }

    override fun navigateToAddText() {
        startAddTextActivity()
    }

    override fun navigateToEditText(title: String, content: String) {
        startEditTextActivity(title, content)
    }

    override fun navigateToExercise(title: String, content: String, level: Level) {
        startExerciseActivity(title, content, level)
    }

    override fun navigateToTutorial() {
        startTutorialActivity()
    }

    override fun onTextClick(entry: AdapterEntryData) {
        presenter.onTextClick(entry.title, entry.level)
    }

    override fun onLevelIconClick(entry: AdapterEntryData) {
        presenter.onLevelIconClick(entry.title, entry.level)
    }

    override fun onEditTextClick(entry: AdapterEntryData) {
        presenter.onEditTextClick(entry.title)
    }

    override fun onDeleteClick(entry: AdapterEntryData) {
        presenter.onDeleteTextClick(entry.title)
    }

}
