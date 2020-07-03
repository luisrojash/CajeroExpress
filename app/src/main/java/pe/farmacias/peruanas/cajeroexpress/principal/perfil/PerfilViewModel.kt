package pe.farmacias.peruanas.cajeroexpress.principal.perfil

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import javax.inject.Inject

class PerfilViewModel @Inject constructor(
    private val repository: PerfilRepository
) : ViewModel() {

    val carritoConteo: LiveData<Resource<String>> = repository.obtenerConteoCarrito()

    val ordenConteo: LiveData<Resource<String>> = repository.obtenerConteoOrden()

}