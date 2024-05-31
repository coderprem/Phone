package com.example.phone.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.phone.db.ContactEntity

class SwipeToCallCallback(private val listener: OnSwipeToCallListener) : ItemTouchHelper.Callback() {

    interface OnSwipeToCallListener {
        fun onSwipeToCall(contact: String)
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlags = ItemTouchHelper.LEFT
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.RIGHT) {
            val contact = (viewHolder as ContactAdapter.ContactViewHolder).number
            listener.onSwipeToCall(contact.toString())
        }
    }
}
