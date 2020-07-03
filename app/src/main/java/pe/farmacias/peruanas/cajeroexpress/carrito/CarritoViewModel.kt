package pe.farmacias.peruanas.cajeroexpress.carrito

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.model.Carrito
import pe.farmacias.peruanas.cajeroexpress.model.Orden
import pe.farmacias.peruanas.cajeroexpress.util.Constantes
import timber.log.Timber
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CarritoViewModel @Inject constructor(
    private val repository: CarritoRepository
) : ViewModel() {

    private var responseResultadoSumar = MutableLiveData<String>()
    val mostrarMensaje = MutableLiveData<String>()
    val carritoUi = MutableLiveData<Carrito>()
    val ordenList = MutableLiveData<List<Orden>>()

    val starActivity = MutableLiveData<HashMap<String, String>>()

    private var carritoList = mutableListOf<Carrito>()
    //private var precioTotal: Double=0.0
    private lateinit var precioTotal: BigDecimal


    fun setCarrito(input: Carrito) {
        carritoUi.value = input
    }

    val carritoLista: LiveData<Resource<List<Carrito>>> = repository.obtenerListaCarrito()

    fun sumarCantidadProducto(it: Carrito) {

        val cantidadEditar: Int? = it.cantidadProducto
        val totalCantidad = cantidadEditar?.plus(1)
        try {
            repository.agregarSumaProducto(it, totalCantidad!!, responseResultadoSumar)
        } catch (exeption: Exception) {
            mostrarMensaje.postValue(exeption.toString())
        }
    }

    fun restarCantidadProducto(it: Carrito) {
        val cantidadEditar: Int? = it.cantidadProducto
        val totalCantidad = cantidadEditar?.minus(1)
        if (totalCantidad == 0) {
            mostrarMensaje.postValue("No se permite valores nulos")
            return
        }
        try {
            repository.restarProducto(it, totalCantidad!!, responseResultadoSumar)
        } catch (exception: Exception) {
            mostrarMensaje.postValue(exception.toString())
        }
    }

    fun onClickOrdenar() {
        if (carritoList.size == 0) {
            return
        }
        val startActivity = HashMap<String, String>()
        startActivity.put("totalCantidad", carritoList.size.toString())
        startActivity.put("totalSoles", precioTotal.toString())
        starActivity.postValue(startActivity)
        Timber.d("onClickOrdenar: %s ", precioTotal)
    }


    fun setMostrarLista(it: MutableList<Carrito>) {
        this.carritoList = it
    }

    fun obtenerTotalPrecio(decimal: BigDecimal?) {
        if (decimal != null) {
            this.precioTotal = decimal
        }
    }

    fun guardarCarritoOrden(nombreUsuario: String, referenciaId: String) {
        val ordenList: MutableList<Orden> = ArrayList()
        val df = SimpleDateFormat("EEE, d MMM yyyy, HH:mm")
        val fecha = df.format(Calendar.getInstance().time)
        for (carritoUi in carritoList) {
            val orden = Orden(
                fecha,
                nombreUsuario,
                "MFCANADA",
                precioTotal.toString(),
                carritoList.size.toString(),
                carritoUi.codigoProducto,
                referenciaId,
                "Enviado"
            )
            ordenList.add(orden)
        }
        setOrdenCarrito(ordenList)
    }

    private fun setOrdenCarrito(ordenListGuardar: MutableList<Orden>) {
        ordenList.value = ordenListGuardar
    }

    var guardarOrdenListCarrito = ordenList.switchMap { ordenList ->
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            emit(repository.guardarOrdenCarrito(ordenList))
        }
    }


    var eliminarCarrito = carritoUi.switchMap { carritoUi ->
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            emit(repository.eliminarProductoCarrito(carritoUi))
        }
    }


}