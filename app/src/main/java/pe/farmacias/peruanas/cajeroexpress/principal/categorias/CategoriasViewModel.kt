package pe.farmacias.peruanas.cajeroexpress.principal.categorias

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.model.Categoria
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.model.CategoriasUi
import javax.inject.Inject

class CategoriasViewModel @Inject constructor(
    private val repository: CategoriasRepository
) :
    ViewModel() {


    val obtenerListaCategorias: LiveData<Resource<List<CategoriasUi>>> =
        repository.obtenerListaCategorias()
}