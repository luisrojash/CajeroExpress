package pe.farmacias.peruanas.cajeroexpress.principal.perfil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.db.CajeroDB
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerfilRepository @Inject constructor(
    private val db: CajeroDB
) {

    fun obtenerConteoCarrito(): LiveData<Resource<String>> {
        val data = MediatorLiveData<Resource<String>>()
        data.value = Resource.loading(null)
        data.addSource(db.carritoDao().obtenerConteoCarrito()) {
            if (it != null) {
                data.value = Resource.success(it)
            }
        }
        return data
    }

    fun obtenerConteoOrden(): LiveData<Resource<String>> {
        val data = MediatorLiveData<Resource<String>>()
        data.value = Resource.loading(null)
        data.addSource(db.ordenDao().obtenerConteoOrden()) {
            if (it != null) {
                data.value = Resource.success(it)
            }
        }
        return data
    }

}