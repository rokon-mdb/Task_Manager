package com.kamrul.taskmanager.ui.activity

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ActivityAlarmBinding
import com.kamrul.taskmanager.utils.ArgumentKey
import com.kamrul.taskmanager.utils.DateTimeConverter

class AlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmBinding

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskData = intent.getStringExtra(ArgumentKey.ALARM_TASK.name)?.split("$")

        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (taskData != null) {
            //val title
            binding.tvTitle.text = taskData[0] //data?.title
            binding.tvDesc.text = taskData[1] //data?.description
            binding.tvDateTime.text = taskData[2] + "   " + taskData[3]
        }

        binding.btnCancel.setOnClickListener {
            mediaPlayer.stop()
            finish()
        }

        // Initialize MediaPlayer with the audio file
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.alarm_tone)
        mediaPlayer.isLooping = true

        // Start playing the audio
        mediaPlayer.start()

        turnOnScreen()

    }

    private fun turnOnScreen() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "AlarmActivity:wakeLock"
        )

        // Acquire the wake lock
        wakeLock.acquire()

        // If your target SDK is 27 or higher, you may need to use the FLAG_KEEP_SCREEN_ON flag as well
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )

        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }

        // Release the wake lock when the activity is destroyed
        wakeLock.release()
    }
}