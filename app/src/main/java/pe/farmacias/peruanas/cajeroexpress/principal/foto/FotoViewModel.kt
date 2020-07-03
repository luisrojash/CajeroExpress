package pe.farmacias.peruanas.cajeroexpress.principal.foto

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi
import timber.log.Timber
import javax.inject.Inject

class FotoViewModel @Inject constructor(
    private val repository: FotoRepository
) : ViewModel() {

    private val codigoProduto = MutableLiveData<String>()
    private val productosUi = MutableLiveData<ProductosUi>()
    val mostrarTotalCantidad = MutableLiveData<Int>()
    val mostrarMensaje = MutableLiveData<String>()

    var tipoEstado: String = ""

    var totalCantidadProducto: String = "1"
    fun bundleTipoEstado(tipoEstado: String?) {
        this.tipoEstado = tipoEstado.toString()
    }


    fun obteniendoCodigo(input: String) {
        codigoProduto.value = input
    }

    fun obteniendoProductos(producto: ProductosUi) {
        producto.cantidadProducto = totalCantidadProducto
        productosUi.value = producto
    }

    /*var handleResult = codigoProduto.switchMap { codigoProduto ->
        liveData(Dispatchers.IO) {
            System.out.println(codigoProduto)
            emit(Resource.loading(null))
            emit(repository.obtenerProducto(codigoProduto))
        }
    }*/

    val handleResult: LiveData<Resource<List<ProductosUi>>> =
        Transformations.switchMap(codigoProduto, ::resultadoLista)

    private fun resultadoLista(codigoProducto: String) = repository.obtenerProducto(codigoProducto)


    var guardarCarrito = productosUi.switchMap { productosUi ->
        liveData(Dispatchers.IO) {
            System.out.println(productosUi)
            emit(Resource.loading(null))
            emit(repository.agregarCarrito(productosUi))
        }
    }

    fun onClickSumar(cantidad: String) {
        val cantidadInt: Int = cantidad.toInt()
        val totalCantidad = cantidadInt.plus(1)
        totalCantidadProducto = totalCantidad.toString()
        mostrarTotalCantidad.postValue(totalCantidad)
    }

    fun onClickRestar(cantidad: String) {
        val cantidadInt: Int = cantidad.toInt()
        val totalCantidad = cantidadInt.minus(1)
        if (totalCantidad == 0) {
            mostrarMensaje.postValue("No se permite valores nulos")
            return
        }
        totalCantidadProducto = totalCantidad.toString()
        mostrarTotalCantidad.postValue(totalCantidad)

    }
}