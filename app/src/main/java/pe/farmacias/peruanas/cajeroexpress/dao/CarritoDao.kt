package pe.farmacias.peruanas.cajeroexpress.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.farmacias.peruanas.cajeroexpress.model.Carrito

@Dao
interface CarritoDao {

    @Query("SELECT count(*) FROM carrito")
    fun obtenerConteoCarrito(): LiveData<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun guardarCarrito(matches: Carrito)

    @Query("SELECT * FROM carrito where codigoProducto=:codigoProducto")
    fun validarExisteProducto(codigoProducto: String): Int

    @Query("SELECT * FROM carrito where codigoProducto=:codigoProducto")
    fun obtenerdatosCarrito(codigoProducto: String): Carrito

    @Query("UPDATE carrito set cantidadProducto=:cantidadProducto where codigoProducto=:codigoProducto")
    fun actualizarCantidadProducto(codigoProducto: String?, cantidadProducto: Int): Int

    @Query("UPDATE carrito set precioTotalProducto=:precioTotal where codigoProducto=:codigoProducto")
    fun actualizarPrecioTotal(codigoProducto: String?, precioTotal: String?): Int

    @Query("Select count(codigoProducto) as cantidadTotalLista from carrito")
    fun sumaTotalProductosCarrito(): CarritoResp.ObtenerCantidadTotal

    @Query("SELECT * from carrito")
    fun obtenerListaCarrito(): LiveData<List<Carrito>>

    @Query("SELECT cantidadProducto,precioTotalProducto,codigoProducto,idCarrito,precioProducto FROM carrito where codigoProducto=:codigoProducto")
    fun obtenerDatosProductoSqlite(codigoProducto: String): CarritoResp.ObtenerCantidadYPrecioTotal

    @Query("DELETE FROM carrito WHERE idCarrito = :idCarrito")
    fun eliminarCarrito(idCarrito: String): Int

    @Query("DELETE FROM carrito")
    fun limpiarCarrito(): Int


}