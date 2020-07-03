package pe.farmacias.peruanas.cajeroexpress.di.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pe.farmacias.peruanas.cajeroexpress.carrito.order.OrderFragment
import pe.farmacias.peruanas.cajeroexpress.di.ViewModelModule
import pe.farmacias.peruanas.cajeroexpress.intro.local.LocalFragment
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.CategoriasFragment
import pe.farmacias.peruanas.cajeroexpress.principal.foto.FotoFragment
import pe.farmacias.peruanas.cajeroexpress.principal.inicio.InicioFragment
import pe.farmacias.peruanas.cajeroexpress.principal.perfil.PerfilFragment
import pe.farmacias.peruanas.cajeroexpress.principal.productos.ProductosFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    internal abstract fun contributeInicioFragment(): InicioFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    internal abstract fun contributeProductosFragment(): ProductosFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    internal abstract fun contributeCategoriasFragment(): CategoriasFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    internal abstract fun contributeOrderFragment(): OrderFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    internal abstract fun contributePerfilFragment(): PerfilFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    internal abstract fun contributeFotoFragment(): FotoFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    internal abstract fun contributeLocalFragment(): LocalFragment
}