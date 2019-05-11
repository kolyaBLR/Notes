package com.ptu.notes.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class ItemNote : RealmObject() {
    @PrimaryKey
    lateinit var id: String
    var title: String? = null
    var date: Date? = null
    var completed = false
}