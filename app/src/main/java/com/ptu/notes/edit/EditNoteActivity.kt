package com.ptu.notes.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.ptu.notes.R
import com.ptu.notes.NoteViewModel
import com.ptu.notes.notification.PushReceiver
import io.realm.Realm
import kotlinx.android.synthetic.main.item_note.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class EditNoteActivity : AppCompatActivity() {

    companion object {
        private const val ITEM_ID_KEY = "ITEM_ID_KEY"
        fun newIntent(context: Context, itemId: String? = null) = Intent(context, EditNoteActivity::class.java)
            .putExtra(ITEM_ID_KEY, itemId)
    }

    private lateinit var calendar: Calendar
    private var id: String? = null

    private val timeFormat = SimpleDateFormat("HH:mm")
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_note)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        id = intent.getStringExtra(ITEM_ID_KEY)
        viewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)

        val note = viewModel.getSyncNote(id)

        if (note != null) {
            calendar = Calendar.getInstance()
            calendar.time = note.date
        } else {
            calendar = Calendar.getInstance().apply {
                add(Calendar.HOUR_OF_DAY, 1)
                set(Calendar.MINUTE, 0)
            }
        }

        if (id == null) {
            remove.visibility = View.GONE
        }

        time.text = timeFormat.format(calendar.time)
        date.text = dateFormat.format(calendar.time)
        note?.title?.let { titleEditText.setText(it) }

        timeAction.setOnTouchListener { v, event -> time.onTouchEvent(event) }
        dateAction.setOnTouchListener { v, event -> date.onTouchEvent(event) }

        time.setOnClickListener {
            TimePickerDialog(
                this, timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        date.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        save.setOnClickListener {
            viewModel.save(id, titleEditText.text.toString(), calendar) {
                PushReceiver.setAlarm(this, it, calendar.timeInMillis)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        remove.setOnClickListener {
            AlertDialog.Builder(this).setTitle(R.string.remove_title)
                .setMessage(R.string.remove_message)
                .setNegativeButton(R.string.remove) { dialog, which ->
                    viewModel.delete(id, Realm.Transaction.OnSuccess {
                        setResult(Activity.RESULT_OK)
                        finish()
                    })
                }.setNeutralButton(R.string.cancel) { dialog, which ->

                }.create().show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
        time.text = timeFormat.format(calendar.time)
    }

    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
        date.text = dateFormat.format(calendar.time)
    }
}
