package pe.farmacias.peruanas.cajeroexpress.principal.foto

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.Result
import dagger.android.support.DaggerDialogFragment
import me.dm7.barcodescanner.zxing.ZXingScannerView
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.Status
import pe.farmacias.peruanas.cajeroexpress.databinding.FotoDialogFragmentBinding
import pe.farmacias.peruanas.cajeroexpress.detalles.DetallesActivity
import pe.farmacias.peruanas.cajeroexpress.di.base.injectViewModel
import pe.farmacias.peruanas.cajeroexpress.principal.foto.service.FotoResponse
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject


class FotoFragment : DaggerDialogFragment(), ZXingScannerView.ResultHandler {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: FotoViewModel
    lateinit var binding: FotoDialogFragmentBinding
    private var mScannerView: ZXingScannerView? = null
    private var mFlash: Boolean = false
    private var tipoEstado: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.foto_dialog_fragment,
            container,
            false
        )
        val view = binding.getRoot()
        viewModel = injectViewModel(viewModelFactory)
        val bundle = arguments
        tipoEstado = bundle?.getString("tipoEstado").toString()
        Timber.d("tipoEstado : %s ", tipoEstado)
        viewModel.bundleTipoEstado(tipoEstado)
        initView()
        initViewModel()
        return view
    }


    private fun exeptionBuscar(result: Resource<List<ProductosUi>>) {
        try {
            initStartActivityDetalles(result?.data!!.get(0))
        } catch (ex: Exception) {
            validarExeption()
        }
    }

    private fun exeptionAgregar(result: Resource<List<ProductosUi>>) {
        try {
            result.data?.let {
                guardarCarrito(result.data.get(0))
            }!!
        } catch (ex: Exception) {
            validarExeption()
        }
    }

    private fun validarExeption() {
        onResume()
        mostrarMensaje("Producto no se encontro")
    }

    private fun initStartActivityDetalles(productosUi: ProductosUi) {
        val intent = Intent(requireContext(), DetallesActivity::class.java)
        intent.putExtra("id", "001")
        intent.putExtra("productoId", productosUi.productoId)
        intent.putExtra("nombre", productosUi.nombre)
        intent.putExtra("descCorta", productosUi.descCorta)
        intent.putExtra("descLarga", productosUi.descLarga)
        intent.putExtra("precio", productosUi.precio)
        intent.putExtra("preciotachado", productosUi.preciotachado)
        intent.putExtra("porcentaje", productosUi.porcentaje)
        intent.putExtra("imageL", productosUi.imageL)
        intent.putExtra("imageM", productosUi.imageM)
        intent.putExtra("imageX", productosUi.imageX)
        startActivity(intent)
        dismiss()
    }


    private fun initViewModel() {
        viewModel.handleResult.observe(viewLifecycleOwner, Observer { result ->
            //obtenerProducto(result)
            obtenerProductoBuscar(result)
        })
        viewModel.guardarCarrito.observe(viewLifecycleOwner, Observer { result ->
            resultadoGuardar(result)
        })
    }

    private fun obtenerProductoBuscar(result: Resource<List<ProductosUi>>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                if (result.data == null) return
                //validarTipoEstado(tipoEstado, result)
                Timber.d("obtenerProductoBuscar: %s ", result.data.size)

                validarTipoEstadoUi(tipoEstado, result)
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("CONEXIÓN 1")
        }
    }

    private fun validarTipoEstadoUi(tipoEstado: String, result: Resource<List<ProductosUi>>) {
        when (tipoEstado) {
            "agregarCarrito" -> {
                exeptionAgregar(result)
            }
            "buscar" -> {
                exeptionBuscar(result)
            }
        }
    }


    private fun resultadoGuardar(result: Resource<String>?) {
        when (result?.status) {
            Status.SUCCESS -> mostrarMensaje(result.data!!)
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("ERRORCONEXIÓN")
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }


    private fun obtenerProducto(result: Resource<FotoResponse>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                if (result.data == null) return
                //validarTipoEstado(tipoEstado, result)
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("CONEXIÓN 1")
        }
    }

    private fun guardarCarrito(productosUi: ProductosUi) {
        viewModel.obteniendoProductos(productosUi)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_AppCompat_Light_NoActionBar)
    }

    private fun initView() {
        mScannerView = ZXingScannerView(requireContext())
        binding.contentFrame.addView(mScannerView)
        binding.toggleFlash.setOnClickListener {
            mFlash = !mFlash
            mScannerView?.setFlash(mFlash)
        }
        binding.btnCargar.setOnClickListener {
            mScannerView?.resumeCameraPreview(this)
            onResume()

        }
        binding.close.setOnClickListener {
            dismiss()
        }
        initEditarCantidad(tipoEstado)
    }

    private fun initEditarCantidad(tipoEstado: String) {
        when (tipoEstado) {
            "agregarCarrito" -> {
                mostrarBotones()
                initViewAddCarrito()
            }
            "buscar" -> {
                ocultarBotones()
            }
        }

    }

    private fun initViewAddCarrito() {

        binding.imageViewRestar.setOnClickListener {
            onClickRestar()
        }
        binding.imageViewSum.setOnClickListener {
            onClickSumar()
        }
        viewModel.mostrarTotalCantidad.observe(this, Observer { cantidad ->
            binding.textViewCantidadProductos.setText(cantidad.toString())
        })
    }

    private fun ocultarBotones() {
        binding.imageViewSum.visibility = View.INVISIBLE
        binding.imageViewRestar.visibility = View.INVISIBLE
        binding.textView2.visibility = View.GONE
        binding.textViewCantidadProductos.visibility = View.GONE
    }

    private fun mostrarBotones() {
        binding.imageViewSum.visibility = View.VISIBLE
        binding.imageViewRestar.visibility = View.VISIBLE
    }

    private fun onClickSumar() {
        viewModel.onClickSumar(binding.textViewCantidadProductos.text.toString())
    }

    private fun onClickRestar() {
        viewModel.onClickRestar(binding.textViewCantidadProductos.text.toString())
    }

    override fun onPause() {
        super.onPause()
        mScannerView?.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        getDialog()?.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mScannerView?.setResultHandler(this)
        mScannerView?.setAspectTolerance(0.2f)
        mScannerView?.startCamera()
    }

    override fun handleResult(rawResult: Result?) {
        viewModel.obteniendoCodigo(rawResult?.getText().toString())
    }

}