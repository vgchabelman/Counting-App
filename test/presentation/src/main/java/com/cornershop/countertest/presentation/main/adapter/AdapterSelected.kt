package com.cornershop.countertest.presentation.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.domain.model.CounterView
import com.cornershop.countertest.presentation.R
import com.cornershop.countertest.presentation.databinding.ItemMainSelectedBinding

class AdapterSelected(
    private val selectedListener: (counter: Counter) -> Unit
) : RecyclerView.Adapter<AdapterSelected.ViewHolder>() {
    private val selectedList: MutableList<CounterView> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(
                R.layout.item_main_selected,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val counter = selectedList[position].counter

        holder.binding.counterTitle.text = counter.title
        holder.binding.selectedGroup.isVisible = selectedList[position].selected
        holder.binding.root.setOnClickListener {
            selectedListener.invoke(counter)
        }
    }

    override fun getItemCount(): Int = selectedList.size

    fun updateCounterList(newCounterList: List<CounterView>) {
        val diffUtilCallback = CounterViewDiffUtil(
            oldList = selectedList,
            newList = newCounterList
        )
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        selectedList.clear()
        selectedList.addAll(newCounterList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMainSelectedBinding.bind(view)
    }
}