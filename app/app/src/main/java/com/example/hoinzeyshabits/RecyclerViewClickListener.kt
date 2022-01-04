package com.example.hoinzeyshabits

import android.view.View

interface RecyclerViewClickListener {
    fun recyclerViewListClicked(v: View?, id: Int, action: RecyclerViewAction)

    enum class RecyclerViewAction{
        EDIT,
        ACHIEVE_GOAL
    }
}