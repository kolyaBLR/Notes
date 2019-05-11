package com.ptu.notes.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ptu.notes.R
import com.ptu.notes.home.notes.NotesFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    companion object {
        const val IS_PUSH_KEY = "IS_PUSH_KEY"
        const val NOTE_ID_KEY = "NOTE_ID_KEY"
        fun newIntent(context: Context, isPush: Boolean, noteId: String) = Intent(context, HomeActivity::class.java)
            .putExtra(IS_PUSH_KEY, isPush).putExtra(NOTE_ID_KEY, noteId)
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        adapter = HomeAdapter(this, supportFragmentManager)
        adapter.notesBundle = NotesFragment.newBundle(
            intent.getBooleanExtra(IS_PUSH_KEY, false),
            intent.getStringExtra(NOTE_ID_KEY)
        )
        homeViewPager.adapter = adapter
        tabLayout.setupWithViewPager(homeViewPager, true)
    }
}
