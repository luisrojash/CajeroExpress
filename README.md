# Cajero Express


Cajero Express es un aplicación mobile(Android),el proyecto fue orientado a un sistema de venta con carrito de compra, los pagos se haran en multiples tarjetas.
La aplicación podra ayudar armar su carrito de compra para sus clientes respectivos y proceder el pago, el proyecto fue desarrollado en Kotlin con las siguientes herramientas:

  - JetPack
  - Arquitectura Componentes
  - Sonarqube
# Introducción
La aplicación utiliza Clean Architecture basada en MVVM y patrones de repositorio. Implementado
Los principios de arquitectura siguen los recomendados por Google [Guia de la Arquitectura](https://developer.android.com/jetpack/docs/guide).
La aplicación está escrita completamente en Kotlin.

Android Jetpack se usa como un pegamento de Arquitectura que incluye, entre otros, ViewModel, LiveData,
Ciclos de vida, navegación, sala y enlace de datos. Vea una lista completa en la sección "Bibliotecas utilizadas".

La aplicación realiza solicitudes HTTP de red a través de Retrofit, OkHttp y GSON. Los datos cargados se guardan en
Sala de base de datos basada en SQL, que sirve como fuente única de verdad y admite el modo fuera de línea.
La biblioteca de paginación se utiliza para la paginación de datos en línea y fuera de línea.

Kotlin Coroutines gestiona subprocesos en segundo plano con código simplificado y reduce las necesidades de devoluciones de llamada.
Se prefieren las funciones de combinación de Coroutines y Kotlin (transformación, colecciones)
sobre RxJava 2.

Work Manager realiza tareas de sincronización que son compatibles con Doze Mode y usan la batería de manera eficiente.
El componente de navegación gestiona la navegación en la aplicación.

Dagger 2 se usa para la inyección de dependencia.

Glide se utiliza para cargar imágenes y Timber para iniciar sesión.

Stetho se utiliza para potenciar las habilidades de depuración (como el registro de llamadas de red, la descripción general del contenido de la base de datos,
Vista de jerarquía de interfaz de usuario, etc.).

![Guide to app architecture](fotos/guide-to-app-architecture.png "Guide to app architecture")
 
 
# Bibliotecas utilizadas!

* [Base][0] - Componentes para las capacidades del sistema central, extensiones de Kotlin y soporte para pruebas multidex y automatizadas.
  * [AppCompat][1] - Degradar con gracia en versiones anteriores de Android.
  * [Android KTX][2] - Escriba un código de Kotlin más conciso e idiomático.
  * [Test][4] - Un marco de prueba de Android para pruebas de UI de unidad y tiempo de ejecución.
* [Arquitectura][10] - Una colección de bibliotecas que lo ayudan a diseñar diseños robustos, comprobables y Aplicaciones mantenibles. Comience con clases para administrar el ciclo de vida de sus componentes UI y manejar datos persistencia.
  * [Data Binding][11] - Vincular declarativamente datos observables a elementos de la IU.
  * [Lifecycles][12] - Cree una interfaz de usuario que responda automáticamente a los eventos del ciclo de vida..
  * [LiveData][13] - Cree objetos de datos que notifiquen las vistas cuando cambie la base de datos subyacente.
  * [Navigation][14] - Maneja todo lo necesario para la navegación en la aplicación.
  * [Room][16] -Acceda a la base de datos SQLite de su aplicación con objetos integrados en la aplicación y comprobaciones en tiempo de compilación.
  * [ViewModel][17] - Almacenar datos relacionados con la interfaz de usuario que no se destruyen en las rotaciones de aplicaciones. Programar fácilmente tareas asincrónicas para una ejecución óptima.
  * [WorkManager][18] - Administre sus trabajos en segundo plano de Android.
* [UI][30] - Detalles sobre por qué y cómo usar los componentes de la interfaz de usuario en sus aplicaciones, juntos o por separado
  * [Animations & Transitions][31] - Mueve widgets y pasa de una pantalla a otra.
  * [Fragment][34] - Una unidad básica de interfaz de usuario composable.
  * [Layout][35] - Diseñe widgets usando diferentes algoritmos.
* Tercera Parte
  * [Glide][90] para cargar imágenes
  * [Kotlin Coroutines][91] o administrar subprocesos en segundo plano con código simplificado y reducir las necesidades de devoluciones de llamada

[0]: https://developer.android.com/jetpack/components
[1]: https://developer.android.com/topic/libraries/support-library/packages#v7-appcompat
[2]: https://developer.android.com/kotlin/ktx
[4]: https://developer.android.com/training/testing/
[10]: https://developer.android.com/jetpack/arch/
[11]: https://developer.android.com/topic/libraries/data-binding/
[12]: https://developer.android.com/topic/libraries/architecture/lifecycle
[13]: https://developer.android.com/topic/libraries/architecture/livedata
[14]: https://developer.android.com/topic/libraries/architecture/navigation/
[16]: https://developer.android.com/topic/libraries/architecture/room
[17]: https://developer.android.com/topic/libraries/architecture/viewmodel
[18]: https://developer.android.com/topic/libraries/architecture/workmanager
[30]: https://developer.android.com/guide/topics/ui
[31]: https://developer.android.com/training/animation/
[34]: https://developer.android.com/guide/components/fragments
[35]: https://developer.android.com/guide/topics/ui/declaring-layout
[90]: https://bumptech.github.io/glide/
[91]: https://kotlinlang.org/docs/reference/coroutines-overview.html