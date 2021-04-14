package com.aiegroup.todo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.aiegroup.todo.R
import com.aiegroup.todo.databinding.RowItemBinding
import com.aiegroup.todo.models.ToDo

class RowItemAdapter(val callback: TodoListFragment.TaskItemClick) :
    RecyclerView.Adapter<RowViewHolder>() {

    /**
     * The todos items that our Adapter will show
     */
    var items: List<ToDo> = emptyList()
        set(value) {
            field = value
            // For an extra challenge, update this to use the paging library.

            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val withDataBinding: RowItemBinding =  RowItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return RowViewHolder(withDataBinding)
    }

    override fun getItemCount() = items.size

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.item = items[position]
            it.callBack = callback
        }
    }

}

/**
 * ViewHolder for DevByte items. All work is done by data binding.
 */
class RowViewHolder(val viewDataBinding: RowItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.row_item
    }
}