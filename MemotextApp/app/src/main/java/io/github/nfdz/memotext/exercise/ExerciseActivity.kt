package io.github.nfdz.memotext.exercise

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.*
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
            Level.valueOf(intent.getStringExtra(EXTRA_TEXT_LEVEL, Level.BRONZE.name)),
            savedInstanceState?.getParcelable(EXTRA_EXERCISE))
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelable(EXTRA_EXERCISE, adapter.exercise)
        outState?.putParcelable(EXTRA_EXERCISE_ANSWERS, adapter.getExerciseAnswers())
        val llm = (exercise_rv.layoutManager as? LinearLayoutManager)
        outState?.putInt(EXTRA_SCROLL_POSITION, llm?.findFirstCompletelyVisibleItemPosition() ?: 0)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        adapter.setExerciseAnswers(savedInstanceState?.getParcelable<ExerciseAnswers>(EXTRA_EXERCISE_ANSWERS))
        exercise_rv.scrollToPosition(savedInstanceState?.getInt(EXTRA_SCROLL_POSITION) ?: 0)
    }

    private fun setupView() {
        setContentView(R.layout.activity_exercise)
        exercise_rv.adapter = adapter
        exercise_iv_font_big.setOnClickListener { presenter.onIncreaseFontSizeClick() }
        exercise_iv_font_small.setOnClickListener { presenter.onDecreaseFontSizeClick() }
        exercise_fab_check.setOnClickListener { presenter.onCheckExerciseClick(adapter.exercise, adapter.getExerciseAnswers()) }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
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
        // TODO
    }

    override fun navigateToError() {
        toast(R.string.exercise_prepare_error)
        finish()
    }

    override fun showChangeAnswerDialog(position: Int, currentAnswer: String) {
        val container = FrameLayout(this)
        container.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        val padding = resources.getDimensionPixelSize(R.dimen.activity_margin)
        container.setPadding(padding, padding, padding, padding)
        val input = AppCompatEditText(this)
        input.maxLines = 1
        input.gravity = Gravity.CENTER
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) input.textAlignment = EditText.TEXT_ALIGNMENT_CENTER
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        input.append(currentAnswer)
        container.addView(input)
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
