package pe.farmacias.peruanas.cajeroexpress.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categoria", primaryKeys = ["codCategoria"])
data class Categoria(
    var codCategoria: String,
    var categoria: String,
    var imagen: String,
    var categoriaPadre: String
)
