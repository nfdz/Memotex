package io.github.nfdz.memotex.exercise

import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import io.github.nfdz.memotex.R
import io.github.nfdz.memotex.common.*
import kotlinx.android.synthetic.main.item_exercise_slot.view.*
import kotlinx.android.synthetic.main.item_exercise_space.view.*
import kotlinx.android.synthetic.main.item_exercise_text.view.*
import kotlin.properties.Delegates

interface AdapterListener {
    fun onProgressChanged(progress: Int)
    fun onChangeAnswerClick(position: Int, currentAnswer: String)
}

class ExerciseAdapter(initialFontSize: Float, val listener: AdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val SPACE_VIEW_TYPE = 0
    private val TEXT_VIEW_TYPE = 1
    private val SLOT_VIEW_TYPE = 2

    private var slotsToFill = 0
    private val answers: MutableMap<Int,String> = mutableMapOf()

    var fontSize by Delegates.observable(initialFontSize) { _, _, _ ->
        notifyDataSetChanged()
    }

    var exercise by Delegates.observable(Exercise(emptyList())) { _, _, newValue ->
        slotsToFill = newValue.countSlots()
        notifyDataSetChanged()
        notifyProgress()
    }

    fun getExerciseAnswers() = ExerciseAnswers(answers.toMap())
    fun setExerciseAnswers(exerciseAnswers: ExerciseAnswers?) {
        exerciseAnswers?.let {
            answers.clear()
            answers.putAll(exerciseAnswers.answers)
            notifyProgress()
            notifyDataSetChanged()
        }
    }

    fun putAnswer(position: Int, answer: String) {
        if (answer.isEmpty()) {
            answers.remove(position)
        } else {
            answers[position] = answer
        }
        notifyItemChanged(position)
        notifyProgress()
    }

    private fun notifyProgress() {
        listener.onProgressChanged(Math.ceil(100*answers.size.toDouble()/slotsToFill).toInt())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TEXT_VIEW_TYPE -> { TextHolder(parent.inflate(R.layout.item_exercise_text)) }
            SLOT_VIEW_TYPE -> { SlotHolder(parent.inflate(R.layout.item_exercise_slot), listener) }
            else -> { SpaceHolder(parent.inflate(R.layout.item_exercise_space)) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val element = exercise.elements[position]
        return when (element) {
            is TextElement -> { TEXT_VIEW_TYPE }
            is SlotElement -> { SLOT_VIEW_TYPE }
            else -> { SPACE_VIEW_TYPE }
        }
    }

    override fun getItemCount() = exercise.elements.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TextHolder) {
            (exercise.elements[position] as? TextElement)?.let {
                holder.bind(it.text, fontSize)
            }
        } else if (holder is SlotHolder) {
            holder.bind(answers[position] ?: "", fontSize)
        } else if (holder is SpaceHolder) {
            holder.bind(fontSize)
        }
    }

    class TextHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(text: String, fontSize: Float) = with(itemView) {
            item_exercise_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
            item_exercise_text.text = text.replace("\n", " / ").replace("\r", " / ")
        }

    }

    class SlotHolder(view: View, val listener: AdapterListener) : RecyclerView.ViewHolder(view) {

        fun bind(answer: String, fontSize: Float) = with(itemView) {
            item_exercise_slot.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
            item_exercise_slot.text = answer
            itemView.item_exercise_slot.setOnClickListener { listener.onChangeAnswerClick(adapterPosition, answer) }
        }

    }

    class SpaceHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(fontSize: Float) = with(itemView) {
            item_exercise_space.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
        }

    }

}