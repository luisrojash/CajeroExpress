package pe.farmacias.peruanas.cajeroexpress.model

import androidx.room.Entity

@Entity(tableName = "usuario", primaryKeys = ["idUsuario"])
data class Usuario(
    val idUsuario: String,
    val usuario: String,
    val claveUsuario: String,
    val nombreUsuario: String,
    val fotoUsuario: String,
    val dniUsuario: String,
    val codigoLocal: String
)
