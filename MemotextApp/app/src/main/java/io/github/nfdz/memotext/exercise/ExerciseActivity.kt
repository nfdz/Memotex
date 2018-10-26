package io.github.nfdz.memotext.exercise

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.Level
import io.github.nfdz.memotext.common.Text
import io.github.nfdz.memotext.common.getStringExtra
import kotlinx.android.synthetic.main.activity_exercise.*

fun Context.startExerciseActivity(text: Text) {
    val starter = Intent(this, ExerciseActivity::class.java)
    starter.putExtra(EXTRA_TEXT_TITLE, text.title)
    starter.putExtra(EXTRA_TEXT_CONTENT, text.content)
    starter.putExtra(EXTRA_TEXT_LEVEL, text.level.name)
    startActivity(starter)
}

private val EXTRA_TEXT_TITLE = "text_title"
private val EXTRA_TEXT_CONTENT = "text_content"
private val EXTRA_TEXT_LEVEL = "text_level"

class ExerciseActivity : AppCompatActivity(), ExerciseView {

    val presenter: ExercisePresenter by lazy { ExercisePresenterImpl(this, ExerciseInteractorImpl()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        presenter.onCreate(intent.getStringExtra(EXTRA_TEXT_TITLE, ""),
            intent.getStringExtra(EXTRA_TEXT_CONTENT, ""),
            Level.valueOf(intent.getStringExtra(EXTRA_TEXT_LEVEL, Level.BRONZE.name)))
    }

    private fun setupView() {
        setContentView(R.layout.activity_exercise)
        exercise_iv_font_big.setOnClickListener { presenter.onIncreaseFontSizeClick() }
        exercise_iv_font_small.setOnClickListener { presenter.onDecreaseFontSizeClick() }
        exercise_fab_check.setOnClickListener { presenter.onCheckExerciseClick() }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showLoading() {
        exercise_loading.visibility = View.VISIBLE
        exercise_group_ongoing.visibility = View.GONE
    }

    override fun showExercise(title: String) {
        exercise_loading.visibility = View.GONE
        exercise_group_ongoing.visibility = View.VISIBLE
        exercise_tv_title.text = title
    }

    override fun setExerciseProgress(progress: Int) {
        exercise_tv_progress.text = "${progress.toString()}%"
        exercise_pb.progress = progress
    }

    override fun increaseFontSize() {
        // TODO
    }

    override fun decreaseFontSize() {
        // TODO
    }

    override fun navigateToResult() {
        // TODO
    }

}
