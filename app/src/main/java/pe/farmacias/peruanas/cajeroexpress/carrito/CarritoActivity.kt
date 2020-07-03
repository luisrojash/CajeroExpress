package pe.farmacias.peruanas.cajeroexpress.carrito

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.toolbar_carrito.view.*
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.Status
import pe.farmacias.peruanas.cajeroexpress.carrito.adapter.CarritoAdapter
import pe.farmacias.peruanas.cajeroexpress.databinding.ActivityCarritoBinding
import pe.farmacias.peruanas.cajeroexpress.di.base.injectViewModel
import pe.farmacias.peruanas.cajeroexpress.model.Carrito
import pe.farmacias.peruanas.cajeroexpress.util.Constantes
import pe.izipay.izipaysdk.entidades.OperacionResult
import pe.izipay.izipaysdk.entidades.OperationInfo
import pe.izipay.izipaysdk.views.ui.IzipaySDK
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject


class CarritoActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: ActivityCarritoBinding
    lateinit var viewModel: CarritoViewModel
    lateinit var carritoAdapter: CarritoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_carrito)
        viewModel = injectViewModel(viewModelFactory)
        initView()
        initAdapter()
        initViewModel()
    }

    private fun initView() {
        binding.includeProductos.back_pressed.setOnClickListener {
            finish()
        }
        binding.buttonOrdenar.setOnClickListener {
            viewModel.onClickOrdenar()
        }
    }

    private fun initViewModel() {

        viewModel.carritoLista.observe(this, Observer { result ->
            obtenerListaCarrito(result)
        })
        viewModel.eliminarCarrito.observe(this, Observer { result ->
            initEliminarCarrito(result)
        })
        viewModel.guardarOrdenListCarrito.observe(this, Observer { result ->
            initGuardarOrdenListCarrito(result)
        })


        viewModel.mostrarMensaje.observe(this, Observer { resultado ->
            initMostrarMensaje(resultado)
        })
        viewModel.starActivity.observe(this, Observer { resultado ->
            initStartActivity(resultado)
        })

    }

    private fun initGuardarOrdenListCarrito(result: Resource<String>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                initMostrarMensaje(result.data)
                finish()
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> initMostrarMensaje(result.data)
        }
    }

    private fun initStartActivity(resultado: HashMap<String, String>?) {
        if (resultado?.get("totalCantidad") != null) {
            val totalSoles = resultado.get("totalSoles")
            Timber.d("IniciarDialog : %s ", totalSoles)
            enviarDataIzyPay(Constantes.IZI_CONSUMO, totalSoles, Constantes.IZI_SOLES)
        }
    }


    private fun enviarDataIzyPay(iziConsumo: String, totalSoles: String?, iziSoles: String) {
        Timber.d("totalSoles : %s ", totalSoles)
        val intent = Intent(this, IzipaySDK::class.java)
        val operation = OperationInfo()
        operation.tipoOperacion = iziConsumo
        operation.importe = 1.00
        operation.tipoMoneda = iziSoles
        operation.isPopUp = true
        val bundle = Bundle()
        bundle.putSerializable(OperationInfo.OPERATION_INFO, operation)
        bundle.putString("entorno", Constantes.IZI_ENTORNO)
        intent.putExtras(bundle)
        startActivityForResult(intent, Constantes.IZI_REQUEST_CODE)
    }

    private fun initMostrarMensaje(resultado: String?) {
        Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show()
    }

    private fun initEliminarCarrito(result: Resource<String>?) {
        when (result?.status) {
            Status.SUCCESS -> Timber.d("ELIMINO CORRECTAMENTE")
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("CONEXIÓN")
        }
    }


    private fun obtenerListaCarrito(result: Resource<List<Carrito>>) {
        when (result.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    actualizarTotalProductos(it)
                    viewModel.setMostrarLista(it as MutableList<Carrito>)
                    carritoAdapter.actualizarLista(it)
                }
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("ERROR TRAER DB")

        }
    }

    private fun actualizarTotalProductos(it: List<Carrito>) {
        var totalProductoLista = 0.0
        for (lista in it) {
            Timber.d("precioTotalProducto : %s ", lista.precioTotalProducto)
            totalProductoLista += lista.precioTotalProducto.toDouble()
        }
        val decimal = BigDecimal(totalProductoLista).setScale(2, RoundingMode.HALF_EVEN)
        viewModel.obtenerTotalPrecio(decimal)
        binding.includeProductos.textViewNumero.text = it.size.toString()
        binding.textViewResultadoTotal.setText("S./" + decimal)
    }

    private fun initAdapter() {
        carritoAdapter = CarritoAdapter(
            {
                /*OnClick Button Sumar Carrito*/
                viewModel.sumarCantidadProducto(it)
            },
            {
                /*OnClick Button Restar Carrito*/
                viewModel.restarCantidadProducto(it)
            },
            {
                /*OnClick Button Eliminar Carrito*/
                viewModel.setCarrito(it)
            })
        binding.recicladorCarrito.layoutManager = LinearLayoutManager(this)
        binding.recicladorCarrito.adapter = carritoAdapter
        binding.recicladorCarrito.setHasFixedSize(true)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constantes.IZI_REQUEST_CODE) {
            if (data != null) {
                val bundle = data.extras
                if (bundle != null) {
                    val o = (bundle.get(OperacionResult.OPERATION_RESULT) as OperacionResult?)!!
                    if (o.typeResult == OperacionResult.TypeResult.ERROR) {
                        initMostrarMensaje(o.responseMessage)
                    } else {
                        if (o.typeResult == OperacionResult.TypeResult.CONSUMO) {

                            Timber.d("o.typeResult : %S ", o.typeResult)
                            Timber.d("o.responseCode : %S ", o.responseCode)
                            Timber.d("o.responseMessage : %S ", o.responseMessage)
                            when (o.responseCode) {
                                "51" -> initMostrarMensaje("Saldo Insuficiente")
                                "10" -> initMostrarMensaje("Operación Cancelada")
                                "00" -> viewModel.guardarCarritoOrden(
                                    o.buyerName,
                                    o.transactionReferenceNumber
                                )
                                else -> {
                                    initMostrarMensaje("Operación Cancelada")
                                }
                            }
                        }
                    }
                }
            } else {
                initMostrarMensaje("Bundle Nulo")
            }
        }
    }
}

