package pe.farmacias.peruanas.cajeroexpress.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.farmacias.peruanas.cajeroexpress.model.Productos

@Dao
interface ProductosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarProductos(productos: List<Productos>)

    @Query("SELECT * FROM productos LIMIT 15 ")
    fun obtenerListaProductos(): LiveData<List<Productos>>

    @Query("SELECT * FROM productos limit :limite OFFSET :page")
    fun obtenerListaProductos(limite: Int, page: Int): LiveData<List<Productos>>

    @Query("SELECT * FROM productos where nombre like '%'||:textBuscar|| '%'")
    fun obtenerListaProductoBuscar(textBuscar: String): LiveData<List<Productos>>

    @Query("SELECT * FROM productos where nombre like '%'||:textBuscar|| '%' LIMIT 5 ")
    fun obtenerListaProductoBuscarSimilar(textBuscar: String): LiveData<List<Productos>>

    @Query("SELECT * FROM productos where categoriaId = :categoria ")
    fun obtenerListaProductosCatPrimera(categoria: String): LiveData<List<Productos>>

    @Query("SELECT * FROM productos where productoId = :codigoProducto ")
    fun obtenerProducto(codigoProducto: String): LiveData<List<Productos>>


}