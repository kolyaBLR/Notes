package com.ptu.notes.home.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ptu.notes.NoteViewModel
import com.ptu.notes.R
import com.ptu.notes.db.ItemNote
import com.ptu.notes.edit.EditNoteActivity
import com.ptu.notes.home.HomeActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesFragment : Fragment() {

    companion object {
        const val REQUEST_CODE = 2354
        fun newBundle(isPush: Boolean, noteId: String?) = Bundle().apply {
            putBoolean(HomeActivity.IS_PUSH_KEY, isPush)
            putString(HomeActivity.NOTE_ID_KEY, noteId)
        }
    }

    private lateinit var adapter: NotesAdapter
    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_notes, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)

        homeRecycler.layoutManager = LinearLayoutManager(context)
        adapter = NotesAdapter(this, viewModel.getActiveNotes())
        adapter.listener = onCompletedClickListener
        homeRecycler.adapter = adapter

        newNoteButton.setOnClickListener {
            startActivityForResult(
                (EditNoteActivity.newIntent(it.context)),
                REQUEST_CODE
            )
        }
        if (arguments?.getBoolean(HomeActivity.IS_PUSH_KEY, false) == true) {
            arguments?.getString(HomeActivity.NOTE_ID_KEY)?.let {
                startActivityForResult(EditNoteActivity.newIntent(view.context, it), REQUEST_CODE)
            }
        }
    }

    private val onCompletedClickListener = object : NotesAdapter.Listener {
        override fun onCompletedClick(position: Int, noteId: String) {
            viewModel.completed(noteId, Realm.Transaction.OnSuccess {
                adapter.setData(viewModel.getActiveNotes())
                adapter.notifyItemRemoved(position)
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, d: Intent?) {
        super.onActivityResult(requestCode, resultCode, d)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            adapter.setData(viewModel.getActiveNotes())
            homeRecycler.adapter?.notifyDataSetChanged()
        }
    }
}
