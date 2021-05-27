package com.cornershop.countertest.presentation.main

import androidx.recyclerview.widget.DiffUtil
import com.cornershop.countertest.domain.model.Counter

class CounterDiffUtil(
    private val oldList: List<Counter>,
    private val newList: List<Counter>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}