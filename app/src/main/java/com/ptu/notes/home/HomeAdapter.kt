package com.ptu.notes.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ptu.notes.R
import com.ptu.notes.home.notes.NotesFragment
import com.ptu.notes.statistic.StatisticFragment
import java.lang.NullPointerException

class HomeAdapter(context: Context, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    val titles = listOf(context.getString(R.string.notes), context.getString(R.string.statistics))
    private val fragments = arrayListOf<Fragment>()

    var notesBundle: Bundle? = null

    override fun getItem(position: Int) = fragments.getOrElse(position) {
        when (position) {
            0 -> {
                val fragment = NotesFragment()
                fragment.arguments = notesBundle
                fragments.add(position, fragment)
                fragment
            }
            1 -> {
                val fragment = StatisticFragment()
                fragments.add(position, fragment)
                fragment
            }
            else -> throw NullPointerException("not found")
        }
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int) = titles[position]
}
