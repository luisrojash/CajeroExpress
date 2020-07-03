package pe.farmacias.peruanas.cajeroexpress.principal.categorias

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.ResponseHandler
import pe.farmacias.peruanas.cajeroexpress.db.CajeroDB
import pe.farmacias.peruanas.cajeroexpress.model.Categoria
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.model.CategoriasUi
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.service.CategoriasResponse
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.service.CategoriasSevice
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriasRepository @Inject constructor(
    private val service: CategoriasSevice,
    private val responseHandler: ResponseHandler,
    private val db: CajeroDB
) {

 /*   suspend fun obtenerListaCategorias(): Resource<CategoriasResponse> {
        return try {
            val response = service.obtenerListaCategorias()
            return responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }

    }*/

    fun obtenerListaCategorias(): LiveData<Resource<List<CategoriasUi>>> {
        val data = MediatorLiveData<Resource<List<CategoriasUi>>>()
        data.value = Resource.loading(null)
        data.addSource(db.categoriasDao().obtenerListaCategoriaAll("")) {
            if (it != null) {
                data.value = Resource.success(setListaCategoria(it))
            }
        }
        return data
    }

    private fun setListaCategoria(it: List<Categoria>): MutableList<CategoriasUi> {
        val listaCategorias: MutableList<CategoriasUi> = ArrayList()
        for (cat in it) {
            Timber.d("cat : %s ",cat.categoria)
            val categoriasUi = CategoriasUi(
                cat.codCategoria,
                cat.categoria,
                cat.imagen,
                "4"
            )
            listaCategorias.add(categoriasUi)
        }
        return listaCategorias
    }

}