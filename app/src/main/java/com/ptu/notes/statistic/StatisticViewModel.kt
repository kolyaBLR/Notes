package com.ptu.notes.statistic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ptu.notes.db.ItemNote
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import java.util.*

class StatisticViewModel : ViewModel() {

    private val items = Realm.getDefaultInstance().where(ItemNote::class.java).findAll()

    val countClosedTasksToday = MutableLiveData<Int>()
    val countClosedTasksYesterday = MutableLiveData<Int>()
    val countClosedTasksWeek = MutableLiveData<Int>()

    private val changeListener = RealmChangeListener<RealmResults<ItemNote>> {
        countClosedTasksToday.value = getCountClosedTasksToday()
        countClosedTasksYesterday.value = getCountClosedTasksYesterday()
        countClosedTasksWeek.value = getCountClosedTasksWeek()
    }

    init {
        items.addChangeListener(changeListener)
        changeListener.onChange(items)
    }

    private fun getCountClosedTasksToday(): Int {
        val actual = Calendar.getInstance()
        return items.filter { it.completed }
            .map {
                val calendar = Calendar.getInstance()
                calendar.time = it.date
                calendar
            }.filter { actual.getYear() == it.getYear() }
            .filter { actual.getMonth() == it.getMonth() }
            .filter { actual.getDayOfMonth() == it.getDayOfMonth() }.size
    }

    private fun getCountClosedTasksYesterday(): Int {
        val actual = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }
        return items.filter { it.completed }
            .map {
                val calendar = Calendar.getInstance()
                calendar.time = it.date
                calendar
            }.filter { actual.getYear() == it.getYear() }
            .filter { actual.getDayOfYear() == it.getDayOfYear() }.size
    }

    private fun getCountClosedTasksWeek(): Int {
        val actual = Calendar.getInstance()
        return items.filter { it.completed }
            .map {
                val calendar = Calendar.getInstance()
                calendar.time = it.date
                calendar
            }.filter { actual.getYear() == it.getYear() }
            .filter { actual.getMonth() == it.getMonth() }
            .filter { actual.getWeekOfMonth() == it.getWeekOfMonth() }.size
    }

    private fun Calendar.getYear() = get(Calendar.YEAR)
    private fun Calendar.getWeekOfMonth() = get(Calendar.WEEK_OF_MONTH)
    private fun Calendar.getMonth() = get(Calendar.MONTH)
    private fun Calendar.getDayOfMonth() = get(Calendar.DAY_OF_MONTH)
    private fun Calendar.getDayOfYear() = get(Calendar.DAY_OF_YEAR)
}