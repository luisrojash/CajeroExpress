package pe.farmacias.peruanas.cajeroexpress.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pe.farmacias.peruanas.cajeroexpress.carrito.CarritoViewModel
import pe.farmacias.peruanas.cajeroexpress.carrito.order.OrderViewModel
import pe.farmacias.peruanas.cajeroexpress.detalles.DetallesViewModel
import pe.farmacias.peruanas.cajeroexpress.di.base.ViewModelFactory
import pe.farmacias.peruanas.cajeroexpress.di.base.ViewModelKey
import pe.farmacias.peruanas.cajeroexpress.intro.local.LocalViewModel
import pe.farmacias.peruanas.cajeroexpress.login.LoginViewModel
import pe.farmacias.peruanas.cajeroexpress.orden.OrdenViewModel
import pe.farmacias.peruanas.cajeroexpress.principal.PrincipalViewModel
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.CategoriasViewModel
import pe.farmacias.peruanas.cajeroexpress.principal.foto.FotoViewModel
import pe.farmacias.peruanas.cajeroexpress.principal.inicio.InicioViewModel
import pe.farmacias.peruanas.cajeroexpress.principal.perfil.PerfilViewModel
import pe.farmacias.peruanas.cajeroexpress.principal.productos.ProductosViewModel
import pe.farmacias.peruanas.cajeroexpress.prodcat.ProdCatViewModel


@Module
abstract class ViewModelModule {
    /*Fragmentos*/
    @Binds
    @IntoMap
    @ViewModelKey(InicioViewModel::class)
    abstract fun bindInicioViewModel(viewModel: InicioViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(ProductosViewModel::class)
    abstract fun bindProductosViewModel(viewModel: ProductosViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(CategoriasViewModel::class)
    abstract fun bindCategoriasViewModel(viewModel: CategoriasViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(OrderViewModel::class)
    abstract fun bindOrderViewModel(viewModel: OrderViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(PerfilViewModel::class)
    abstract fun bindPerfilViewModel(viewModel: PerfilViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(FotoViewModel::class)
    abstract fun bindFotoViewModel(viewModel: FotoViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(LocalViewModel::class)
    abstract fun bindLocalViewModel(viewModel: LocalViewModel): ViewModel

    /*Actividad*/

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CarritoViewModel::class)
    abstract fun bindCarritoViewModel(viewModel: CarritoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetallesViewModel::class)
    abstract fun bindDetallesViewModel(viewModel: DetallesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PrincipalViewModel::class)
    abstract fun bindPrincipalViewModel(viewModel: PrincipalViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProdCatViewModel::class)
    abstract fun bindProdCatViewModel(viewModel: ProdCatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrdenViewModel::class)
    abstract fun bindOrdenViewModel(viewModel: OrdenViewModel): ViewModel



    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


}