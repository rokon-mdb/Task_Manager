package com.kamrul.taskmanager.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.kamrul.taskmanager.utils.ArgumentKey
import com.kamrul.taskmanager.utils.CompareKey
import com.kamrul.taskmanager.ui.activity.AlarmActivity

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent?.action
        Log.d("alarm_tag", "onReceive: call")

        if (action == CompareKey.ALARM_ACTION.name) {
            // Start MyActivity and specify the fragment to show
            val activityIntent = Intent(context, AlarmActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                // Assuming ArgumentKey.ALARM_TASK is a Parcelable object
                val alarmTask = intent.getStringExtra(ArgumentKey.ALARM_TASK.name)

                Log.d("alarm_tag", "onReceive: call: $alarmTask")


                // Check if alarmTask is not null before putting it as an extra
                if (alarmTask != null) {
                    putExtra(ArgumentKey.ALARM_TASK.name, alarmTask)
                }
            }

            context?.startActivity(activityIntent)
        }
    }
}

