package pe.farmacias.peruanas.cajeroexpress.principal.categorias

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import dagger.android.support.DaggerFragment
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.Status
import pe.farmacias.peruanas.cajeroexpress.databinding.FragmentCategoriasBinding
import pe.farmacias.peruanas.cajeroexpress.di.base.injectViewModel
import pe.farmacias.peruanas.cajeroexpress.model.Categoria
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.adapter.CategoriasAdapter
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.model.CategoriasUi
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.service.CategoriasResponse
import pe.farmacias.peruanas.cajeroexpress.prodcat.ProdCatActivity
import timber.log.Timber
import javax.inject.Inject

class CategoriasFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: CategoriasViewModel
    lateinit var binding: FragmentCategoriasBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        /*binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_categorias, container, false
        )*/
        binding = FragmentCategoriasBinding.inflate(inflater, container, false)
        val view = binding.getRoot()
        viewModel = injectViewModel(viewModelFactory)
        initViewModel()
        initAdapter()

        return view
    }

    private fun initViewModel() {
        viewModel.obtenerListaCategorias.observe(viewLifecycleOwner, Observer { result ->
            initValidarCategoriaLista(result)
        })
    }

    private fun initValidarCategoriaLista(result: Resource<List<CategoriasUi>>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    categoriaAdapter.actualizarLista(result.data)
                }!!
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> {
                Timber.d("CONEXIÓN")
            }
        }
    }


    lateinit var categoriaAdapter: CategoriasAdapter

    private fun initAdapter() {
        categoriaAdapter = CategoriasAdapter {
            initStartActivityProdCat(it)
        }
        binding.recicladorCategorias.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recicladorCategorias.setItemAnimator(DefaultItemAnimator())
        binding.recicladorCategorias.adapter = categoriaAdapter
        binding.recicladorCategorias.setHasFixedSize(true)
    }

    private fun initStartActivityProdCat(it: CategoriasUi) {
        Timber.d("initStartActivityProdCat : %s ", it.categoriaId)
        val intent = Intent(requireContext(), ProdCatActivity::class.java)
        // intent.putExtra("id", it.id)
        intent.putExtra("categoriaId", it.categoriaId)
        intent.putExtra("name", it.categoriaNombre)
        intent.putExtra("items", it.items)
        intent.putExtra("image", it.imagen)
        startActivity(intent)
    }


}