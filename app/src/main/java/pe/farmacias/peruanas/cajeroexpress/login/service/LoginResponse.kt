package pe.farmacias.peruanas.cajeroexpress.login.service

import pe.farmacias.peruanas.cajeroexpress.model.Categoria
import pe.farmacias.peruanas.cajeroexpress.model.Productos

object LoginResponse {

    data class LoginProductosResponse(
        val productoLista: List<Productos>
    )

    data class LoginCategoriasResponse(
        val categoriaLista: List<Categoria>
    )
}



