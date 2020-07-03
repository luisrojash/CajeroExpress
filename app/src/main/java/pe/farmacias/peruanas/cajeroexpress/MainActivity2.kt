package pe.farmacias.peruanas.cajeroexpress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main2.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService



class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        sample_text.text = stringFromJNI()



    }
    /* val storageConnectionString = "DefaultEndpointsProtocol=https;" +
     "AccountName=<account-name>;" +
     "AccountKey=<account-key>"*/

    external fun stringFromJNI(): String

    companion object {

        init {
            System.loadLibrary("native-lib")
        }
    }
}
