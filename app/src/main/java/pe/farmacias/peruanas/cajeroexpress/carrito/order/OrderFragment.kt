package pe.farmacias.peruanas.cajeroexpress.carrito.order


import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerDialogFragment
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.Status
import pe.farmacias.peruanas.cajeroexpress.databinding.OrderDialogFragmentBinding
import pe.farmacias.peruanas.cajeroexpress.di.base.injectViewModel
import pe.farmacias.peruanas.cajeroexpress.model.Orden
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class OrderFragment : DaggerDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var binding: OrderDialogFragmentBinding
    lateinit var viewModel: OrderViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.order_dialog_fragment,
            container,
            false
        )
        val view = binding.getRoot()
        viewModel = injectViewModel(viewModelFactory)
        initView()
        initViewModel()
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return view
    }

    private fun initViewModel() {
        viewModel.guardarOrden.observe(this, androidx.lifecycle.Observer {
            initResultadoGuardarOrden(it)
        })

    }

    private fun initResultadoGuardarOrden(result: Resource<String>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                dismiss()
                Timber.d("GUARDADO CORRECTAMENTE")

            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("CONEXIÓN")
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initView() {
        val totalCantidad = arguments?.getString("totalCantidad")
        val totalSoles = arguments?.getString("totalSoles")
        val nombreLocal = binding.nombreLocal.text.toString()
        val nombreUsuario = binding.textViewNombreUsuario.text.toString()
        val numeroTarjeta = binding.numeroTarjeta.text.toString()

        binding.totalCantidad.text = totalCantidad.toString() + "- Items"
        binding.totalSoles.text = "S./ " + totalSoles.toString()


        val df = SimpleDateFormat("EEE, d MMM yyyy, HH:mm")
        val fecha = df.format(Calendar.getInstance().time)
        binding.textViewFecha.text = fecha

        binding.btnOrdernar.setOnClickListener {

            val orden = Orden(
                fecha,
                numeroTarjeta,
                nombreUsuario,
                nombreLocal,
                totalSoles!!,
                totalCantidad!!, "",
                ""
            )
            viewModel.setOrder(orden)
        }
    }


}