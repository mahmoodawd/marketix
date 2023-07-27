package com.example.shopify.utils.recycler

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.moveRecyclerItemListener(action: (Int, Int) -> Unit) {
    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP, ItemTouchHelper.DOWN
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val draggedItemIndex = viewHolder.adapterPosition
            val targetIndex = target.adapterPosition
            action.invoke(draggedItemIndex, targetIndex)
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }

    }).attachToRecyclerView(this)
}