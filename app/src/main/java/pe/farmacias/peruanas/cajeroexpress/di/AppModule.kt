package pe.farmacias.peruanas.cajeroexpress.di

import android.app.Application
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pe.farmacias.peruanas.cajeroexpress.base.ResponseHandler
import pe.farmacias.peruanas.cajeroexpress.carrito.service.CarritoService
import pe.farmacias.peruanas.cajeroexpress.db.CajeroDB
import pe.farmacias.peruanas.cajeroexpress.detalles.service.DetallesService
import pe.farmacias.peruanas.cajeroexpress.di.base.CajeroAPI
import pe.farmacias.peruanas.cajeroexpress.di.base.CoroutineScropeIO
import pe.farmacias.peruanas.cajeroexpress.intro.local.service.LocalService
import pe.farmacias.peruanas.cajeroexpress.login.remote.LoginRemote
import pe.farmacias.peruanas.cajeroexpress.login.service.LoginService
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.service.CategoriasSevice
import pe.farmacias.peruanas.cajeroexpress.principal.foto.service.FotoService
import pe.farmacias.peruanas.cajeroexpress.principal.inicio.service.InicioService
import pe.farmacias.peruanas.cajeroexpress.principal.productos.service.ProductosService
import pe.farmacias.peruanas.cajeroexpress.principal.service.PrincipalService
import pe.farmacias.peruanas.cajeroexpress.prodcat.service.ProdCatService
import pe.farmacias.peruanas.cajeroexpress.util.Constantes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.xml.datatype.DatatypeConstants.MINUTES


@Module(includes = [ViewModelModule::class, CoreDataModule::class])
class AppModule {

    @Singleton
    @Provides
    @Named("apiLocal")
    internal fun provideRetrofitLocal(
        @CajeroAPI okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        val baseUrl = "http://10.18.3.20:8080/"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okhttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    @Named("apiRemote")
    internal fun provideRetrofitRemote(
        @CajeroAPI okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        val baseUrl = "https://cajeroexpress.solucionesfps.pe/"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okhttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }


    @CajeroAPI
    @Provides
    fun providePrivateOkHttpClient(
    ): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
        client.addInterceptor(interceptor)
        client.connectTimeout(Constantes.CONNECT_TIMEOUT.toLong(), TimeUnit.MINUTES)
        client.readTimeout(Constantes.READ_TIMEOUT.toLong(), TimeUnit.MINUTES)
        client.writeTimeout(Constantes.WRITE_TIMEOUT.toLong(), TimeUnit.MINUTES)
        client.retryOnConnectionFailure(true)
        return client.build()
    }


    @Singleton
    @Provides
    internal fun provideInicioFrService(@Named("apiLocal") retrofit: Retrofit): InicioService {
        return retrofit.create(InicioService::class.java)
    }

    @Singleton
    @Provides
    internal fun provideProductoFrService(@Named("apiLocal") retrofit: Retrofit): ProductosService {
        return retrofit.create(ProductosService::class.java)
    }

    @Singleton
    @Provides
    internal fun provideCategoriasFrService(@Named("apiLocal") retrofit: Retrofit): CategoriasSevice {
        return retrofit.create(CategoriasSevice::class.java)
    }

    @Singleton
    @Provides
    internal fun provideFotoFrService(@Named("apiLocal") retrofit: Retrofit): FotoService {
        return retrofit.create(FotoService::class.java)
    }

    @Singleton
    @Provides
    internal fun provideCarritoActiService(@Named("apiLocal") retrofit: Retrofit): CarritoService {
        return retrofit.create(CarritoService::class.java)
    }

    @Singleton
    @Provides
    internal fun provideDetallesActiService(@Named("apiLocal") retrofit: Retrofit): DetallesService {
        return retrofit.create(DetallesService::class.java)
    }

    @Singleton
    @Provides
    internal fun providePrincipalActiService(@Named("apiLocal") retrofit: Retrofit): PrincipalService {
        return retrofit.create(PrincipalService::class.java)
    }

    @Singleton
    @Provides
    internal fun provideProdCatActiService(@Named("apiLocal") retrofit: Retrofit): ProdCatService {
        return retrofit.create(ProdCatService::class.java)
    }

    @Singleton
    @Provides
    internal fun provideResponseHandler(): ResponseHandler {
        return ResponseHandler()
    }

    @Singleton
    @Provides
    fun provideCajeroDB(app: Application) = CajeroDB.getInstance(app)


    /*Remote Service*/
    @Singleton
    @Provides
    internal fun provideLoginRemoteDataSource(@Named("apiRemote") retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Singleton
    @Provides
    internal fun provideLocalRemoteDataSource(@Named("apiRemote") retrofit: Retrofit): LocalService {
        return retrofit.create(LocalService::class.java)
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
        client.addInterceptor(interceptor)
        client.retryOnConnectionFailure(true)
        return client.build()
    }

    /*@CoroutineScropeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)*/


}