package com.ptu.notes

import androidx.lifecycle.ViewModel
import com.ptu.notes.db.ItemNote
import io.realm.Realm
import java.util.*

class NoteViewModel : ViewModel() {

    private var id: String? = null
    private var item: ItemNote? = null

    fun getSyncNote(id: String?) = if (id == null) null else
        Realm.getDefaultInstance().where(ItemNote::class.java).equalTo("id", id).findFirst()

    fun getActiveNotes() =
        Realm.getDefaultInstance().where(ItemNote::class.java).sort("date").equalTo("completed", false).findAll()!!

    fun save(id: String?, title: String, calendar: Calendar, onSuccess: (noteId: String) -> Unit) {
        var actualId: String? = null
        Realm.getDefaultInstance().executeTransactionAsync(Realm.Transaction {
            val note = ItemNote().apply {
                this.id = id ?: UUID.randomUUID().toString()
                this.title = title
                this.date = calendar.time
                actualId = this.id
            }
            it.copyToRealmOrUpdate(note)
        }, Realm.Transaction.OnSuccess { actualId?.let(onSuccess) })
    }

    fun delete(id: String?, onSuccess: Realm.Transaction.OnSuccess) {
        Realm.getDefaultInstance().executeTransactionAsync(Realm.Transaction { realm ->
            id?.let { realm.where(ItemNote::class.java).equalTo("id", id).findFirst()?.deleteFromRealm() }
        }, onSuccess)
    }

    fun completed(id: String?, onSuccess: Realm.Transaction.OnSuccess) {
        Realm.getDefaultInstance().executeTransactionAsync(Realm.Transaction { realm ->
            id?.let { realm.where(ItemNote::class.java).equalTo("id", id).findFirst()?.completed = true }
        }, onSuccess)
    }
}