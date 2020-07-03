package pe.farmacias.peruanas.cajeroexpress.principal.productos

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.model.Productos
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi
import javax.inject.Inject

class ProductosViewModel @Inject constructor(
    private val repository: ProductosRepository
) :
    ViewModel() {

    private val productosUi = MutableLiveData<ProductosUi>()

    fun obteniendoProductosUi(input: ProductosUi) {
        productosUi.value = input
    }

    var clickAgregarCarrito = productosUi.switchMap { productos ->
        liveData(Dispatchers.IO) {
            System.out.println(productos)
            emit(Resource.loading(null))
            emit(repository.agregarCarrito(productos))
        }
    }

 /*   var obtenerListaProductos = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        emit(repository.obtenerListaProductos(20,2))
    }*/

    val obtenerListaProductos: LiveData<Resource<List<ProductosUi>>> = repository.obtenerListaProductos(150,0)

}