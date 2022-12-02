package com.FaceShift.lab4


import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.FaceShift.lab4.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 18)
        calendar.set(Calendar.MINUTE, 30)
        calendar.set(Calendar.SECOND, 0)
        val intentt = Intent(this, ReminderBroadcast::class.java)
        val pendingIntentt: PendingIntent = PendingIntent.getBroadcast(this, 0, intentt, PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 1, pendingIntentt)

        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, LogInActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

//        var builder = NotificationCompat.Builder(this, getString(R.string.channel_name))
//            .setSmallIcon(androidx.core.R.drawable.notification_icon_background)
//            .setContentTitle("Take daily picture!")
//            .setContentText("Don't forget to take your daily picture!")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//
//        with(NotificationManagerCompat.from(this)) {
//            notify(0, builder.build())
//        }

        startActivity(Intent(this, LogInActivity::class.java))
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_title)
            val descriptionText = getString(R.string.notification_text)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(getString(R.string.channel_name), name, importance).apply {
                    description = descriptionText
                }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}