package pe.farmacias.peruanas.cajeroexpress.detalles

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.toolbar_product_details.view.*
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.Status
import pe.farmacias.peruanas.cajeroexpress.databinding.ActivityDetallesBinding
import pe.farmacias.peruanas.cajeroexpress.detalles.adapter.DetallesAdater
import pe.farmacias.peruanas.cajeroexpress.detalles.adapter.SimilaresAdapter
import pe.farmacias.peruanas.cajeroexpress.detalles.service.DetallesResponse
import pe.farmacias.peruanas.cajeroexpress.detalles.ui.DetallesUi
import pe.farmacias.peruanas.cajeroexpress.detalles.ui.SimilaresUi
import pe.farmacias.peruanas.cajeroexpress.di.base.injectViewModel
import timber.log.Timber
import javax.inject.Inject

class DetallesActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: ActivityDetallesBinding
    lateinit var adapterDetalles: DetallesAdater
    lateinit var adapterSimilares: SimilaresAdapter
    lateinit var viewModel: DetallesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detalles)
        viewModel = injectViewModel(viewModelFactory)
        val nombre = intent?.getStringExtra("nombre")
        viewModel.initBundle(intent)
        viewModel.initNombreProducto(nombre)
        initView()
        initAdapter()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.mostrarDatainicial.observe(this, Observer { result ->
            mostrarVistaInicial(result)
        })
        viewModel.listaImagenes.observe(this, Observer { result ->
            adapterDetalles.setMostrarLista(result)
        })
        viewModel.obtenerListaSimilar.observe(this, Observer { result ->
            //initObtenerListaSimilar(result)
            initObtenerListaSimilarUi(result)
        })
        viewModel.mostrarTotalCantidad.observe(this, Observer { result ->
            binding.textViewConteo.text = result
        })
        viewModel.agregarCarrito.observe(this, Observer { result ->
            initAgregarCarritoProducto(result)
        })
    }

    private fun initObtenerListaSimilarUi(result: Resource<List<SimilaresUi>>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    adapterSimilares.actualizarLista(result.data)
                }!!
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("CONEXIÓN")
        }
    }

    private fun initAgregarCarritoProducto(result: Resource<String>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    Timber.d("SUCCESS")
                    mostrarMensaje(result.data)
                }!!
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("CONEXIÓN")
        }
    }

    private fun initObtenerListaSimilar(result: Resource<DetallesResponse>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    adapterSimilares.actualizarLista(result.data.listaProductoSimilar)
                }!!
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("CONEXIÓN")
        }
    }


    private fun mostrarVistaInicial(result: DetallesUi?) {
        binding.textViewPrecio.text = "S/." + result?.precio
        binding.textViewPrecioTachado.text = result?.preciotachado
        binding.textViewNombre.text = result?.nombre
        binding.textViewDescripcion.text = result?.descCorta
        binding.textViewPrecioTachado.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun mostrarMensaje(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
    }

    private fun initView() {

        binding.imgRestar.setOnClickListener {
            val cantidadProducto = binding.textViewConteo.text
            viewModel.restar(cantidadProducto.toString())
        }
        binding.imgSumar.setOnClickListener {
            val cantidadProducto = binding.textViewConteo.text
            viewModel.sumar(cantidadProducto.toString())
        }
        binding.include.back_pressed.setOnClickListener {
            finish()
        }
        binding.btnAgregarCarrito.setOnClickListener {
            val cantidadProducto = binding.textViewConteo.text
            viewModel.agregarCarrito(cantidadProducto.toString())
        }
    }

    private fun initAdapter() {
        adapterDetalles = DetallesAdater()
        binding.sliderView.setSliderAdapter(adapterDetalles)
        binding.sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION)
        binding.sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH)
        binding.sliderView.setIndicatorSelectedColor(Color.WHITE)
        binding.sliderView.setIndicatorUnselectedColor(Color.GRAY)
        binding.sliderView.startAutoCycle();
        binding.sliderView.setOnIndicatorClickListener { position ->
            binding.sliderView.currentPagePosition = position
        }
        adapterSimilares = SimilaresAdapter()
        binding.recicladorSimilares.layoutManager = GridLayoutManager(this, 2)
        binding.recicladorSimilares.setItemAnimator(DefaultItemAnimator())
        binding.recicladorSimilares.adapter = adapterSimilares
        binding.recicladorSimilares.setHasFixedSize(true)
    }


}