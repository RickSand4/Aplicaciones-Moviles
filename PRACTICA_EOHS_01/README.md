# WhatsApp · Práctica Android

Aplicación educativa en Java y vistas Android, con interfaz en español y datos locales. No se conecta a WhatsApp ni a un servidor. No requiere permisos de contactos, cámara, teléfono o Internet.

## Ejecutar

1. Abrir esta carpeta en Android Studio y sincronizar Gradle.
2. Usar el SDK 37 y el JDK configurado por el proyecto. La versión mínima es Android 8.0 (API 26).
3. Seleccionar un dispositivo o emulador y ejecutar `app`.

El APK de depuración se genera en `app/build/outputs/apk/debug/app-debug.apk`.

## Funciones

- **Chats:** diez contactos/grupos con avatar circular, nombre, última conexión, último mensaje y mensajes sin leer. Los filtros Todos, No leídos, Favoritos y Grupos se combinan con la búsqueda por nombre. La búsqueda ignora mayúsculas y acentos; limita a cinco coincidencias cuando hay texto y vuelve a mostrar la lista del filtro al vaciarlo. `ContactAdapter.filter()` llama a `notifyDataSetChanged()`; un `DataSetObserver` actualiza el listado dentro del `ScrollView`.
- **Conversación:** al tocar un contacto, `MainActivity` envía `nombre_usuario` y `usuario_id` mediante `Intent.putExtra()`. `ChatActivity` recupera ambos extras, marca el chat como leído y permite guardar mensajes propios. Los mensajes persisten al cerrar la app y actualizan el listado. El borrador se conserva al recrear la pantalla. Cada conversación contiene mensajes iniciales de ejemplo.
- **Llamadas:** favoritos editables con Más, botones de voz y video e historial reciente. Las acciones muestran una simulación y agregan una entrada local; no llaman a personas reales.
- **Novedades:** estados con fondo ilustrado y avatar con borde verde (sin ver) o blanco (visto). Tocar un estado lo muestra y cambia el borde. Canales con título, mensaje, fecha y contador; abrir uno lo marca como leído. Las sugerencias muestran seguidores y permiten seguir un canal. Explorar más amplía las sugerencias.

Se usan `ScrollView`, `HorizontalScrollView`, `LinearLayout`, `ImageView` y [CircleImageView 3.1.0](https://github.com/hdodenhof/CircleImageView). Los avatares son imágenes de iniciales generadas localmente. `DemoStore` guarda mensajes, favoritos, estados vistos, canales y llamadas en `SharedPreferences`.

## Verificación

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

`ContactFilterTest` comprueba el límite, búsqueda sin acentos, combinación de filtros, resultados vacíos y restauración del listado al limpiar la búsqueda.

Para revisar manualmente: buscar `a`, probar cada filtro, abrir un chat y enviar un mensaje, volver al listado, editar favoritos en Llamadas y abrir/seguir estados y canales en Novedades. Los cambios persisten al reiniciar. Borrar los datos de la aplicación restablece la demostración.
