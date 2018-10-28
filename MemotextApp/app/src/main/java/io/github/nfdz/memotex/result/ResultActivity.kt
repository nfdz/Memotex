package io.github.nfdz.memotex.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import io.github.nfdz.memotex.R
import io.github.nfdz.memotex.common.Level
import io.github.nfdz.memotex.common.getStringExtra
import io.github.nfdz.memotex.common.showAskLevelDialog
import io.github.nfdz.memotex.common.showSnackbar
import io.github.nfdz.memotex.exercise.startExerciseActivity
import kotlinx.android.synthetic.main.activity_result.*
import timber.log.Timber

fun Context.startResultActivity(title: String, content: String, level: Level, percentage: Int, textSolution: CharSequence) {
    val starter = Intent(this, ResultActivity::class.java).apply {
        putExtra(EXTRA_RESULT_TITLE, title)
        putExtra(EXTRA_RESULT_CONTENT, content)
        putExtra(EXTRA_RESULT_LEVEL, level.name)
        putExtra(EXTRA_RESULT_PERCENTAGE, percentage)
        putExtra(EXTRA_RESULT_SOLUTION, textSolution)
    }
    startActivity(starter)
}

private val EXTRA_RESULT_TITLE = "result_title"
private val EXTRA_RESULT_CONTENT = "result_content"
private val EXTRA_RESULT_LEVEL = "result_level"
private val EXTRA_RESULT_PERCENTAGE = "result_percentage"
private val EXTRA_RESULT_SOLUTION = "result_solution"

class ResultActivity : AppCompatActivity(), ResultView {

    private val presenter: ResultPresenter by lazy { ResultPresenterImpl(this, ResultInteractorImpl(this)) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        presenter.onCreate(intent.getStringExtra(EXTRA_RESULT_TITLE, ""),
            intent.getStringExtra(EXTRA_RESULT_CONTENT, ""),
            Level.valueOf(intent.getStringExtra(EXTRA_RESULT_LEVEL,  Level.EASY.name)),
            intent.getIntExtra(EXTRA_RESULT_PERCENTAGE, 0),
            intent.getCharSequenceExtra(EXTRA_RESULT_SOLUTION) ?: "")
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private fun setupView() {
        setContentView(R.layout.activity_result)
        result_fab_again.setOnClickListener { presenter.onAnotherExerciseClick() }
        result_fab_level.setOnClickListener { presenter.onChangeLevelClick() }
        result_fab_share.setOnClickListener { presenter.onShareResultsClick() }
        result_iv_exit.setOnClickListener { presenter.onExitExerciseClick() }
    }

    override fun onBackPressed() {
        NavUtils.navigateUpFromSameTask(this)
    }

    override fun showResults(title: String, level: Level, percentage: Int, textSolution: CharSequence) {
        result_tv_title.text = title
        result_tv_solution.text = textSolution
        result_tv_percentage.text = "${percentage.toString()}%"
        result_tv_level.text = when (level) {
            Level.EASY -> getString(R.string.level_easy)
            Level.MEDIUM -> getString(R.string.level_medium)
            Level.HARD -> getString(R.string.level_hard)
        }
    }

    override fun askLevel(level: Level) {
        showAskLevelDialog(level) { presenter.onLevelSelected(it) }
    }

    override fun shareResults(textToShare: String) {
        try {
            val sendIntent: Intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, textToShare)
                type = "text/plain"
            }
            startActivity(sendIntent)
        } catch (e: Exception) {
            Timber.e(e)
            coordinator_root.showSnackbar(getString(R.string.result_share_error))
        }
    }

    override fun navigateToAnotherExercise(title: String, content: String, level: Level) {
        startExerciseActivity(title, content, level, Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }

    override fun navigateToHome() {
        onBackPressed()
    }

}
