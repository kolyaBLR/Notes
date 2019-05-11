package com.ptu.notes.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ptu.notes.R
import kotlinx.android.synthetic.main.fragment_statitic.*

class StatisticFragment : Fragment() {

    private lateinit var viewModel: StatisticViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_statitic, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StatisticViewModel::class.java)

        viewModel.countClosedTasksToday.observe(this, Observer {
            countOfDay.text = getString(R.string.count_of_day, it)
        })
        viewModel.countClosedTasksYesterday.observe(this, Observer {
            countOfYesterday.text = getString(R.string.count_of_day, it)
        })
        viewModel.countClosedTasksWeek.observe(this, Observer {
            countOfWeek.text = getString(R.string.count_of_day, it)
        })

        mem.setImageResource(R.drawable.mem)
    }
}
