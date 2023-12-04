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
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentAddTaskBinding
import com.kamrul.taskmanager.utils.ArgumentKey
import com.kamrul.taskmanager.utils.CompareKey
import com.kamrul.taskmanager.model.Task
import com.kamrul.taskmanager.receiver.AlarmBroadcastReceiver
import com.kamrul.taskmanager.utils.DateTimeConverter
import com.kamrul.taskmanager.viewmodel.TaskViewModel
import java.util.Calendar

class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels()
    private var customDialog: Dialog? = null

    private var data: Task? = null
    private var task_id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getParcelable(ArgumentKey.EDIT_DATA.name)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (data != null) {
            binding.tvTitle.text = "Update Task"
            binding.tvSubTitle.text = "Please Update The Task Details"
            binding.btnAddTask.text = "Update"

            binding.etTitle.setText(data?.title)
            binding.etDesc.setText(data?.description)
            binding.etDate.setText(data?.date)
            val time = data?.time?.let { DateTimeConverter.formatTimeToAmPm(it) }
            binding.etTime.setText(time)

            binding.etDate.setOnFocusChangeListener { _, focus ->
                if (focus) {
                    hideKeyboard()

                    if (binding.etTitle.text.isNullOrEmpty()) {
                        binding.etTitle.error = "Required"
                        binding.etTitle.requestFocus()
                    } else {
                        binding.etTitle.error = null

                        val flag = customDialog?.isShowing ?: false
                        if (!flag) {
                            showDatePickerDialog(data?.date ?: "")
                        }
                    }
                }
            }

            binding.etTime.setOnFocusChangeListener { _, focus ->
                if (focus) {
                    hideKeyboard()

                    if (binding.etTitle.text.isNullOrEmpty()) {
                        binding.etTitle.error = "Required"
                        binding.etTitle.requestFocus()
                    } else {
                        binding.etTitle.error = null

                        val flag = customDialog?.isShowing ?: false
                        if (!flag) {
                            showTimePickerDialog(data?.time ?: "")
                        }
                    }
                }
            }

            binding.etDate.setOnClickListener() {
                hideKeyboard()

                if (binding.etTitle.text.isNullOrEmpty()) {
                    binding.etTitle.error = "Required"
                    binding.etTitle.requestFocus()
                } else {
                    binding.etTitle.error = null

                    val flag = customDialog?.isShowing ?: false
                    if (!flag) {
                        showDatePickerDialog(data?.date ?: "")
                    }
                }
            }

            binding.etTime.setOnClickListener {
                hideKeyboard()

                if (binding.etTitle.text.isNullOrEmpty()) {
                    binding.etTitle.error = "Required"
                    binding.etTitle.requestFocus()
                } else if (binding.etDate.text.isNullOrEmpty()) {
                    binding.etDate.error = "Required"
                    binding.etDate.requestFocus()
                } else {
                    binding.etTitle.error = null
                    binding.etDate.error = null

                    val flag = customDialog?.isShowing ?: false
                    if (!flag) {
                        showTimePickerDialog(data?.time ?: "")
                    }
                }
            }

            binding.btnAddTask.setOnClickListener {
                if (binding.etTitle.text.isNullOrEmpty()) {
                    binding.etTitle.error = "Required"
                    binding.etTitle.requestFocus()
                } else if (binding.etDate.text.isNullOrEmpty()) {
                    binding.etDate.error = "Required"
                    binding.etDate.requestFocus()
                } else {
                    binding.etTitle.error = null
                    binding.etDate.error = null

                    updateTask()
                }
            }

        } else {

            binding.etDate.setOnFocusChangeListener { _, focus ->
                if (focus) {
                    hideKeyboard()

                    if (binding.etTitle.text.isNullOrEmpty()) {
                        binding.etTitle.error = "Required"
                        binding.etTitle.requestFocus()
                    } else {
                        binding.etTitle.error = null

                        val flag = customDialog?.isShowing ?: false
                        if (!flag) {
                            showDatePickerDialog("")
                        }
                    }
                }
            }

            binding.etTime.setOnFocusChangeListener { _, focus ->
                if (focus) {
                    hideKeyboard()
                    if (binding.etTitle.text.isNullOrEmpty()) {
                        binding.etTitle.error = "Required"
                        binding.etTitle.requestFocus()
                    } else if (binding.etDate.text.isNullOrEmpty()) {
                        binding.etDate.error = "Required"
                        binding.etDate.requestFocus()
                    } else {
                        binding.etTitle.error = null
                        binding.etDate.error = null

                        val flag = customDialog?.isShowing ?: false
                        if (!flag) {
                            showTimePickerDialog("")
                        }
                    }
                }
            }

            binding.etDate.setOnClickListener {
                hideKeyboard()

                if (binding.etTitle.text.isNullOrEmpty()) {
                    binding.etTitle.error = "Required"
                } else {
                    binding.etTitle.error = null

                    val flag = customDialog?.isShowing ?: false
                    if (!flag) {
                        showDatePickerDialog("")
                    }
                }
            }

            binding.etTime.setOnClickListener {
                hideKeyboard()

                if (binding.etTitle.text.isNullOrEmpty()) {
                    binding.etTitle.error = "Required"
                } else if (binding.etDate.text.isNullOrEmpty()) {
                    binding.etDate.error = "Required"
                } else {
                    binding.etTitle.error = null
                    binding.etDate.error = null

                    val flag = customDialog?.isShowing ?: false
                    if (!flag) {
                        showTimePickerDialog("")
                    }
                }
            }

            binding.btnAddTask.setOnClickListener {
                if (binding.etTitle.text.isNullOrEmpty()) {
                    binding.etTitle.error = "Required"
                } else if (binding.etDate.text.isNullOrEmpty()) {
                    binding.etDate.error = "Required"
                } else {
                    binding.etTitle.error = null
                    binding.etDate.error = null

                    addTask()
                }
            }
        }

        binding.close.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

    }

    private fun updateTask() {
        val title = binding.etTitle.text.toString()
        val description = if (binding.etDesc.text.isNullOrEmpty()) {
            title
        } else {
            binding.etDesc.text.toString()
        }

        val date = binding.etDate.text.toString()
        val time = DateTimeConverter.formatTimeTo24Hour(binding.etTime.text.toString())

        val duration = DateTimeConverter.dateStringToDuration("$date $time")
        if (duration < 0) {
            binding.etDate.error = "Invalid"
            binding.etTime.error = "Invalid"

        } else {
            val task = data?.id?.let {
                Task(
                    it,
                    title,
                    description,
                    date,
                    time,
                    duration,
                    false
                )
            }
            if (task != null) {
                showConfirmWarningDialog(task)
            }
        }
    }

    private fun showConfirmWarningDialog(item: Task) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_confirm)
        dialog.window?.setBackgroundDrawableResource(R.color.full_transparent)

        // get reference
        val ok = dialog.findViewById<Button>(R.id.btn_ok)
        val cancel = dialog.findViewById<Button>(R.id.btn_cancel)
        val msg = dialog.findViewById<TextView>(R.id.tv_message)

        val warning = "Are you sure to update '${item.title}' task to complete?"
        msg?.text = warning


        ok?.setOnClickListener {
            viewModel.updateTask(item)
            dialog.dismiss()
            Toast.makeText(
                requireContext(),
                "You have successfully updated data!!",
                Toast.LENGTH_SHORT
            ).show()

            updateAlarm(task = item)

            findNavController().navigate(R.id.homeFragment)
        }

        cancel?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun addTask() {
        val title = binding.etTitle.text.toString()
        val description = if (binding.etDesc.text.isNullOrEmpty()) {
            title
        } else {
            binding.etDesc.text.toString()
        }

        val date = binding.etDate.text.toString()
        val time = DateTimeConverter.formatTimeTo24Hour(binding.etTime.text.toString())

        val duration = DateTimeConverter.dateStringToDuration("$date $time")
        if (duration < 0) {
            binding.etDate.error = "Invalid"
            binding.etTime.error = "Invalid"

        } else {
            val task = Task(
                0,
                title,
                description,
                date,
                time,
                duration,
                false
            )
            viewModel.addTask(task)
            viewModel.taskId.observe(viewLifecycleOwner){
                //Log.d("task_id", "addTask: (before) it: $it task_id: $task_id")
                if(it != null && it != task_id){
                    task_id = it
                    val taskCopy = task.copy(
                        id = task_id
                    )
                    setAlarm(taskCopy)

                    Log.d("task_id", "addTask: id: ${taskCopy.id} name: ${taskCopy.title}")
                    findNavController().navigate(R.id.homeFragment)
                }
            }

        }
    }

    private fun showDatePickerDialog(date: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_date_picker)
        dialog.window?.setBackgroundDrawableResource(R.color.full_transparent)
        dialog.setCanceledOnTouchOutside(true)
        customDialog = dialog
        var year = 0
        var month = 0
        var day = 0

        if (date.isEmpty()) {
            val calendar = Calendar.getInstance()
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
            binding.etDate.setText("$day-$month-$year")
        } else {
            val dateList = date.split("-")
            day = dateList[0].toInt()
            month = dateList[1].toInt() - 1
            year = dateList[2].toInt()
        }


        // get reference
        val ok = dialog.findViewById<Button>(R.id.btn_ok)
        val datePicker = dialog.findViewById<DatePicker>(R.id.dp_task_date)

        datePicker?.updateDate(year, month, day)

        ok?.setOnClickListener {
            year = datePicker.year
            month = datePicker.month + 1
            day = datePicker.dayOfMonth
            val monthString = if (month < 10) {
                "0$month"
            } else {
                month.toString()
            }

            val dayString = if (day < 10) {
                "0$day"
            } else {
                day.toString()
            }
            binding.etDate.setText("$dayString-$monthString-$year")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showTimePickerDialog(timeString: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_time_picker)
        dialog.window?.setBackgroundDrawableResource(R.color.full_transparent)
        dialog.setCanceledOnTouchOutside(true)
        customDialog = dialog

        // get reference
        val ok = dialog.findViewById<Button>(R.id.btn_ok)
        val timePicker = dialog.findViewById<TimePicker>(R.id.tp_task_time)

        var hour = 0
        var minute = 0
        var amPm = 0

        if (timeString.isEmpty()) {

            val calendar = Calendar.getInstance()
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)
            amPm = calendar.get(Calendar.AM_PM)

            if (amPm == 1 && hour != 12) {
                hour += 12
            } else if (amPm == 0 && hour == 12) {
                hour = 0
            }

        } else {
            val timeList = timeString.split(":")
            hour = timeList[0].toInt()
            minute = timeList[1].toInt()
        }


        var hourString = if (hour < 10) {
            "0$hour"
        } else {
            hour.toString()
        }
        var minuteString = if (minute < 10) {
            "0$minute"
        } else {
            minute.toString()
        }

        var time =
            DateTimeConverter.formatTimeToAmPm("$hourString:$minuteString")
        binding.etTime.setText(time)


        timePicker?.hour = hour
        timePicker?.minute = minute

        ok?.setOnClickListener {
            hour = timePicker.hour
            minute = timePicker.minute

            hourString = if (hour < 10) {
                "0$hour"
            } else {
                hour.toString()
            }
            minuteString = if (minute < 10) {
                "0$minute"
            } else {
                minute.toString()
            }

            time =
                DateTimeConverter.formatTimeToAmPm("$hourString:$minuteString")
            binding.etTime.setText(time)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun hideKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setAlarm(task: Task) {
        val alarmTime = DateTimeConverter.dateStringToLong(task.date + " " + task.time)
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(requireContext(), AlarmBroadcastReceiver::class.java).apply {
            action = CompareKey.ALARM_ACTION.name
            val taskData =  "${task.title}$${task.description}$${task.date}$${task.time}"
            putExtra(ArgumentKey.ALARM_TASK.name, taskData)
            //putExtra(ArgumentKey.ALARM_TASK.name, task)
        }

        val requestCodeCounter = task.id

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            requestCodeCounter,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Set the alarm
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime - (2*1000), pendingIntent)
    }

    private fun updateAlarm(task: Task) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val existingAlarmIntent = Intent(requireContext(), AlarmBroadcastReceiver::class.java).apply {
            action = CompareKey.ALARM_ACTION.name
            putExtra(ArgumentKey.ALARM_TASK.name, task)
        }
        val requestCodeCounter = task.id

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            requestCodeCounter,
            existingAlarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Cancel the existing alarm
        alarmManager.cancel(pendingIntent)

        // Set the new alarm
        val newAlarmIntent = Intent(requireContext(), AlarmBroadcastReceiver::class.java).apply {
            action = CompareKey.ALARM_ACTION.name
            putExtra(ArgumentKey.ALARM_TASK.name, task)
        }

        val newPendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            requestCodeCounter,
            newAlarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val newAlarmTime = DateTimeConverter.dateStringToLong(task.date + " " + task.time)

        alarmManager.set(AlarmManager.RTC_WAKEUP, newAlarmTime - (2*1000), newPendingIntent)
    }

}