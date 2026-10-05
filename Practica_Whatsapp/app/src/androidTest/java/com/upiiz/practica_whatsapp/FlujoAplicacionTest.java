package com.upiiz.practica_whatsapp;

import android.graphics.Bitmap;
import android.content.Context;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.upiiz.practica_whatsapp.datos.DatosDemo;
import com.upiiz.practica_whatsapp.models.Usuario;
import java.io.File;
import java.io.FileOutputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class FlujoAplicacionTest {
    private void captura(String nombre) throws Exception {
        Context contexto = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Bitmap imagen = InstrumentationRegistry.getInstrumentation().getUiAutomation().takeScreenshot();
        assertNotNull(imagen);
        try (FileOutputStream salida = new FileOutputStream(new File(contexto.getExternalFilesDir(null),nombre + ".png"))) {
            imagen.compress(Bitmap.CompressFormat.PNG,100,salida);
        }
        imagen.recycle();
    }

    @Test public void recorridoCompleto() throws Exception {
        try (ActivityScenario<MainActivity> activity = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.tvResultados)).check(matches(withText("Conversaciones: 9")));
            captura("01_chats");
            onView(withId(R.id.etBuscar)).perform(replaceText("a"),closeSoftKeyboard());
            onView(withId(R.id.tvResultados)).check(matches(withText("Resultados: 5 · máximo 5")));
            onView(withId(R.id.etBuscar)).perform(replaceText("ana"),closeSoftKeyboard());
            onView(withId(R.id.btnFavoritos)).perform(click());
            onView(withText("Ana")).perform(click());
            onView(withId(R.id.tvNombreChat)).check(matches(withText("Ana")));
            onView(withId(R.id.etMensaje)).perform(replaceText("Mensaje de prueba"),closeSoftKeyboard());
            onView(withId(R.id.btnEnviar)).perform(click());
            onView(withText("Mensaje de prueba")).check(matches(isDisplayed()));
            captura("02_chat");
            onView(withId(R.id.btnRegresar)).perform(click());
            onView(withId(R.id.etBuscar)).check(matches(withText("ana")));
            onView(withId(R.id.etBuscar)).perform(replaceText(""),closeSoftKeyboard());
            onView(withId(R.id.btnTodos)).perform(click());
            onView(withId(R.id.btnLlamadas)).perform(click());
            captura("03_llamadas");
            onView(withId(R.id.btnMas)).perform(click());
            onView(withText("Mateo")).perform(click());
            onView(withText("Guardar")).perform(click());
            onView(withContentDescription("Videollamar a Mateo")).perform(click());
            onView(withText("Videollamada simulada")).check(matches(isDisplayed()));
            onView(withText("Finalizar")).perform(click());
            onView(withId(R.id.btnRegresar)).perform(click());
            onView(withId(R.id.btnFavoritos)).perform(click());
            onView(withText("Mateo")).check(matches(isDisplayed()));
            onView(withId(R.id.btnNovedades)).perform(click());
            captura("04_novedades");
            onView(withContentDescription("Estado de Sofía, sin ver")).perform(click());
            onView(withText("Cerrar")).perform(click());
            onView(withContentDescription("Estado de Sofía, visto")).check(matches(isDisplayed()));
            onView(withText("Noticias UPIIZ")).perform(scrollTo(),click());
            onView(withText("Cerrar")).perform(click());
            assertEquals(0,DatosDemo.obtener().getCanales().get(0).getSinLeer());
            onView(withId(R.id.btnExplorar)).perform(scrollTo(),click());
            onView(withContentDescription("Seguir Rutas de México")).perform(scrollTo(),click());
            assertTrue(DatosDemo.obtener().getCanales().get(4).isSeguido());
            onView(withId(R.id.btnRegresar)).perform(click());
            onView(withId(R.id.btnTodos)).perform(click());
            onView(withId(R.id.etBuscar)).perform(replaceText("nadie"),closeSoftKeyboard());
            onView(withId(R.id.tvSinUsuarios)).check(matches(isDisplayed()));
            onView(withId(R.id.etBuscar)).perform(replaceText(""),closeSoftKeyboard());
            // Restaurar los datos modificados por la prueba.
            DatosDemo.obtener().buscarUsuario(2).setFavorito(false);
        }
    }
}

