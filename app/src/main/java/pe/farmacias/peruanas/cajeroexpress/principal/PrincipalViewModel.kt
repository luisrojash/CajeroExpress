package pe.farmacias.peruanas.cajeroexpress.principal

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi
import timber.log.Timber
import javax.inject.Inject

class PrincipalViewModel @Inject constructor(
    private val repository: PrincipalRepository
) : ViewModel() {

    val posicion = MutableLiveData<Int>()
    val textBuscar = MutableLiveData<String>()

    var posicionFragment: Int = 0

    val carritoConteo: LiveData<Resource<String>> = repository.obtenerConteoCarrito()


    fun initPosicionAdapter(it: Int) {
        this.posicionFragment = it
        when (it) {
            0 -> posicion.postValue(it)
            1 -> posicion.postValue(it)
            2 -> posicion.postValue(it)
            3 -> posicion.postValue(it)
            else -> Timber.d("")
        }
    }

    fun initBuscarTexto(textoBuscar: String) {
        Timber.d("initBuscarTexto: %s ", textoBuscar)
        textBuscar.value = textoBuscar
    }


    val obtenerLista: LiveData<Resource<List<ProductosUi>>> =
        Transformations.switchMap(textBuscar, ::resultadoLista)

    private fun resultadoLista(textoBuscar: String) = repository.obtenerListaProductos(textoBuscar)


}