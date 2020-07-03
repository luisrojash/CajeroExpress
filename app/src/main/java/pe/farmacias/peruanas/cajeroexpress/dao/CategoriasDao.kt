package pe.farmacias.peruanas.cajeroexpress.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.farmacias.peruanas.cajeroexpress.model.Categoria

@Dao
interface CategoriasDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun guardarListaCategorias(categoria: List<Categoria>)

    @Query("SELECT * from categoria limit 1")
    fun obtenerListaCategoria(): LiveData<List<Categoria>>

    @Query("SELECT * from categoria where categoriaPadre =:codigoPadre ")
    fun obtenerListaCategoriaAll(codigoPadre: String): LiveData<List<Categoria>>

}