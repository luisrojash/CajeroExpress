package pe.farmacias.peruanas.cajeroexpress.principal.productos.model

class ProductosUi(

    val id: Long,

    val productoId: String,

    val esLam: String?,

    val nombre: String,

    val descCorta: String?,

    val descLarga: String?,

    val categoriaId: String?,

    val categoriaDesc: String?,

    val categoriaId2: String?,

    val categoriaDesc2: String?,

    val categoriaId3: String?,

    val categoriaDesc3: String?,

    val imageL: String?,

    val imageM: String,

    val imageX: String?,

    val precio: String?,


    val porcentaje: String?,

    val preciotachado: String,

    var cantidadProducto: String
)
