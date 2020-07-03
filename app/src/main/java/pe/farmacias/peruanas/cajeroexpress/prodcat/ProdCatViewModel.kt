package pe.farmacias.peruanas.cajeroexpress.prodcat

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi
import javax.inject.Inject

class ProdCatViewModel @Inject constructor(
    private val repository: ProdCatRepository
) : ViewModel() {

    private val idCategoria = MutableLiveData<String>()

    fun obteniendoProductosUi(input: String) {
        idCategoria.value = input
    }

    private val productosUi = MutableLiveData<ProductosUi>()

    fun obteniendoProductosUiObject(input: ProductosUi) {
        productosUi.value = input
    }


   /* var obtenerListaCatProd = idCategoria.switchMap { idCategoria ->
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            emit(repository.obtenerListaCategoriasProd(idCategoria))
        }
    }*/
   val obtenerListaCatProd: LiveData<Resource<List<ProductosUi>>> =
       Transformations.switchMap(idCategoria, ::resultadoLista)

    private fun resultadoLista(idCategoria: String) = repository.obtenerListaCategoriasProd(idCategoria)

    var clickAgregarCarrito = productosUi.switchMap { productos ->
        liveData(Dispatchers.IO) {
            System.out.println(productos)
            emit(Resource.loading(null))
            emit(repository.agregarCarrito(productos))
        }
    }

}