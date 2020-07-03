package pe.farmacias.peruanas.cajeroexpress.orden.model

import pe.farmacias.peruanas.cajeroexpress.dao.CarritoResp

data class OrdenUi(
    var fecha: String,
    var nombreUsuario: String,
    var nombreLocal: String,
    var totalSoles: String,
    var totalCantidad: String,
    var productoId: String,
    var referenciaId: String,
    var estado: String,
    var conteo: String,
    var status: Boolean
) {
    var ordenDetallesList: MutableList<CarritoResp.ObtenerOrdenDetalle> = mutableListOf()
}
