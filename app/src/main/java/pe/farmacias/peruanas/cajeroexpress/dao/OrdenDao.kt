package pe.farmacias.peruanas.cajeroexpress.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.farmacias.peruanas.cajeroexpress.model.Orden

@Dao
interface OrdenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun guardarOrden(orden: Orden)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun guardarOrdenList(orden: List<Orden>)

    @Query("SELECT * from orden GROUP BY referenciaId order by idOrden desc ")
    fun obtenerListaOrden(): LiveData<List<Orden>>

    @Query("SELECT productos.nombre AS nombreProducto,productos.descCorta AS descripcionCortaProducto,productos.precio AS precioProducto,productos.imageM AS productosImagen FROM orden INNER JOIN productos ON orden.productoId = productos.productoId WHERE orden.referenciaId = :codigoReferencia")
    fun obtenerListaOrdenDetalles(codigoReferencia: String): List<CarritoResp.ObtenerOrdenDetalle>

    @Query("SELECT count(*) FROM orden GROUP BY referenciaId")
    fun obtenerConteoOrden(): LiveData<String>

    @Query("update orden set estado =:estado where referenciaId =:codigoReferenciaId ")
    fun actualizarOrden(codigoReferenciaId: String, estado: String): Int
}