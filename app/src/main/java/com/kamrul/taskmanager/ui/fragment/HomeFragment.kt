package com.kamrul.taskmanager.ui.fragment

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentHomeBinding
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.github.sundeepk.compactcalendarview.domain.Event
import com.kamrul.taskmanager.utils.ArgumentKey
import com.kamrul.taskmanager.utils.CompareKey
import com.kamrul.taskmanager.adapter.EventAdapter
import com.kamrul.taskmanager.adapter.TaskAdapter
import com.kamrul.taskmanager.model.Task
import com.kamrul.taskmanager.receiver.AlarmBroadcastReceiver
import com.kamrul.taskmanager.utils.DateTimeConverter
import com.kamrul.taskmanager.viewmodel.TaskViewModel
import java.util.Date


class HomeFragment : Fragment(), TaskAdapter.Interaction, EventAdapter.Interaction {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels()
    private var taskList = listOf<Task>()
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var eventAdapter: EventAdapter
    private var eventlist = listOf<Event>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        taskAdapter = TaskAdapter(this)
        eventAdapter = EventAdapter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAdd.setOnClickListener {
            findNavController().navigate(R.id.addTaskFragment)
        }

        binding.rvTask.adapter = taskAdapter

        viewModel.taskList.observe(viewLifecycleOwner) { list ->
            if (!list.isNullOrEmpty()) {
                if (taskList != list) {
                    taskList = list
                    taskAdapter.submitList(taskList as ArrayList<Task>)
                }
            }
            if (taskList.isEmpty()) {
                binding.tvNoData.visibility = View.VISIBLE
            } else {
                binding.tvNoData.visibility = View.GONE
            }
        }

        binding.ivCalender.setOnClickListener {
            showCalendarDialog()
        }
    }

    override fun onClickMenu(item: Task, view: ImageView) {
        val menu = PopupMenu(requireContext(), view)
        menu.inflate(R.menu.tast_item_menu)
        menu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_complete -> {
                    showConfirmWarningDialog(item, CompareKey.COMPLETE.name)
                    return@setOnMenuItemClickListener true
                }

                R.id.menu_update -> {
                    val bundle = Bundle()
                    bundle.putParcelable(ArgumentKey.EDIT_DATA.name, item)
                    findNavController().navigate(R.id.addTaskFragment, bundle)

                    return@setOnMenuItemClickListener true
                }

                R.id.menu_delete -> {
                    showConfirmWarningDialog(item, CompareKey.DELETE.name)
                    return@setOnMenuItemClickListener true
                }

                else -> false
            }
        }
        menu.show()
    }

    private fun showConfirmWarningDialog(item: Task, type: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_confirm)
        dialog.window?.setBackgroundDrawableResource(R.color.full_transparent)
        //dialog.setCanceledOnTouchOutside(true)


        // get reference
        val ok = dialog.findViewById<Button>(R.id.btn_ok)
        val cancel = dialog.findViewById<Button>(R.id.btn_cancel)
        val msg = dialog.findViewById<TextView>(R.id.tv_message)

        if (type == CompareKey.DELETE.name) {
            val warning = "Are you sure to delete '${item.title}' task?"
            msg?.text = warning

            ok?.setOnClickListener {
                viewModel.deleteTask(item)
                deleteAlarm(item)
                dialog.dismiss()
            }

        } else {

            val warning = "Are you sure to update '${item.title}' task to complete?"
            msg?.text = warning

            val task = Task(
                item.id,
                item.title,
                item.description,
                item.date,
                item.time,
                item.duration,
                true
            )

            ok?.setOnClickListener {
                viewModel.updateTask(task)
                val duration = DateTimeConverter.dateStringToDuration(item.date + " " + item.time)

                // if alarm is not run now
                if (duration > 0) {
                    deleteAlarm(item)
                }

                dialog.dismiss()
                Toast.makeText(
                    requireContext(),
                    "You have successfully updated data!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        cancel?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showCalendarDialog() {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_calendar_show)
        dialog.window?.setBackgroundDrawableResource(R.color.full_transparent)
        dialog.setCanceledOnTouchOutside(true)

        // get reference
        val cancel = dialog.findViewById<Button>(R.id.btn_cancel)
        val calendar = dialog.findViewById<CompactCalendarView>(R.id.cv_calendar)
        val month = dialog.findViewById<TextView>(R.id.tv_month_title)
        val events = dialog.findViewById<RecyclerView>(R.id.rv_day_tak)

        // Add tasks to the calendar
        for (task in taskList) {
            val time = DateTimeConverter.dateStringToLong(task.date + " " + task.time)
            val event = Event(R.color.light_red, time, task)
            calendar.addEvent(event)
        }

        calendar.setCurrentDate(Date())
        month.text = DateTimeConverter.longToMonth(Date().time)
        eventlist = calendar.getEvents(Date())
        showEvent()

        events.apply {
            adapter = eventAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        calendar.setListener(object : CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                eventlist = calendar.getEvents(dateClicked)
                showEvent()
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                eventlist = calendar.getEvents(firstDayOfNewMonth)

                showEvent()

                val monthYear = DateTimeConverter.longToMonth(firstDayOfNewMonth.time)
                month.text = monthYear
            }
        })

        cancel?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showEvent() {
        val mutableList = mutableListOf<Task>()
        eventlist.forEach { item ->
            mutableList.add(item.data as Task)
        }
        eventAdapter.submitList(mutableList as ArrayList<Task>)

    }

    private fun deleteAlarm(task: Task) {

        val alarmIntent = Intent(requireContext(), AlarmBroadcastReceiver::class.java).apply {
            action = CompareKey.ALARM_ACTION.name
            putExtra(ArgumentKey.ALARM_TASK.name, task)
        }

        val requestCodeCounter = task.id
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCodeCounter,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Cancel the alarm
        alarmManager.cancel(pendingIntent)
    }


    override fun onClickEvent(item: Task) {
        showEventDetailsDialog(item)
    }

    private fun showEventDetailsDialog(item: Task) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_event_details)
        dialog.window?.setBackgroundDrawableResource(R.color.full_transparent)
        dialog.setCanceledOnTouchOutside(true)

        // get reference
        val tvDayOfWeek = dialog.findViewById<TextView>(R.id.tv_day_of_week)
        val tvDayOfMonth = dialog.findViewById<TextView>(R.id.tv_day_of_month)
        val tvMonth = dialog.findViewById<TextView>(R.id.tv_month)
        val tvTitle = dialog.findViewById<TextView>(R.id.tv_title)
        val tvDesc = dialog.findViewById<TextView>(R.id.tv_desc)
        val tvTime = dialog.findViewById<TextView>(R.id.tv_time)
        val tvCondition = dialog.findViewById<TextView>(R.id.tv_condition)
        val cancel = dialog.findViewById<TextView>(R.id.tv_cancel)
        val menu = dialog.findViewById<ImageView>(R.id.iv_menu)

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

        menu?.setOnClickListener {
            onClickMenu(item, menu)
        }

        cancel?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}