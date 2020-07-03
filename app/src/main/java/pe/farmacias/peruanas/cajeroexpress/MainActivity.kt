package pe.farmacias.peruanas.cajeroexpress

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pe.farmacias.peruanas.cajeroexpress.databinding.ActivityMainBinding
import pe.farmacias.peruanas.cajeroexpress.util.Constantes

import timber.log.Timber


class MainActivity : AppCompatActivity() {

    // lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSendData("consumo", "1.00", "PEN")
        //initSendDataConfiguracion(Constantes.IZI_CONFIGURACION, true)
    }

    private fun initSendDataConfiguracion(iziConfiguracion: String, b: Boolean) {
        /*val intent = Intent(this, IzipaySDK::class.java)
        val operation = OperationInfo()
        operation.tipoOperacion = iziConfiguracion
       // operation.importe = importe.toDouble()
        //operation.tipoMoneda = mone
        operation.isPopUp = b
        val bundle = Bundle()
        bundle.putSerializable(OperationInfo.OPERATION_INFO, operation)
        bundle.putString("entorno", "P")
        intent.putExtras(bundle)

        startActivityForResult(intent, 1300)*/
    }

    private fun initSendData(consumo: String, importe: String, mone: String) {
        /*val intent = Intent(this, IzipaySDK::class.java)
        val operation = OperationInfo()
        operation.tipoOperacion = consumo
        operation.importe = importe.toDouble()
        operation.tipoMoneda = mone
        operation.isPopUp = true
        val bundle = Bundle()
        bundle.putSerializable(OperationInfo.OPERATION_INFO, operation)
        bundle.putString("entorno", "P")
        intent.putExtras(bundle)
        //  operation.tipoOperacion.setvalue()
        startActivityForResult(intent, 1300)*/
    }

  /*  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val stringBuilder = StringBuilder()
        if (requestCode == Constantes.IZI_REQUEST_CODE) {
            val bundle = data?.extras
            //val operationResult = bundle?.get(Constantes.IZI_OP)
            Timber.d("requestCode : requestCode")
        } else {
            Timber.d("timBer : else")
        }
    }*/
}
