package com.ptu.notes.home.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ptu.notes.R
import com.ptu.notes.db.ItemNote

class NotesAdapter(private val fragment: Fragment, private var data: List<ItemNote>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listener: Listener? = null

    fun setData(data: List<ItemNote>) {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NoteHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_note_preview, parent, false)
    )

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val note = data[holder.adapterPosition]
        (holder as? NoteHolder)?.bind(fragment, note, View.OnClickListener {
            listener?.onCompletedClick(holder.adapterPosition, note.id)
        })
    }

    interface Listener {
        fun onCompletedClick(position: Int, noteId: String)
    }
}
