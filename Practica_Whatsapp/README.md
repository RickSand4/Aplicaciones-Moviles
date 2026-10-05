# Práctica de WhatsApp en Java

Proyecto educativo construido con actividades Java, modelos, un BaseAdapter y layouts XML. Conserva los nombres y la estructura del ejercicio inicial.

## Abrir y ejecutar

Abrir esta carpeta en Android Studio, sincronizar Gradle y ejecutar el módulo app. Se conserva la configuración del proyecto: SDK 37, mínimo API 26 y CircleImageView 3.1.0.

## Qué incluye

- Usuarios con imagen circular, nombre, última conexión, último mensaje e indicador de lectura.
- Búsqueda por nombre y filtros Todos, No leídos, Favoritos y Grupos combinados. La búsqueda ignora mayúsculas, conserva acentos y limita a cinco resultados solo cuando hay texto. Al limpiar el buscador se muestran todos los usuarios de la categoría.
- Chat que recibe nombre_usuario e id_usuario mediante extras. Permite enviar mensajes locales, actualiza el último mensaje y marca la conversación como leída.
- Llamadas con favoritos editables mediante Más. Los iconos de teléfono y cámara muestran simulaciones y agregan entradas a Recientes. Los favoritos coinciden con los de la pantalla principal.
- Estados con imagen de fondo, foto circular, nombre y borde verde o blanco según estén pendientes o vistos.
- Canales seguidos con mensaje, fecha y contador. Abrir un canal marca sus mensajes como leídos.
- Sugerencias de canales con seguidores y botón Seguir. Explorar más muestra el resto de las sugerencias.

## Alcance

Es una demostración local: no realiza llamadas, no envía mensajes por Internet y no requiere permisos de teléfono o cámara. Los datos se comparten en memoria mientras vive el proceso de la aplicación. Al reiniciar el proceso se restablecen los ejemplos. No hay base de datos ni almacenamiento permanente.

## Cómo estudiar el código

1. models/Usuario.java y adapters/UsuarioAdapter.java: modelo, getters, setters, getView y enlace con item_usuario.xml.
2. MainActivity.java y util/FiltroUsuarios.java: lista completa, lista filtrada, TextWatcher y notifyDataSetChanged.
3. ChatActivity.java: putExtra/getStringExtra, identificador de usuario e inflado de item_mensaje.xml.
4. LlamadasActivity.java: recorrer listas, inflar filas XML y elegir favoritos con un diálogo.
5. NovedadesActivity.java: ScrollView, HorizontalScrollView, estados, contadores y acciones de canales.
6. datos/DatosDemo.java: datos de ejemplo compartidos para que los cambios se reflejen entre pantallas.

Todos los diseños están en res/layout. Llamadas y novedades inflan filas dentro de LinearLayout, igual que el adapter infla filas para ListView. No se anidan ListView dentro de ScrollView. FrameLayout solo se utiliza en la tarjeta de estado para superponer foto y nombre sobre el fondo.

## Comprobaciones

~~~powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
.\gradlew.bat :app:connectedDebugAndroidTest
~~~

La segunda instrucción necesita un dispositivo o emulador. FiltroUsuariosTest revisa las combinaciones y el límite de búsqueda. FlujoAplicacionTest recorre búsqueda, extras, envío local, favoritos, videollamada simulada, estados y canales.

El APK se genera en app/build/outputs/apk/debug/app-debug.apk.

