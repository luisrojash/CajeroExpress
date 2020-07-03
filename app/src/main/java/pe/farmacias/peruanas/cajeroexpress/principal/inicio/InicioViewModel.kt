package pe.farmacias.peruanas.cajeroexpress.principal.inicio

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.model.CategoriasUi
import pe.farmacias.peruanas.cajeroexpress.principal.inicio.model.TopProducto
import javax.inject.Inject

class InicioViewModel @Inject constructor(
    private val repository: InicioRepository
) : ViewModel() {

    val obtenerListaTopProductos: LiveData<Resource<List<TopProducto>>> =
        repository.obtenerListaTopProducto()

    val obtenerListaCategorias: LiveData<Resource<List<CategoriasUi>>> =
        repository.obtenerListaCategorias()

}