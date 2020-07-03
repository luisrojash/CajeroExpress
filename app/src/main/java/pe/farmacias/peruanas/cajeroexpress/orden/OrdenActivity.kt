package pe.farmacias.peruanas.cajeroexpress.orden

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.toolbar_orden.view.*
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.Status
import pe.farmacias.peruanas.cajeroexpress.databinding.ActivityOrdenBinding
import pe.farmacias.peruanas.cajeroexpress.di.base.injectViewModel
import pe.farmacias.peruanas.cajeroexpress.model.Orden
import pe.farmacias.peruanas.cajeroexpress.orden.adapter.OrdenAdapter
import pe.farmacias.peruanas.cajeroexpress.orden.model.OrdenUi
import pe.farmacias.peruanas.cajeroexpress.util.Constantes
import pe.izipay.izipaysdk.entidades.OperacionResult
import pe.izipay.izipaysdk.entidades.OperationInfo
import pe.izipay.izipaysdk.views.ui.IzipaySDK
import timber.log.Timber
import javax.inject.Inject

class OrdenActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: ActivityOrdenBinding
    lateinit var viewModel: OrdenViewModel
    lateinit var ordenAdapter: OrdenAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_orden)
        viewModel = injectViewModel(viewModelFactory)
        initView()
        initAdapter()
        initViewModel()
    }

    private fun initView() {
        binding.include.back_pressed.setOnClickListener {
            finish()
        }
    }

    private fun initViewModel() {
        viewModel.obtenerListaOrden.observe(this, Observer { result ->
            initObtenerLista(result)
        })
        viewModel.actualizarOrden.observe(this, Observer { result ->
            when (result?.status) {
                Status.SUCCESS -> initMostrarMensaje("Anulación Exitosa")
                Status.LOADING -> Timber.d("CARGANDO")
                Status.ERROR -> Timber.d("ERROR TRAER DB")
            }
        })
    }

    private fun initObtenerLista(result: Resource<List<OrdenUi>>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    ordenAdapter.actualizarLista(it)
                }
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("ERROR TRAER DB")
        }
    }

    private fun initAdapter() {
        ordenAdapter = OrdenAdapter {
            anularDataIzyPay(it)
        }
        binding.recicladorOrden.layoutManager = LinearLayoutManager(this)
        binding.recicladorOrden.adapter = ordenAdapter
        binding.recicladorOrden.setHasFixedSize(true)
    }

    private fun anularDataIzyPay(ordenUi: OrdenUi) {
        viewModel.onLongClickAnularOrden(ordenUi)
        val operation = OperationInfo()
        operation.tipoOperacion = Constantes.IZI_ANULACION
        operation.importe = 1.00
        operation.tipoMoneda = Constantes.IZI_SOLES
        operation.codReferencia = ordenUi.referenciaId
        operation.isPopUp = false
        val intent = Intent(this, IzipaySDK::class.java)
        val bundle = Bundle()
        bundle.putSerializable(OperationInfo.OPERATION_INFO, operation)
        bundle.putString("entorno", Constantes.IZI_ENTORNO)
        intent.putExtras(bundle)
        startActivityForResult(intent, Constantes.IZI_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constantes.IZI_REQUEST_CODE) {
            if (data != null) {
                val bundle = data.extras
                if (bundle != null) {
                    val o = (bundle.get(OperacionResult.OPERATION_RESULT) as OperacionResult?)!!
                    when (o.responseCode) {
                        "00" -> viewModel.setProductosUi()
                        else -> {
                            initMostrarMensaje(o.responseMessage)
                        }
                    }
                }
            }
        }
    }

    private fun initMostrarMensaje(mensaje: String) {
        Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
    }

}