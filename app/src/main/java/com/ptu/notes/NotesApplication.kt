package com.ptu.notes

import android.app.Application
import androidx.multidex.MultiDexApplication
import io.realm.Realm
import io.realm.RealmConfiguration

class NotesApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build())
    }
}