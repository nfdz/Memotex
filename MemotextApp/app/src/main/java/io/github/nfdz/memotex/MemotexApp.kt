package io.github.nfdz.memotex

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber


class MemotexApp : Application() {

    private val DB_NAME = "memotex.realm"
    private val SCHEMA_VERSION_NAME = 1L

    override fun onCreate() {
        super.onCreate()
        setupLogger()
        setupRealm()
    }

    private fun setupLogger() {
        if (BuildConfig.DEBUG) {
            Timber.uprootAll()
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupRealm() {
        Realm.init(this)
        Realm.setDefaultConfiguration(getRealmConfiguration())
    }

    private fun getRealmConfiguration(): RealmConfiguration {
        val bld = RealmConfiguration.Builder()
            .name(DB_NAME)
            .schemaVersion(SCHEMA_VERSION_NAME)
        if (!BuildConfig.DEBUG) {
            bld.deleteRealmIfMigrationNeeded()
        }
        return bld.build()
    }

}