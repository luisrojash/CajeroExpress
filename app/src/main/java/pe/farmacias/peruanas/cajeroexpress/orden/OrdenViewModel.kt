package pe.farmacias.peruanas.cajeroexpress.orden

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.model.Orden
import pe.farmacias.peruanas.cajeroexpress.orden.model.OrdenUi
import pe.izipay.izipaysdk.entidades.OperacionResult
import timber.log.Timber
import javax.inject.Inject

class OrdenViewModel @Inject constructor(
    repository: OrdenRepository
) : ViewModel() {

    val mutableAnularOrdenUi = MutableLiveData<OrdenUi>()
    var ordenUi: OrdenUi? = null

    fun onLongClickAnularOrden(it: OrdenUi) {
        this.ordenUi = it
    }

    fun setProductosUi() {
        mutableAnularOrdenUi.value = ordenUi
    }

    var actualizarOrden = mutableAnularOrdenUi.switchMap { ordenUi ->
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            emit(repository.actualizarOrden(ordenUi))
        }
    }

    val obtenerListaOrden: LiveData<Resource<List<OrdenUi>>> = repository.obtenerListaCarrito()


}