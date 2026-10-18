# EXAMEN_EOHS_01 · ChatApp

Aplicación Android en Java y XML conforme a **Examen 1.pdf**.

## Ejecutar

Abrir esta carpeta en Android Studio, sincronizar Gradle y ejecutar `app` en un dispositivo o emulador (Android 8.0 / API 26 o posterior). El proyecto conserva su configuración original de SDK 37 y JDK 25.

**Acceso:** usuario `admin`, contraseña `123456`.

## Requisitos implementados

| Valor | Requisito | Implementación |
| --- | --- | --- |
| 25% | Login, validar admin / 123456, abrir listado o registro | `MainActivity`, `Validation` |
| 25% | Nombre de al menos 3 caracteres, contraseñas coincidentes, volver al login | `RegisterActivity`, `Validation` |
| 20% | Mostrar 20 usuarios en CustomListView y abrir chat al tocar | `UsersActivity`, `UserAdapter`, `row_user.xml` |
| 30% | 20 mensajes guardados en List, CustomListView, izquierda y derecha | `ChatActivity`, `MessageAdapter`, `row_message_in.xml`, `row_message_out.xml` |

CustomListView se implementa con `ListView` y adaptadores propios derivados de `BaseAdapter`. Los 20 usuarios y las 20 intervenciones iniciales por conversación están en `DemoData`. Cada chat muestra el nombre elegido. También permite enviar mensajes locales y conserva los nuevos mensajes al girar la pantalla.

El registro es una simulación: valida los datos y vuelve al login. No crea una cuenta en un servidor ni sustituye las credenciales fijas exigidas por el examen. Correo y usuario se muestran como en la referencia; las reglas de aprobación son el nombre y las contraseñas. No se necesitan permisos de Internet. Al salir del chat, los mensajes adicionales no se guardan en disco.

## Comprobación

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

1. Intentar ingresar con valores vacíos y credenciales incorrectas; permanecer en login con error.
2. Abrir Crear una cuenta; probar nombre de 2 caracteres y contraseñas diferentes.
3. Usar nombre de 3 o más caracteres y contraseñas iguales no vacías; volver al login. Probar también Ya tienes una cuenta y la flecha de regreso.
4. Ingresar con `admin` / `123456`; recorrer los 20 usuarios hasta Ricardo Reyes.
5. Abrir distintos usuarios; verificar nombre y 20 mensajes alternados, desplazándose por toda la conversación.
6. Enviar un mensaje, girar el dispositivo y comprobar que se conserva. Volver al listado con la flecha.

El APK se genera en `app/build/outputs/apk/debug/app-debug.apk`.
