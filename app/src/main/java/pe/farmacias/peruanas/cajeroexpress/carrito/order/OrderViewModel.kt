package pe.farmacias.peruanas.cajeroexpress.carrito.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.Dispatchers
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.model.Orden

import javax.inject.Inject

class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    private val orden = MutableLiveData<Orden>()

    fun setOrder(input: Orden) {
        orden.value = input
    }

    var guardarOrden = orden.switchMap { orden ->
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            emit(repository.guardarOrden(orden))
        }
    }
}