package io.github.nfdz.memotex.exercise

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NavUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import io.github.nfdz.memotex.R
import io.github.nfdz.memotex.common.*
import io.github.nfdz.memotex.result.startResultActivity
import kotlinx.android.synthetic.main.activity_exercise.*


fun Context.startExerciseActivity(title: String, content: String, level: Level, flags: Int = 0) {
    val starter = Intent(this, ExerciseActivity::class.java).apply {
        putExtra(EXTRA_TEXT_TITLE, title)
        putExtra(EXTRA_TEXT_CONTENT, content)
        putExtra(EXTRA_TEXT_LEVEL, level.name)
        this.flags = flags
    }
    startActivity(starter)
}

private val EXTRA_TEXT_TITLE = "text_title"
private val EXTRA_TEXT_CONTENT = "text_content"
private val EXTRA_TEXT_LEVEL = "text_level"
private val EXTRA_EXERCISE_ANSWERS = "exercise_answers"
private val EXTRA_SCROLL_POSITION = "scroll_position"
private val EXTRA_EXERCISE = "exercise"

class ExerciseActivity : AppCompatActivity(), ExerciseView, AdapterListener {

    private val presenter: ExercisePresenter by lazy { ExercisePresenterImpl(this, ExerciseInteractorImpl(this)) }
    private val adapter: ExerciseAdapter by lazy { ExerciseAdapter(resources.getInteger(R.integer.exercise_default_font_size_sp).toFloat(), this) }
    private val minFontSize: Int by lazy { resources.getInteger(R.integer.exercise_min_font_size_sp) }
    private val maxFontSize: Int by lazy { resources.getInteger(R.integer.exercise_max_font_size_sp) }
    private val fontSizeDelta = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        presenter.onCreate(intent.getStringExtra(EXTRA_TEXT_TITLE, ""),
            intent.getStringExtra(EXTRA_TEXT_CONTENT, ""),
            Level.valueOf(intent.getStringExtra(EXTRA_TEXT_LEVEL, Level.EASY.name)),
            savedInstanceState?.getParcelable(EXTRA_EXERCISE))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(EXTRA_EXERCISE, adapter.exercise)
        outState.putParcelable(EXTRA_EXERCISE_ANSWERS, adapter.getExerciseAnswers())
        val llm = (exercise_rv.layoutManager as? LinearLayoutManager)
        outState.putInt(EXTRA_SCROLL_POSITION, llm?.findFirstCompletelyVisibleItemPosition() ?: 0)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        adapter.setExerciseAnswers(savedInstanceState?.getParcelable(EXTRA_EXERCISE_ANSWERS))
        exercise_rv.scrollToPosition(savedInstanceState?.getInt(EXTRA_SCROLL_POSITION) ?: 0)
    }

    private fun setupView() {
        setContentView(R.layout.activity_exercise)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        exercise_rv.adapter = adapter
        exercise_iv_font_big.setOnClickListener { presenter.onIncreaseFontSizeClick() }
        exercise_iv_font_small.setOnClickListener { presenter.onDecreaseFontSizeClick() }
        exercise_fab_check.setOnClickListener { presenter.onCheckExerciseClick(adapter.exercise, adapter.getExerciseAnswers()) }
//        exercise_av_banner.loadAd(AdRequest.Builder().build())
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        NavUtils.navigateUpFromSameTask(this)
    }

    override fun showLoading() {
        exercise_loading.visibility = View.VISIBLE
        exercise_group_ongoing.visibility = View.GONE
    }

    override fun showExercise(title: String, exercise: Exercise) {
        exercise_loading.visibility = View.GONE
        exercise_group_ongoing.visibility = View.VISIBLE
        exercise_tv_title.text = title
        adapter.exercise = exercise
    }

    override fun setExerciseProgress(progress: Int) {
        exercise_tv_progress.text = "${progress.toString()}%"
        exercise_pb.progress = progress
    }

    override fun increaseFontSize() {
        if (adapter.fontSize < maxFontSize) {
            adapter.fontSize = adapter.fontSize + fontSizeDelta
        }
    }

    override fun decreaseFontSize() {
        if (adapter.fontSize > minFontSize) {
            adapter.fontSize = adapter.fontSize - fontSizeDelta
        }
    }

    override fun navigateToResult(result: ExerciseResult) {
        logAnalytics("FINISH_EXERCISE")
        startResultActivity(result.title, result.content, result.level, result.percentage, result.textSolution)
    }

    override fun navigateToError() {
        toast(R.string.exercise_prepare_error)
        finish()
    }

    override fun showChangeAnswerDialog(position: Int, currentAnswer: String) {
        val padding = resources.getDimensionPixelSize(R.dimen.activity_margin)
        val input = AppCompatEditText(this).apply {
            maxLines = 1
            gravity = Gravity.CENTER
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textAlignment = EditText.TEXT_ALIGNMENT_CENTER
            }
            inputType = InputType.TYPE_CLASS_TEXT
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            append(currentAnswer)
        }
        val container = FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            setPadding(padding, padding, padding, padding)
            addView(input)
        }
        val dialog = AlertDialog.Builder(this).apply {
            setView(container)
        }.setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }.setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
            adapter.putAnswer(position, input.text.toString())
            dialog.dismiss()
        }.show()
        input.onNextOrEnterListener {
            adapter.putAnswer(position, input.text.toString())
            dialog.dismiss()
        }
        input.post { input.showKeyboard() }
    }

    override fun onProgressChanged(progress: Int) {
        presenter.onProgressChanged(progress)
    }

    override fun onChangeAnswerClick(position: Int, currentAnswer: String) {
        presenter.onChangeAnswerClick(position, currentAnswer)
    }

}
