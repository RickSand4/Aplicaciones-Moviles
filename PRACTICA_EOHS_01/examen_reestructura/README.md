# EXAMEN_EOHS_01

Aplicación Android en Java y XML para los requisitos de Examen 1.pdf.

## Estructura

Se sigue la organización de DM_EOHS_06, MN_eohs_04 y Ejercicio02: variables al inicio, enlace con `findViewById`, eventos mediante `implements View.OnClickListener`, métodos en las actividades y datos en `ArrayList`.

```text
app/src/main/java/
    com/upiiz/examen_eohs_01/
        MainActivity.java
        RegistroActivity.java
        CustomListViewActivity.java
        ChatActivity.java
        adapters/
            CustomAdapter.java
            MensajeAdapter.java
    model/
        User.java
        Mensaje.java
```

- `MainActivity`: enlaza usuario y clave; `validarUsuario()` compara `admin` y `123456`. `abrirRegistro()` abre el registro mediante Intent.
- `RegistroActivity`: `registrarUsuario()` comprueba nombre de 3 caracteres y contraseñas iguales no vacías; `regresar()` vuelve al login.
- `CustomListViewActivity`: `cargarUsuarios()` agrega 20 usuarios al ArrayList. `onItemClick()` manda su id y nombre al chat usando `putExtra()`.
- `ChatActivity`: recibe los extras y `cargarMensajes()` agrega 20 mensajes. `enviarMensaje()` añade nuevos mensajes locales. Los nuevos mensajes se conservan al girar la pantalla.
- `CustomAdapter`: infla `custom_list_item.xml`, enlaza sus vistas y muestra cada usuario.
- `MensajeAdapter`: infla `custom_list_mensaje.xml`, muestra cada mensaje y coloca la burbuja a la izquierda o derecha con un `if`.
- `model`: clases normales con atributos privados, constructor, getters y setters.

Cada actividad hereda directamente de `AppCompatActivity`. Las validaciones y listas se encuentran en la actividad que las usa. No hay clases `final`, `BaseActivity`, `Validation` ni `DemoData`.

## Ejecutar

Abrir esta carpeta en Android Studio, sincronizar Gradle y ejecutar `app`.

Usuario: **admin**. Clave: **123456**.

El registro es una simulación que regresa al login; no sustituye la cuenta fija del examen. Los mensajes nuevos no se guardan en disco al salir del chat.

## Comprobación

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleDebugAndroidTest
```

Las pruebas de navegación en `ExamFlowTest` comprueban acceso, registro, los 20 usuarios, mensajes alternados, envío y recreación de la pantalla. Para ejecutarlas se requiere un dispositivo o emulador funcional. La prueba unitaria de plantilla no comprueba el flujo de pantallas.

Para revisar manualmente: ingresar credenciales incorrectas, registrar un nombre corto y contraseñas distintas, corregir el registro, ingresar con admin / 123456, recorrer los 20 usuarios, abrir distintos chats y comprobar las burbujas de ambos lados. Cada chat inicia con 20 mensajes.

APK: `app/build/outputs/apk/debug/app-debug.apk`.
