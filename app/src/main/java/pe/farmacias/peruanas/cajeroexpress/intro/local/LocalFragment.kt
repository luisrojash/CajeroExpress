package pe.farmacias.peruanas.cajeroexpress.intro.local

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import dagger.android.support.DaggerFragment
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.base.Result
import pe.farmacias.peruanas.cajeroexpress.base.Status
import pe.farmacias.peruanas.cajeroexpress.databinding.FragmentLocalBinding
import pe.farmacias.peruanas.cajeroexpress.di.base.injectViewModel

import timber.log.Timber
import javax.inject.Inject

class LocalFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var binding: FragmentLocalBinding
    lateinit var viewModel: LocalViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_local, container, false
        )
        val view = binding.getRoot()
        viewModel = injectViewModel(viewModelFactory)
        initView()
        return view
    }

    private fun initView() {
        viewModel.validarLocal.observe(viewLifecycleOwner, Observer { result ->
            when (result?.status) {
                Status.SUCCESS -> {
                    Timber.d("CARGANDO CORRECTAMENTE: ")
                }
                Status.LOADING -> Timber.d("CARGANDO")
                Status.ERROR -> Timber.d("ERROR CONEXIÓN")
                else -> {
                    Timber.d("Ocurrio Algun Problema")
                }
            }
        })


        binding.editTextLocal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                Timber.d("beforeTextChanged")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    binding.textViewNombreLocal.setText("Local")
                    return
                }
                binding.textViewNombreLocal.setText(s)
            }

            override fun afterTextChanged(s: Editable) {
                Timber.d("afterTextChanged")
            }
        })
        binding.next.setOnClickListener { v ->
            val nombreLocal: String = binding.editTextLocal.text.toString()
            if (nombreLocal.isEmpty()) {
                mostrarMensaje("Ingrese Datos Local")
                return@setOnClickListener
            }
            viewModel.setCodigoLocal(nombreLocal)
            //v.findNavController().navigate(R.id.action_navigation_intro_to_navigation_login)
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(activity, mensaje, Toast.LENGTH_SHORT).show()
    }
}