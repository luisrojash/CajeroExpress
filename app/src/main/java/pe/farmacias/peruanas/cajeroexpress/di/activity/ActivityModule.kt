package pe.farmacias.peruanas.cajeroexpress.di.activity

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pe.farmacias.peruanas.cajeroexpress.carrito.CarritoActivity
import pe.farmacias.peruanas.cajeroexpress.detalles.DetallesActivity
import pe.farmacias.peruanas.cajeroexpress.di.ViewModelModule
import pe.farmacias.peruanas.cajeroexpress.login.LoginActivity
import pe.farmacias.peruanas.cajeroexpress.orden.OrdenActivity
import pe.farmacias.peruanas.cajeroexpress.principal.PrincipalActivity
import pe.farmacias.peruanas.cajeroexpress.prodcat.ProdCatActivity


@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeCarritoActivity(): CarritoActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeDetallesActivity(): DetallesActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributePrincipalActivity(): PrincipalActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeProdCatActivity(): ProdCatActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeOrdenActivity(): OrdenActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeLoginActivity(): LoginActivity


}
