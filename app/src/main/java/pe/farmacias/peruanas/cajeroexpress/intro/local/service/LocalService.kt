package pe.farmacias.peruanas.cajeroexpress.intro.local.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response

interface LocalService {

    @GET("v1/producto/codLocal/{codLocal}")
    suspend fun obtenerListaProductos(@Path("codLocal") codLocal: String): Response<LocalResponse.LocalResponseList>
}