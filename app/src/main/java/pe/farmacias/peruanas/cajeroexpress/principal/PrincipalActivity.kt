package pe.farmacias.peruanas.cajeroexpress.principal

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.vincent.bottomnavigationbar.BottomItem
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.toolbar_home.view.*
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.Status
import pe.farmacias.peruanas.cajeroexpress.carrito.CarritoActivity
import pe.farmacias.peruanas.cajeroexpress.databinding.ActivityPrincipalBinding
import pe.farmacias.peruanas.cajeroexpress.detalles.DetallesActivity
import pe.farmacias.peruanas.cajeroexpress.di.base.injectViewModel
import pe.farmacias.peruanas.cajeroexpress.orden.OrdenActivity
import pe.farmacias.peruanas.cajeroexpress.principal.adapter.BuscadorProducto
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.CategoriasFragment
import pe.farmacias.peruanas.cajeroexpress.principal.foto.FotoFragment
import pe.farmacias.peruanas.cajeroexpress.principal.inicio.InicioFragment
import pe.farmacias.peruanas.cajeroexpress.principal.perfil.PerfilFragment
import pe.farmacias.peruanas.cajeroexpress.principal.productos.ProductosFragment
import pe.farmacias.peruanas.cajeroexpress.principal.productos.listener.ProductosListener
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi
import pe.farmacias.peruanas.cajeroexpress.principal.productos.service.ProductosResponse
import pe.farmacias.peruanas.cajeroexpress.util.Constantes.ZXING_CAMERA_PERMISSION
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class PrincipalActivity : DaggerAppCompatActivity(), ProductosListener {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: PrincipalViewModel
    lateinit var binding: ActivityPrincipalBinding

    private var mBnbDefaultList: MutableList<BottomItem>? = null

    private var mPackageName: String? = null

    lateinit var buscadorProducto: BuscadorProducto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_principal)
        viewModel = injectViewModel(viewModelFactory)
        initPermisos()

        initView()
        initPrimerFragmento()
        initBottomBar()
        initViewModel()
        initOnClick()
    }


    private fun initPermisos() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) !== PackageManager.PERMISSION_GRANTED
        ) run {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA), ZXING_CAMERA_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ZXING_CAMERA_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.d("ZXING_CAMERA_PERMISSION")
                } else {
                    Toast.makeText(
                        this,
                        "Please grant camera permission to use the QR Scanner",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    private fun initOnClick() {
        binding.include.imageViewCart.setOnClickListener {
            startActivity(Intent(this@PrincipalActivity, CarritoActivity::class.java))
        }
        binding.include.imageViewOrder.setOnClickListener {
            startActivity(Intent(this@PrincipalActivity, OrdenActivity::class.java))
        }
        binding.include.imageViewFoto.setOnClickListener {
            val tipoEstado = "agregarCarrito"
            startFragmentFoto(tipoEstado)
        }
        binding.btnBuscarProducto.setOnClickListener {
            val tipoEstado = "buscar"
            startFragmentFoto(tipoEstado)
        }
    }

    private fun startFragmentFoto(tipoEstado: String) {

        val fotoFragment = FotoFragment()
        val bundle = Bundle()
        bundle.putString("tipoEstado", tipoEstado);
        fotoFragment.setArguments(bundle)
        fotoFragment.show(supportFragmentManager, "ABC")
    }

    private fun initViewModel() {
        viewModel.posicion.observe(this, Observer { posicion ->
            initFragment(posicion)
        })

        /*  viewModel.obtenerLista.observe(this, Observer { result ->
              //initMostrarListaProductos(result)
              initMostrarListaBuscador(result)
          })*/
        viewModel.obtenerLista.observe(this, Observer { result ->
            //initMostrarListaBuscadorLive(result)
            initMostrarListaBuscador(result)
        })
        initViewModelCarrito()
    }


    private fun initMostrarListaBuscador(result: Resource<List<ProductosUi>>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    buscadorProducto.actualizarLista(result.data)
                }!!
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("ERROR DE CONEXIÓN")
        }
        binding.textViewBuscar?.setAdapter(buscadorProducto)

        binding.textViewBuscar.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as ProductosUi?
            binding.textViewBuscar.setText(selectedPoi?.nombre)
            initDetallesActivity(selectedPoi!!)
            binding.textViewBuscar.setText("")
        }
    }


    private fun initMostrarListaProductos(result: Resource<ProductosResponse>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    buscadorProducto.actualizarLista(result.data?.productoLista)
                }!!
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("ERROR DE CONEXIÓN")
        }
        binding.textViewBuscar?.setAdapter(buscadorProducto)

        binding.textViewBuscar.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as ProductosUi?
            binding.textViewBuscar.setText(selectedPoi?.nombre)
            initDetallesActivity(selectedPoi!!)
            binding.textViewBuscar.setText("")
        }
    }

    private fun initDetallesActivity(it: ProductosUi) {
        val intent = Intent(this, DetallesActivity::class.java)
        intent.putExtra("id", "001")
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


    private fun initView() {
        mPackageName = applicationInfo.packageName
        val item1 = BottomItem()
        item1.text = "Inicio"
        // resources.getIdentifier("ic_tint_bag", "drawable", mPackageName)
        item1.iconResID = resources.getIdentifier("ic_tint_bag", "drawable", mPackageName)
        item1.activeBgResID = R.drawable.bg_bottom_navi_selected
        item1.inactiveBgResID = R.drawable.bg_bottom_navi_normal
        binding.bottomBar.addItem(item1)
        mBnbDefaultList?.add(item1)

        val item2 = BottomItem()
        item2.text = "Productos"
        item2.iconResID = resources.getIdentifier("ic_tint_book", "drawable", mPackageName)
        item2.activeBgResID = R.drawable.bg_bottom_navi_selected
        item2.inactiveBgResID = R.drawable.bg_bottom_navi_normal
        binding.bottomBar.addItem(item2)
        mBnbDefaultList?.add(item2)

        val item3 = BottomItem()
        item3.text = "Categorias"
        item3.iconResID = resources.getIdentifier("ic_tint_cart", "drawable", mPackageName)
        item3.activeBgResID = R.drawable.bg_bottom_navi_selected
        item3.inactiveBgResID = R.drawable.bg_bottom_navi_normal
        binding.bottomBar.addItem(item3)
        mBnbDefaultList?.add(item3)

        val item4 = BottomItem()
        item4.text = "Perfil"
        item4.iconResID = resources.getIdentifier("ic_tint_list", "drawable", mPackageName)
        item4.activeBgResID = R.drawable.bg_bottom_navi_selected
        item4.inactiveBgResID = R.drawable.bg_bottom_navi_normal
        binding.bottomBar.addItem(item4)
        mBnbDefaultList?.add(item4)


        binding.bottomBar.initialize()

        buscadorProducto = BuscadorProducto(this, android.R.layout.simple_list_item_1, ArrayList<ProductosUi>())

        binding.textViewBuscar?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Timber.d("beforeTextChanged")
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Timber.d("onTextChanged")
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().length > 3) {
                    Timber.d("afterTextChanged: %s ", editable.toString())
                    initAdapterBuscar(editable.toString())
                } else {
                    return
                }
            }
        })
    }

    private fun initAdapterBuscar(textoBuscar: String) {
        viewModel.initBuscarTexto(textoBuscar)
    }

    private fun initFragment(posicion: Int?) {
        when (posicion) {
            0 -> initPrimerFragmento()
            1 -> initProductosFragmento()
            2 -> initCategoriasFragmento()
            3 -> initPerfilFragmento()
            else -> Timber.d("Default::Error")
        }
    }

    private fun initPerfilFragmento() {
        replaceFragment(PerfilFragment())
    }

    private fun initCategoriasFragmento() {
        replaceFragment(CategoriasFragment())
    }

    private fun initProductosFragmento() {
        replaceFragment(ProductosFragment())
    }

    private fun initPrimerFragmento() {
        replaceFragment(InicioFragment())
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framelayout, fragment)
        transaction.commit()
    }

    private fun initBottomBar() {

        binding.bottomBar.addOnSelectedListener { _, newPosition ->
            viewModel.initPosicionAdapter(newPosition)
        }
    }


    override fun onClickAgregarCarrito() {
        Timber.d("onClickAgregarCarrito PrincipalActivity")
        initViewModelCarrito()
    }

    private fun initViewModelCarrito() {
        viewModel.carritoConteo.observe(this, Observer { res ->
            initObtenerCarritoConteo(res)
        })
    }

    private fun initObtenerCarritoConteo(result: Resource<String>?) {
        when (result?.status) {
            Status.SUCCESS -> {
                binding.include.textViewNumero.text = result.data
            }
            Status.LOADING -> Timber.d("CARGANDO")
            Status.ERROR -> Timber.d("ERROR TRAER DB")

        }
    }

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

}

