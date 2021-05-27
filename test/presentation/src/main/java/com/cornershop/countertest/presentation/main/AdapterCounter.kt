package com.cornershop.countertest.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.presentation.R
import com.cornershop.countertest.presentation.databinding.ItemMainCounterBinding

class AdapterCounter(
    val minusListener: (counter: Counter) -> Unit,
    val plusListener: (counter: Counter) -> Unit,
) : RecyclerView.Adapter<AdapterCounter.ViewHolder>() {
    private val counterList = mutableListOf<Counter>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_main_counter, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val counter = counterList[position]

        holder.binding.counterTitle.text = counter.title
        holder.binding.counterNum.text = counter.count.toString()
        holder.binding.minus.isEnabled = counter.count > 0
        holder.binding.minus.setOnClickListener { minusListener.invoke(counter) }
        holder.binding.plus.setOnClickListener { plusListener.invoke(counter) }
    }

    override fun getItemCount(): Int = counterList.size

    fun updateCounterList(newCounterList: List<Counter>) {
        val diffUtilCallback = CounterDiffUtil(
            oldList = counterList,
            newList = newCounterList
        )
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        counterList.clear()
        counterList.addAll(newCounterList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMainCounterBinding.bind(view)
    }
}