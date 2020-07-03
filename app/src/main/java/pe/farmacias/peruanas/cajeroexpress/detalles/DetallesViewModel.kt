package pe.farmacias.peruanas.cajeroexpress.detalles

import android.content.Intent
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.detalles.ui.DetallesUi
import pe.farmacias.peruanas.cajeroexpress.detalles.ui.SimilaresUi
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi
import timber.log.Timber
import javax.inject.Inject

class DetallesViewModel @Inject constructor(
    private val repository: DetallesRepository
) :
    ViewModel() {

    val mostrarDatainicial = MutableLiveData<DetallesUi>()
    val listaImagenes = MutableLiveData<List<DetallesUi>>()
    val mostrarTotalCantidad = MutableLiveData<String>()
    private val cantidadProductos = MutableLiveData<String>()
    val nombreProducto = MutableLiveData<String>()
    private lateinit var detallesUi: DetallesUi


    fun initBundle(intent: Intent?) {

        val id = intent?.getStringExtra("id")
        val productoId = intent?.getStringExtra("productoId")
        val nombre = intent?.getStringExtra("nombre")
        val descLarga = intent?.getStringExtra("descLarga")
        val descCorta = intent?.getStringExtra("descCorta")
        val precio = intent?.getStringExtra("precio")
        val preciotachado = intent?.getStringExtra("preciotachado")
        val porcentaje = intent?.getStringExtra("porcentaje")
        val imageL = intent?.getStringExtra("imageL")
        val imageM = intent?.getStringExtra("imageM")
        val imageX = intent?.getStringExtra("imageX")
        Timber.d("productoId: %s ", productoId)
        Timber.d("nombre: %s ", nombre)
        Timber.d("descLarga: %s ", descLarga)
        Timber.d("descCorta: %s ", descCorta)
        Timber.d("precio: %s ", precio)
        Timber.d("preciotachado: %s ", preciotachado)
        Timber.d("porcentaje: %s ", porcentaje)
        Timber.d("imageL: %s ", imageL)
        Timber.d("imageM: %s ", imageM)
        Timber.d("imageX: %s ", imageX)



        detallesUi = DetallesUi(
            id,
            productoId,
            nombre,
            descLarga,
            descCorta,
            precio,
            preciotachado,
            porcentaje,
            imageL
        )
        mostrarDatainicial.postValue(
            detallesUi
        )
        initListaImagenes(
            id,
            productoId,
            nombre,
            descLarga,
            descCorta,
            precio,
            preciotachado,
            porcentaje,
            imageL,
            imageM,
            imageX
        )
    }

    private fun initListaImagenes(
        id: String?,
        productoId: String?,
        nombre: String?,
        descLarga: String?,
        descCorta: String?,
        precio: String?,
        preciotachado: String?,
        porcentaje: String?,
        imageL: String?,
        imageM: String?,
        imageX: String?
    ) {
        val listaDetalles: MutableList<DetallesUi> = ArrayList()
        listaDetalles.add(
            DetallesUi(
                id.toString(),
                productoId.toString(),
                nombre.toString(),
                descLarga.toString(),
                descCorta.toString(),
                precio.toString(),
                preciotachado.toString(),
                porcentaje.toString(),
                imageL.toString()
            )
        )
        listaDetalles.add(
            DetallesUi(
                id,
                productoId,
                nombre,
                descLarga,
                descCorta,
                precio,
                preciotachado,
                porcentaje,
                imageM!!
            )
        )
        listaDetalles.add(
            DetallesUi(
                id,
                productoId,
                nombre,
                descLarga,
                descCorta,
                precio,
                preciotachado,
                porcentaje,
                imageX!!
            )
        )
        listaImagenes.postValue(listaDetalles)
    }

    fun restar(cantidadProducto: String) {
        if (cantidadProducto.equals("1")) {
            return
        }
        val totalCantidad = cantidadProducto.toInt().minus(1)
        mostrarTotalCantidad.postValue(totalCantidad.toString())
    }

    fun sumar(cantidadProducto: String) {
        val cantidadEditar: Int? = cantidadProducto.toInt()
        val totalCantidad = cantidadEditar?.plus(1)
        mostrarTotalCantidad.postValue(totalCantidad.toString())
    }

    fun agregarCarrito(cantidadProducto: String) {
        cantidadProductos.value = cantidadProducto
    }

    fun initNombreProducto(nombre: String?) {
        Timber.d("initNombreProducto : %s ", nombre)
        nombreProducto.value = nombre
    }

    var agregarCarrito = cantidadProductos.switchMap { cantidadProductos ->
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            emit(repository.agregarCarrito(cantidadProductos, detallesUi))
        }
    }

    val obtenerListaSimilar: LiveData<Resource<List<SimilaresUi>>> =
        Transformations.switchMap(nombreProducto, ::resultadoLista)

    private fun resultadoLista(textoBuscar: String) =
        repository.obtenerListaProductosSimilar(textoBuscar)


    // private fun resultadoLista(textoBuscar: String) = repository.obtenerListaProductos(textoBuscar)
    /* var obtenerListaSimilar = liveData(Dispatchers.IO) {
         emit(Resource.loading(null))
         emit(repository.obtenerListaProductosSimilar())
     }*/
}