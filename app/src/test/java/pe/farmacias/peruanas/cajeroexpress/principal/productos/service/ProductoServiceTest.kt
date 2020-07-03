package pe.farmacias.peruanas.cajeroexpress.principal.productos.service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

//@RunWith(JUnit4::class)
class ProductoServiceTest {
/*
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: ProductosService

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductosService::class.java)
    }

    @After
    fun stopService() {
        //  mockWebServer.shutdown()
    }

    @Test
    fun getLegoSetsResponse() {
        runBlocking {
            enqueueResponse("legosets.json")
            val resultResponse = service.obtenerProductos2().body()
            val legoSets = resultResponse?.productoLista

            //  assertThat(resultResponse.count, CoreMatchers.`is`(9))
            assertThat(legoSets?.size, CoreMatchers.`is`(9))
        }
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader
            ?.getResourceAsStream("api-response/$fileName")
        Timber.d("inputStream : %s", inputStream.toString())
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse.setBody(
                source.readString(Charsets.UTF_8)
            )
        )
    }
*/

}