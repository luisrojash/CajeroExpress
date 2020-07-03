package pe.farmacias.peruanas.cajeroexpress.intro.local

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import timber.log.Timber
import javax.inject.Inject

class LocalViewModel @Inject constructor(
    private val repository: LocalRepository
) : ViewModel() {


    private val codigoLocal = MutableLiveData<String>()

    var validarLocal = codigoLocal.switchMap { codLocal ->
        liveData(Dispatchers.IO) {
            System.out.println(codLocal)
            emit(Resource.loading(null))
            emit(repository.obtenerProductos(codLocal))
        }
    }

    fun setCodigoLocal(nombreLocal: String) {
        Timber.d("setCodigoLocal : %s ", nombreLocal)
        codigoLocal.value = nombreLocal
    }
}