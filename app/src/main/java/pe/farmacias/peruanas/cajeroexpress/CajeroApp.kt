package pe.farmacias.peruanas.cajeroexpress

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

import pe.farmacias.peruanas.cajeroexpress.di.base.DaggerAppComponent


class CajeroApp : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}