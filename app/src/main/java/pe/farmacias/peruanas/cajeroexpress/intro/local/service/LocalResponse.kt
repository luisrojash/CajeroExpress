package pe.farmacias.peruanas.cajeroexpress.intro.local.service

import pe.farmacias.peruanas.cajeroexpress.model.Productos

object LocalResponse {

    data class LocalResponseList(
        val productoLista: List<Productos>
    )
}