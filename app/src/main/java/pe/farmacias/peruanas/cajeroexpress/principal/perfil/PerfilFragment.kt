package pe.farmacias.peruanas.cajeroexpress.principal.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import dagger.android.support.DaggerFragment
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.Status
import pe.farmacias.peruanas.cajeroexpress.databinding.FragmentPerfilBinding
import pe.farmacias.peruanas.cajeroexpress.di.base.injectViewModel
import timber.log.Timber
import javax.inject.Inject

class PerfilFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PerfilViewModel
    lateinit var binding: FragmentPerfilBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)
        val view = binding.getRoot()
        viewModel = injectViewModel(viewModelFactory)
        initView()
        initViewModel()
        return view
    }

    private fun initViewModel() {
        viewModel.carritoConteo.observe(viewLifecycleOwner, Observer { result ->
            obtenerConteoCarrito(result)
        })
        viewModel.ordenConteo.observe(viewLifecycleOwner, Observer { result ->
            obtenerConteoOrden(result)
        })
    }

    private fun obtenerConteoOrden(result: Resource<String>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                binding.txtOrden.text = result.data
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("ERROR TRAER DB")
        }
    }

    private fun obtenerConteoCarrito(result: Resource<String>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                binding.txtCarrito.text = result.data
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("ERROR TRAER DB")
        }
    }

    private fun initView() {
        Glide.with(requireContext())
            .load("https://krazytech.com/wp-content/uploads/Face-detection-and-recognition-e1462652980288.jpg")
            .into(binding.circularImageView)

    }

}