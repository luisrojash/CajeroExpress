package pe.farmacias.peruanas.cajeroexpress.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_login2.*
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.base.Result
import pe.farmacias.peruanas.cajeroexpress.databinding.ActivityLogin2Binding
import pe.farmacias.peruanas.cajeroexpress.di.base.ViewModelFactory
import pe.farmacias.peruanas.cajeroexpress.di.base.injectViewModel
import pe.farmacias.peruanas.cajeroexpress.model.Productos
import pe.farmacias.peruanas.cajeroexpress.model.Usuario
import pe.farmacias.peruanas.cajeroexpress.principal.PrincipalActivity
import timber.log.Timber
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {

    lateinit var binding: ActivityLogin2Binding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login2)
        viewModel = injectViewModel(viewModelFactory)
        viewModel.usuarioLiveData.observe(this, Observer { usuario ->
            initValidateUsuario(usuario)
        })

        initView()
    }

    private fun initValidateUsuario(usuario: Usuario?) {
        if (usuario!!.usuario.equals("admin") && usuario!!.claveUsuario.equals("admin")) {
            mostrarProgressBar()
            viewModel.initLogin.observe(this, Observer {
                initValidateStatus(it)
            })
        } else {
            Toast.makeText(applicationContext, "usuarios Invalidos ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initValidateStatus(it: Result<List<Productos>>?) {
        when (it?.status) {
            Result.Status.LOADING -> {
                mostrarProgressBar()
                Timber.d("LOADING")
            }
            Result.Status.SUCCESS -> {
                mostrarProgressBar()
                initViewModelCategoria()
                Timber.d("SUCCESS")
                //initStarActivity()
            }
            Result.Status.ERROR -> {
                Timber.d("ERROR")
            }
        }
    }

    private fun initViewModelCategoria() {
        viewModel.initListCategorias.observe(this, Observer {
            when (it?.status) {
                Result.Status.LOADING -> {
                    mostrarProgressBar()
                    Timber.d("LOADING")
                }
                Result.Status.SUCCESS -> {
                    initStarActivity()
                }
                Result.Status.ERROR -> {
                    Timber.d("ERROR")
                }
            }
        })
    }


    private fun initView() {
        binding.loginButton.setOnClickListener {
            val usuario = editextUsuario.text.toString().trim()
            val clave = editextClave.text.toString().trim()
            viewModel.onLoginButton(usuario, clave)
        }
    }

    private fun mostrarProgressBar() {
       // binding.spinKit.visibility = View.VISIBLE
        binding.editextUsuario.isEnabled = false
        binding.editextClave.isEnabled = false
    }

    private fun ocultarProgresBar() {
       // binding.spinKit.visibility = View.GONE
        binding.editextUsuario.isEnabled = true
        binding.editextClave.isEnabled = true
    }

    private fun initStarActivity() {
        val intent = Intent(this, PrincipalActivity::class.java)
        startActivity(intent)
    }

}