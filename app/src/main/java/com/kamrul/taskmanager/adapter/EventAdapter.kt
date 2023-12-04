package com.kamrul.taskmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ItemEventBinding
import com.kamrul.taskmanager.model.Task

class EventAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {

        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_event,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                //holder.bind(differ.currentList[position])
                val item = differ.currentList.getOrNull(position) as Task
                holder.bind(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: ArrayList<Task>) {
        differ.submitList(list)
    }

    class ViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        var binding = ItemEventBinding.bind(itemView)

        fun bind(item: Task) {

            binding.apply {

                itemView.setOnClickListener {
                    interaction?.onClickEvent(item)
                }

                tvEvent.text = if (item.title.length > 15) {
                    item.time.substring(0, 15) + "..."
                } else {
                    item.title
                }
            }
        }
    }

    interface Interaction {
        fun onClickEvent(item: Task)
    }
}
