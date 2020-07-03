package pe.farmacias.peruanas.cajeroexpress.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.Dispatchers
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.model.Usuario
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    repository: LoginRepository
) : ViewModel() {

    val usuarioLiveData = MutableLiveData<Usuario>()

    fun onLoginButton(usuario: String, clave: String) {
        val usuario = Usuario("", usuario, clave, "", "", "", "")
        usuarioLiveData.value = usuario
    }

    val initLogin = repository.onListaProductos



    val initListCategorias = repository.onListaCategorias

//    var obtenerCategorias = liveData(Dispatchers.IO) {
//        emit(Resource.loading(null))
//        emit(repository.obtenerListaCategorias())
//    }
}