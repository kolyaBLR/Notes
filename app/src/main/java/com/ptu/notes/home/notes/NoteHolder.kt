package com.ptu.notes.home.notes

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ptu.notes.db.ItemNote
import com.ptu.notes.edit.EditNoteActivity
import kotlinx.android.synthetic.main.item_note_preview.view.*
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
class NoteHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val timeFormat = SimpleDateFormat("HH:mm")
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    fun bind(fragment: Fragment, item: ItemNote, onCompletedClick: View.OnClickListener) {
        itemView.apply {
            titleEditText
            titleEditText.text = item.title
            time.text = timeFormat.format(item.date)
            date.text = dateFormat.format(item.date)

            completed.setOnClickListener(onCompletedClick)
            cardView.setOnClickListener {
                fragment.startActivityForResult(
                    EditNoteActivity.newIntent(it.context, item.id),
                    NotesFragment.REQUEST_CODE
                )
            }
        }
    }
}