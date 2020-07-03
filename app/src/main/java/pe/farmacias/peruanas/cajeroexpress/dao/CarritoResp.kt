package pe.farmacias.peruanas.cajeroexpress.dao

object CarritoResp {

    data class ObtenerCantidadTotal(
        val cantidadTotalLista: String?
    )

    data class ObtenerCantidadYPrecioTotal(
        val cantidadProducto: String,
        val precioTotalProducto: String,
        val codigoProducto: String,
        val idCarrito: String,
        val precioProducto: String
    )

    data class ObtenerCarrito(
        val codigoProducto: String,
        val nombreProducto: String,
        val cantidadProducto: String,
        val imagenProducto: String,
        val precioProducto: String,
        val idCarrito: String,
        val descripcionCorta: String
    )

    data class ObtenerOrdenDetalle(
        val nombreProducto: String,
        val descripcionCortaProducto: String,
        val precioProducto: String,
        val productosImagen: String
    )

}