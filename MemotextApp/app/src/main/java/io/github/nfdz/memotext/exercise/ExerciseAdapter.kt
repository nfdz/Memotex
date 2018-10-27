package io.github.nfdz.memotext.exercise

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.*
import kotlinx.android.synthetic.main.item_exercise_slot.view.*
import kotlinx.android.synthetic.main.item_exercise_text.view.*
import kotlin.properties.Delegates

interface AdapterListener {
    fun onChangeAnswerClick(position: Int, currentAnswer: String)
}

class ExerciseAdapter(exercise: Exercise = Exercise(emptyList()), val listener: AdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val SPACE_VIEW_TYPE = 0
    private val TEXT_VIEW_TYPE = 1
    private val SLOT_VIEW_TYPE = 2

    var exercise by Delegates.observable(exercise) { _, _, _ ->
        notifyDataSetChanged()
    }

    private val answers: MutableMap<Int,String> = mutableMapOf()

    fun getExerciseAnswers() = ExerciseAnswers(answers.toMap())
    fun setExerciseAnswers(exerciseAnswers: ExerciseAnswers?) {
        exerciseAnswers?.let {
            answers.clear()
            answers.putAll(exerciseAnswers.answers)
        }
    }

    fun putAnswer(position: Int, answer: String) {
        answers.put(position, answer)
        notifyDataSetChanged()
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
                holder.bind(it.text)
            }
        } else if (holder is SlotHolder) {
            holder.bind(answers[position] ?: "")
        }
    }

    class TextHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(text: String) = with(itemView) {
            item_exercise_text.text = text
        }

    }

    class SlotHolder(view: View, val listener: AdapterListener) : RecyclerView.ViewHolder(view) {

        fun bind(answer: String) = with(itemView) {
            item_exercise_slot.text = answer
            itemView.item_exercise_slot.setOnClickListener { listener.onChangeAnswerClick(adapterPosition, answer) }
        }

    }

    class SpaceHolder(view: View) : RecyclerView.ViewHolder(view)

}