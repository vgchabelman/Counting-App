package com.cornershop.countertest.presentation.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.domain.model.CounterView

class CounterViewDiffUtil(
    private val oldList: List<CounterView>,
    private val newList: List<CounterView>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].counter.id == newList[newItemPosition].counter.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].selected == newList[newItemPosition].selected &&
                oldList[oldItemPosition].counter == newList[newItemPosition].counter
    }
}
class CounterDiffUtil(
    private val oldList: List<Counter>,
    private val newList: List<Counter>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}