package io.github.nfdz.memotext.home

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.Level
import io.github.nfdz.memotext.common.Text
import io.github.nfdz.memotext.common.showSnackbar
import io.github.nfdz.memotext.common.showSnackbarWithAction
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), HomeView, TextsAdapter.Listener {

    val presenter: HomePresenter by lazy { HomePresenterImpl(this, HomeInteractorImpl()) }
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

    override fun askSortCriteria() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToSettings() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToAddText() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToEditText(text: Text) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

}
