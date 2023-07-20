package com.example.shopify.utils.recycler

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.swipeRecyclerItemListener(action  : (RecyclerView.ViewHolder) -> Unit)
{
    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
          return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            action.invoke(viewHolder)
        }

    }).attachToRecyclerView(this)
}