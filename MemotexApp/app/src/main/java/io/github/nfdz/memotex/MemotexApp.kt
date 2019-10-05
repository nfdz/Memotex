package io.github.nfdz.memotex

import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber


class MemotexApp : MultiDexApplication() {

    private val DB_NAME = "memotex.realm"
    private val SCHEMA_VERSION = 1L

    override fun onCreate() {
        super.onCreate()
        setupLogger()
        setupRealm()
        setupCrashlytics()
//        setupAds()
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
            .schemaVersion(SCHEMA_VERSION)
        if (!BuildConfig.DEBUG) {
            bld.deleteRealmIfMigrationNeeded()
        }
        return bld.build()
    }

    private fun setupCrashlytics() {
        // Set up Crashlytics, disabled for debug builds
        val crashlyticsKit = Crashlytics.Builder()
            .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
            .build()
        // Initialize Fabric with the debug-disabled crashlytics
        Fabric.with(this, crashlyticsKit)

        // Debug
        //        Crashlytics crashlyticsKit = new Crashlytics.Builder()
        //                .core(new CrashlyticsCore.Builder().build())
        //                .build();
        //        final Fabric fabric = new Fabric.Builder(this)
        //                .kits(crashlyticsKit)
        //                .debuggable(true)
        //                .build();
        //        Fabric.with(fabric);
    }

//    private fun setupAds() {
//        MobileAds.initialize(this, BuildConfig.AppAdId)
//    }

}