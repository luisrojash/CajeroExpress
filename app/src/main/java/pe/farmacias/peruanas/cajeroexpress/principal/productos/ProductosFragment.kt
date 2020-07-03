package pe.farmacias.peruanas.cajeroexpress.principal.productos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerFragment
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.Status
import pe.farmacias.peruanas.cajeroexpress.base.loadMore.OnLoadMoreListener
import pe.farmacias.peruanas.cajeroexpress.base.loadMore.RecyclerViewLoadMoreScroll
import pe.farmacias.peruanas.cajeroexpress.databinding.FragmentProductosBinding
import pe.farmacias.peruanas.cajeroexpress.detalles.DetallesActivity
import pe.farmacias.peruanas.cajeroexpress.di.base.injectViewModel
import pe.farmacias.peruanas.cajeroexpress.model.Productos
import pe.farmacias.peruanas.cajeroexpress.principal.productos.adapter.ProductosAdapter
import pe.farmacias.peruanas.cajeroexpress.principal.productos.listener.ProductosListener
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi
import pe.farmacias.peruanas.cajeroexpress.principal.productos.service.ProductosResponse
import pe.farmacias.peruanas.cajeroexpress.util.Constantes.VIEW_TYPE_ITEM
import pe.farmacias.peruanas.cajeroexpress.util.Constantes.VIEW_TYPE_LOADING
import timber.log.Timber
import javax.inject.Inject

class ProductosFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ProductosViewModel
    private var listener: ProductosListener? = null
    lateinit var binding: FragmentProductosBinding
    lateinit var productosAdapter: ProductosAdapter
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProductosListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        /* binding = DataBindingUtil.inflate(
             inflater, R.layout.fragment_productos, container, false
         */
        binding = FragmentProductosBinding.inflate(inflater, container, false)
        val view = binding.getRoot()
        viewModel = injectViewModel(viewModelFactory)
        initViewModel()
        initAdapterProductos()
        return view
    }

    private fun initViewModel() {
        viewModel.obtenerListaProductos.observe(viewLifecycleOwner, Observer { result ->
            obtenerListaProductos(result)
        })
        viewModel.clickAgregarCarrito.observe(viewLifecycleOwner, Observer { result ->
            initResultadoGuardarProducto(result)
        })

    }

    private fun obtenerListaProductos(result: Resource<List<ProductosUi>>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    productosAdapter.actualizarLista(result.data)
                    Timber.d("result.data.size : %s ", result.data.size)
                }!!
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("CONEXIÓN")
        }

    }


    /* private fun obtenerListaProductos(result: Resource<ProductosResponse>?) {
         when (result?.status) {
             Status.SUCCESS -> {
                 result.data?.let {
                     productosAdapter.actualizarLista(result.data.productoLista)
                 }!!
             }
             Status.LOADING -> Timber.d("CARGANDO")
             Status.ERROR -> Timber.d("CONEXIÓN")
         }

     }*/


    private fun initAdapterProductos() {
        productosAdapter = ProductosAdapter({
            Timber.d("guardarCarrito")
            initGuardarProductoCarrito(it)
        }, {
            initStartActivityDetalles(it)
        })
        binding.recicladorProductos.adapter = productosAdapter
        setLayoutManager()
    }

    private fun initGuardarProductoCarrito(it: ProductosUi) {
        viewModel.obteniendoProductosUi(it)
    }

    private fun initResultadoGuardarProducto(result: Resource<String>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    Timber.d("SUCCESS : %s ", it)
                    mostrarMensaje(result.data)
                    if (listener != null) listener!!.onClickAgregarCarrito()
                }!!
            }
            Status.LOADING -> Timber.d("LOADING")
            Status.ERROR -> Timber.d("ERROR: %s ", result.message)
        }
    }

    private fun mostrarMensaje(data: String) {
        Toast.makeText(activity, data, Toast.LENGTH_SHORT).show()
    }

    private fun initStartActivityDetalles(it: ProductosUi) {
        val intent = Intent(requireContext(), DetallesActivity::class.java)
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

    private fun setLayoutManager() {
        mLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.recicladorProductos.layoutManager = mLayoutManager
        binding.recicladorProductos.setHasFixedSize(true)
        binding.recicladorProductos.adapter = productosAdapter
        (mLayoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (productosAdapter.getItemViewType(position)) {
                        VIEW_TYPE_ITEM -> 1
                        VIEW_TYPE_LOADING -> 2 //number of columns of the grid
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
                cargarMasData()
            }
        })

        binding.recicladorProductos.addOnScrollListener(scrollListener)
    }

    private fun cargarMasData() {
        Timber.d("cargarMasData")
    }
}