package com.ptu.notes.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ptu.notes.NoteViewModel
import com.ptu.notes.R
import com.ptu.notes.home.HomeActivity

class PushReceiver : BroadcastReceiver() {

    companion object {
        fun setAlarm(context: Context, noteId: String, time: Long) {
            val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val broadcast = PendingIntent.getBroadcast(
                context, 1, newIntent(context, noteId), PendingIntent.FLAG_UPDATE_CURRENT
            )
            manager.set(AlarmManager.RTC_WAKEUP, time, broadcast)
        }

        private fun newIntent(context: Context, noteId: String) =
            Intent(context, PushReceiver::class.java).putExtra(HomeActivity.NOTE_ID_KEY, noteId)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val noteId = intent.getStringExtra(HomeActivity.NOTE_ID_KEY)
        NoteViewModel().getSyncNote(noteId)?.takeIf { !it.completed }.let { note ->
            Notification(context).createNotification(
                context.getString(R.string.notification_title),
                note?.title ?: "",
                true,
                noteId
            )
        }
    }
}