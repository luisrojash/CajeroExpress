package pe.farmacias.peruanas.cajeroexpress.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pe.farmacias.peruanas.cajeroexpress.dao.*
import pe.farmacias.peruanas.cajeroexpress.model.*

@Database(
    entities = [Carrito::class, Orden::class, Categoria::class, Productos::class,
        Usuario::class],
    version =2,
    exportSchema = false
)
abstract class CajeroDB : RoomDatabase() {

    abstract fun carritoDao(): CarritoDao

    abstract fun ordenDao(): OrdenDao

    abstract fun categoriasDao(): CategoriasDao

    abstract fun productosDao(): ProductosDao

    abstract fun usuarioDao(): UsuarioDao

    companion object {

        @Volatile
        private var INSTANCE: CajeroDB? = null

        fun getInstance(context: Context): CajeroDB {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, CajeroDB::class.java, "CajeroDB")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

}