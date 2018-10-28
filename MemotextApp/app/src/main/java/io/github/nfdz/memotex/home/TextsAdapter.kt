package io.github.nfdz.memotex.home

import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.LEFT
import android.support.v7.widget.helper.ItemTouchHelper.RIGHT
import android.view.View
import android.view.ViewGroup
import io.github.nfdz.memotex.R
import io.github.nfdz.memotex.common.Level
import io.github.nfdz.memotex.common.Text
import io.github.nfdz.memotex.common.inflate
import kotlinx.android.synthetic.main.item_text_entry.view.*
import kotlin.properties.Delegates


interface AdapterListener {
    fun onTextClick(text: Text)
    fun onEditTextClick(text: Text)
    fun onLevelIconClick(text: Text)
    fun onDeleteClick(text: Text)
}

class TextsAdapter(data: List<Text> = emptyList(), val listener: AdapterListener) : RecyclerView.Adapter<TextsAdapter.TextEntryHolder>() {

    var data by Delegates.observable(data) { _, oldList, newList ->
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldList.size
            }
            override fun getNewListSize(): Int {
                return newList.size
            }
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        })
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextEntryHolder {
        return TextEntryHolder(parent.inflate(R.layout.item_text_entry), listener)
    }

    override fun onBindViewHolder(holder: TextEntryHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class TextEntryHolder(view: View, val listener: AdapterListener) : RecyclerView.ViewHolder(view) {

        var bindedText: Text? = null

        fun bind(item: Text) = with(itemView) {
            tv_text_title.text = item.title
            tv_success_percentage.text = "${item.percentage.toString()}%"
            iv_level_trophy.contentDescription = when (item.level) {
                Level.EASY -> context.getString(R.string.level_easy)
                Level.MEDIUM -> context.getString(R.string.level_medium)
                Level.HARD -> context.getString(R.string.level_hard)
            }
            val color = when (item.level) {
                Level.EASY -> ContextCompat.getColor(context, R.color.easyColor)
                Level.MEDIUM -> ContextCompat.getColor(context, R.color.mediumColor)
                Level.HARD -> ContextCompat.getColor(context, R.color.hardColor)
            }
            iv_level_trophy.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_ATOP)
            iv_level_trophy.setOnClickListener { listener.onLevelIconClick(item) }
            setOnClickListener { listener.onTextClick(item) }
            setOnLongClickListener { listener.onEditTextClick(item); true }
            bindedText = item
        }

        fun onSwiped() {
            bindedText?.let { listener.onDeleteClick(it) }
        }

    }

}

class TextSwipeController : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return ItemTouchHelper.Callback.makeMovementFlags(0, LEFT or RIGHT)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder is TextsAdapter.TextEntryHolder) {
            viewHolder.onSwiped()
        }
    }

}