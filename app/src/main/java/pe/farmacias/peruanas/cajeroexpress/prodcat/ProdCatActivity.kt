package pe.farmacias.peruanas.cajeroexpress.prodcat

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.toolbar_prod_cat.view.*
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.Status
import pe.farmacias.peruanas.cajeroexpress.base.loadMore.OnLoadMoreListener
import pe.farmacias.peruanas.cajeroexpress.base.loadMore.RecyclerViewLoadMoreScroll
import pe.farmacias.peruanas.cajeroexpress.databinding.ActivityProdCatBinding
import pe.farmacias.peruanas.cajeroexpress.detalles.DetallesActivity
import pe.farmacias.peruanas.cajeroexpress.di.base.injectViewModel
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi
import pe.farmacias.peruanas.cajeroexpress.prodcat.adapter.ProdCatAdapter
import pe.farmacias.peruanas.cajeroexpress.prodcat.service.ProdCatResponse
import pe.farmacias.peruanas.cajeroexpress.util.Constantes
import timber.log.Timber
import javax.inject.Inject


class ProdCatActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: ProdCatViewModel
    lateinit var binding: ActivityProdCatBinding
    lateinit var prodCatAdapter: ProdCatAdapter
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prod_cat)
        viewModel = injectViewModel(viewModelFactory)
        id = intent.getStringExtra("categoriaId")
        Timber.d("categoriaId : %s ", id)
        val nombreCategoria = intent?.getStringExtra("name")
        binding.include.textViewTitulo.text = nombreCategoria
        binding.include.back_pressed.setOnClickListener {
            finish()
        }
        initViewModel()
        initAdapter()
        initMostrarListaProductos()

        initBuscar()
    }

    private fun initMostrarListaProductos() {
        viewModel.obteniendoProductosUi(id)
    }


    private fun initBuscar() {

        binding.textViewBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                Timber.d("beforeTextChanged")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    initMostrarListaProductos()
                    return
                }
                prodCatAdapter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {
                Timber.d("afterTextChanged")
            }
        })
    }

    private fun initAdapter() {
        prodCatAdapter = ProdCatAdapter({
            Timber.d("guardarCarrito")
            initGuardarProductoCarrito(it)
        }, {
            initStartActivityDetalles(it)
        })
        binding.reciclador.adapter = prodCatAdapter
        setLayoutManager()
    }

    private fun initStartActivityDetalles(it: ProductosUi) {
        val intent = Intent(this, DetallesActivity::class.java)
        intent.putExtra("id", it.productoId)
        intent.putExtra("productoId", it.productoId)
        intent.putExtra("nombre", it.nombre)
        intent.putExtra("descCorta", it.descCorta)
        intent.putExtra("descLarga", it.descLarga)
        intent.putExtra("precio", it.precio)
        intent.putExtra("preciotachado", it.preciotachado)
        intent.putExtra("porcentaje", it.porcentaje)
        intent.putExtra("imageL", it.imageL)
        intent.putExtra("imageM", it.imageM)
        intent.putExtra("imageX", it.imageX)
        startActivity(intent)
    }

    private fun initGuardarProductoCarrito(it: ProductosUi) {
        viewModel.obteniendoProductosUiObject(it)
    }

    private fun setLayoutManager() {
        mLayoutManager = GridLayoutManager(this, 2)
        binding.reciclador.layoutManager = mLayoutManager
        binding.reciclador.setHasFixedSize(true)
        binding.reciclador.adapter = prodCatAdapter
        (mLayoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (prodCatAdapter.getItemViewType(position)) {
                        Constantes.VIEW_TYPE_ITEM -> 1
                        Constantes.VIEW_TYPE_LOADING -> 2 //number of columns of the grid
                        else -> -1
                    }
                }
            }
        setScroollListener()
    }

    private fun setScroollListener() {
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as GridLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                Timber.d("Cargar más Data - ProdCat ")
            }
        })

        binding.reciclador.addOnScrollListener(scrollListener)
    }

    private fun initViewModel() {
        viewModel.obtenerListaCatProd.observe(this, Observer { result ->
            //initObtenerListaCatProd(result)
            initObtenerListaCatProdCategoria(result)
        })

        viewModel.clickAgregarCarrito.observe(this, Observer { result ->
            initResultadoGuardarProducto(result)
        })
    }

    private fun initObtenerListaCatProdCategoria(result: Resource<List<ProductosUi>>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    prodCatAdapter.actualizarLista(result.data)
                }!!
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("ERROR CONEXIÓN")
        }
    }

    private fun initResultadoGuardarProducto(result: Resource<String>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    Timber.d("SUCCESS : %s ", it)
                    mostrarMensaje(result.data)
                }!!
            }
            Status.LOADING -> Timber.d("LOADING")
            Status.ERROR -> Timber.d("ERROR: %s ", result.message)
        }
    }

    private fun mostrarMensaje(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
    }

    private fun initObtenerListaCatProd(result: Resource<ProdCatResponse>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    prodCatAdapter.actualizarLista(result.data.productoLista)
                }!!
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("ERROR CONEXIÓN")
        }
    }

}