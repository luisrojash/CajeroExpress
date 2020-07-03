package pe.farmacias.peruanas.cajeroexpress.di.base

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import pe.farmacias.peruanas.cajeroexpress.CajeroApp
import pe.farmacias.peruanas.cajeroexpress.di.AppModule
import pe.farmacias.peruanas.cajeroexpress.di.activity.ActivityModule
import pe.farmacias.peruanas.cajeroexpress.di.fragment.FragmentModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityModule::class,
        FragmentModule::class]
)
interface AppComponent : AndroidInjector<CajeroApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}