package pe.farmacias.peruanas.cajeroexpress.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orden")
data class Orden(
    var fecha: String,
    var nombreUsuario: String,
    var nombreLocal: String,
    var totalSoles: String,
    var totalCantidad: String,
    var productoId: String,
    var referenciaId: String,
    var estado: String
) {
    @PrimaryKey(autoGenerate = true)
    var idOrden: Long? = null
}
