package pe.farmacias.peruanas.cajeroexpress.model

import androidx.room.Entity

@Entity(tableName = "productos", primaryKeys = ["productoId"])
data class Productos(
    val id: String,
    val productoId: String,
    val esLam: String,
    val nombre: String,
    val descCorta: String,
    val descLarga: String?,
    val precio: String,
    val categoriaId: String,
    val categoriaDesc: String,
    val imageL: String,
    val imageM: String,
    val imageX: String,
    val categoriaId2: String?,
    val categoriaDesc2: String?,
    val categoriaId3: String?,
    val categoriaDesc3: String?
)