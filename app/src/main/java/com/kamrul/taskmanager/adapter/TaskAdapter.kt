package com.kamrul.taskmanager.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ItemTaskBinding
import com.kamrul.taskmanager.model.Task
import com.kamrul.taskmanager.utils.DateTimeConverter

class TaskAdapter(private val interaction: Interaction? = null) :
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
                R.layout.item_task,
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
                val item = differ.currentList.getOrNull(position)
                if (item != null) {
                    holder.bind(item)
                }
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

        var binding = ItemTaskBinding.bind(itemView)

        fun bind(item: Task) {

            binding.apply {

                ivMenu.setOnClickListener {
                    interaction?.onClickMenu(item, ivMenu)
                }

                tvTitle.text = item.title
                tvDesc.text = item.description
                tvTime.text = DateTimeConverter.formatTimeToAmPm(item.time)
                val duration = DateTimeConverter.dateStringToDuration(item.date + " " + item.time)

                tvCondition.text =
                    if (item.completed) {
                        "Completed"
                    } else {
                        if (duration > 0) {
                            "Upcoming"
                        } else {
                            "Expired"
                        }
                    }

                val date = DateTimeConverter.dateToShowDate(item.date).split("$")
                Log.d("date_tag", "onBindViewHolder: $date")
                if (date.size == 3) {
                    tvDayOfWeek.text = date[0]
                    tvDayOfMonth.text = date[1]
                    tvMonth.text = date[2]
                }
            }
        }
    }

    interface Interaction {
        fun onClickMenu(item: Task, menu: ImageView)
    }
}

/*
 class TaskAdapter(
    private val taskList: List<Task>
) : RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    private lateinit var context: Context

    class TaskHolder(binding: View) : RecyclerView.ViewHolder(binding.rootView), View.OnClickListener {
        val dayOfWeek = binding.findViewById<TextView>(R.id.tv_day_of_week)
        val dayOfMonth = binding.findViewById<TextView>(R.id.tv_day_of_month)
        val month = binding.findViewById<TextView>(R.id.tv_month)
        val title = binding.findViewById<TextView>(R.id.tv_title)
        val desc = binding.findViewById<TextView>(R.id.tv_desc)
        val time = binding.findViewById<TextView>(R.id.tv_time)
        val complete = binding.findViewById<TextView>(R.id.tv_condition)
        val menu = binding.findViewById<ImageView>(R.id.iv_menu)

        override fun onClick(p0: View?) {
            showMenu()
        }

        private fun showMenu() {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        context = parent.context
        val view = LayoutInflater.from(MyApplication.appContext)
            .inflate(R.layout.item_task, parent, false)
        return TaskHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val item = taskList[position]

        holder.title.text = item.title
        holder.desc.text = item.description
        holder.time.text = DateTimeConverter.formatTimeToAmPm(item.time)
        holder.complete.text =
            if (item.completed) {
                "Completed"
            } else {
                "Upcoming"
            }
        val date = DateTimeConverter.dateToShowDate(item.date).split("$")
        Log.d("date_tag", "onBindViewHolder: $date")
        holder.dayOfWeek.text = date[0]
        holder.dayOfMonth.text = date[1]
        holder.month.text = date[2]

    }
}
* */
