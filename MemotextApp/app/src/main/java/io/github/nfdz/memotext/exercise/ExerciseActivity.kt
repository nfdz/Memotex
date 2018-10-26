package io.github.nfdz.memotext.exercise

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.Text

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

class ExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
//        presenter.onCreate(intent.getStringExtra(EXTRA_TEXT_TITLE, ""),
//            intent.getStringExtra(EXTRA_TEXT_TITLE, ""),
//            Level.valueOf(intent.getStringExtra(EXTRA_TEXT_TITLE, Level.BRONZE.name)))
    }

    private fun setupView() {
        setContentView(R.layout.activity_exercise)
        //exercise_group_ongoing

    }

    override fun onDestroy() {
//        presenter.onDestroy()
        super.onDestroy()
    }

}
