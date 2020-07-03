package pe.farmacias.peruanas.cajeroexpress.principal.inicio

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.Status
import pe.farmacias.peruanas.cajeroexpress.databinding.FragmentInicioBinding
import pe.farmacias.peruanas.cajeroexpress.detalles.DetallesActivity
import pe.farmacias.peruanas.cajeroexpress.di.base.injectViewModel
import pe.farmacias.peruanas.cajeroexpress.model.Categoria
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.model.CategoriasUi
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.service.CategoriasResponse
import pe.farmacias.peruanas.cajeroexpress.principal.inicio.adapter.CategoriaAdapter
import pe.farmacias.peruanas.cajeroexpress.principal.inicio.adapter.TopProductosAdapter
import pe.farmacias.peruanas.cajeroexpress.principal.inicio.model.TopProducto
import pe.farmacias.peruanas.cajeroexpress.principal.inicio.service.InicioResponse
import pe.farmacias.peruanas.cajeroexpress.prodcat.ProdCatActivity
import timber.log.Timber
import javax.inject.Inject

class InicioFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: InicioViewModel
    lateinit var binding: FragmentInicioBinding
    lateinit var categoriaAdapter: CategoriaAdapter
    lateinit var productoAdapter: TopProductosAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInicioBinding.inflate(inflater, container, false)
        val view = binding.getRoot()
        viewModel = injectViewModel(viewModelFactory)
        initViewModel()
        initAdapterProducto()
        initAdapterCategoria()
        return view
    }

    private fun initViewModel() {
        viewModel.obtenerListaTopProductos.observe(viewLifecycleOwner, Observer { result ->
            obtenerListaTopProductos(result)
        })
        viewModel.obtenerListaCategorias.observe(viewLifecycleOwner, Observer { result ->
            obtenerListaCategorias(result)
        })
    }

    private fun obtenerListaCategorias(result: Resource<List<CategoriasUi>>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    categoriaAdapter.actualizarLista(it)
                }!!
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("ERROR CONEXIÓN")
        }
    }



    private fun obtenerListaTopProductos(result: Resource<List<TopProducto>>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    productoAdapter.actualizarLista(result.data)
                }!!
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("ERROR CONEXIÓN")
        }
    }

    private fun initAdapterCategoria() {
        binding.recicladorCategorias.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoriaAdapter = CategoriaAdapter {
            initStartActivityProdCat(it)
        }
        binding.recicladorCategorias.adapter = categoriaAdapter
        binding.recicladorPopulares.setHasFixedSize(true)
    }

    private fun initAdapterProducto() {
        binding.recicladorPopulares.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        productoAdapter = TopProductosAdapter {
            initDetallesActivity(it)
        }
        binding.recicladorPopulares.adapter = productoAdapter
        binding.recicladorPopulares.setHasFixedSize(true)
        Timber.d("init Adapter")
    }

    private fun initDetallesActivity(it: TopProducto) {
        val intent = Intent(requireContext(), DetallesActivity::class.java)
        intent.putExtra("id", it.id)
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

    private fun initStartActivityProdCat(it: CategoriasUi) {
        Timber.d("initStartActivityProdCat : %s ", it.categoriaId)
        val intent = Intent(requireContext(), ProdCatActivity::class.java)
        intent.putExtra("categoriaId", it.categoriaId)
        intent.putExtra("name", it.categoriaNombre)
        intent.putExtra("items", it.items)
        startActivity(intent)
    }

}