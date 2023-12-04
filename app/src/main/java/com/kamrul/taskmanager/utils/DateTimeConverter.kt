package com.kamrul.taskmanager.utils

import android.annotation.SuppressLint
import android.util.Log
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateTimeConverter {

    companion object {

        fun formatTimeToAmPm(time: String): String {
            val timeSplit = time.split(":")

            val hour = timeSplit[0].toInt()
            val minute = timeSplit[1]
            val amPm: String
            val formattedHour: Int

            if (hour < 12) {
                amPm = "AM"
                formattedHour = if (hour == 0) 12 else hour
            } else {
                amPm = "PM"
                formattedHour = if (hour == 12) 12 else hour - 12
            }
            val hourString = if(formattedHour<10){
                "0$formattedHour"
            }else{
                formattedHour.toString()
            }

            return "$hourString:$minute $amPm"
        }

        fun formatTimeTo24Hour(time: String): String {

            if (time.isEmpty()) {
                return "00:00"
            }
            val timeSplit = time.split(":", " ")
            var hour = timeSplit[0].toInt()
            val minute = timeSplit[1].toInt()
            val amPm = timeSplit[2]

            if (amPm == "PM" && hour != 12) {
                hour += 12
            } else if (amPm == "AM" && hour == 12) {
                hour = 0
            }

            val returnTime = if (hour < 10 && minute < 10) {
                "0$hour:0$minute"
            } else if (hour < 10) {
                "0$hour:$minute"
            } else if (minute < 10) {
                "$hour:0$minute"
            } else {
                "$hour:$minute"
            }

            return returnTime
        }

        fun dateToShowDate(dateString: String): String {
            Log.d("date_tag", "onBindViewHolder: Date String: $dateString")

            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
            try {
                val date = LocalDate.parse(dateString, formatter)

                val dayOfWeek = DateTimeFormatter.ofPattern("EEE", Locale.getDefault()).format(date)
                val month = DateTimeFormatter.ofPattern("MMM", Locale.getDefault()).format(date)
                val dayOfMonth = date.dayOfMonth

                return "$dayOfWeek$$dayOfMonth$$month"
            } catch (e: Exception) {
                Log.e("date_tag", "dateToShowDate: Error: $e")
            }
            return ""
        }

        fun todayDateToLong(): Long {
            return Date().time
        }

        @SuppressLint("SimpleDateFormat")
        fun dateStringToLong(dateString: String): Long {
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val date = formatter.parse(dateString)
            return date?.time ?: 0
        }

        @SuppressLint("SimpleDateFormat")
        fun longToMonth(time: Long): String {
            // val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val formatter = SimpleDateFormat("dd-MM-yyyy")

            val date = Date(time)

            return getMonth(formatter.format(date))
        }

        private fun getMonth(date: String): String{
            val dateSplit = date.split("-"," ")
            val month = dateSplit[1]
            val year = dateSplit[2]
            return when(month){
                "01" -> "January, $year"
                "02" -> "February, $year"
                "03" -> "March, $year"
                "04" -> "April, $year"
                "05" -> "May, $year"
                "06" -> "June, $year"
                "07" -> "July, $year"
                "08" -> "August, $year"
                "09" -> "September, $year"
                "10" -> "October, $year"
                "11" -> "November, $year"
                "12" -> "December, $year"

                else -> "January, $year"
            }
        }

        fun dateStringToDuration(dateString: String): Long {
            Log.d("date_tag", "onBindViewHolder: Date String: $dateString")

            val taskDateLong = dateStringToLong(dateString)
            val currentDateLong = todayDateToLong()
            return taskDateLong - currentDateLong
        }
    }
}