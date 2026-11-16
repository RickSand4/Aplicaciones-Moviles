package com.upiiz.dm_eohs_practica_ii.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public final class Preferencias {
    private static final String PREFS = "explorador_libros_prefs";
    private static final String KEY_USUARIO = "usuario";
    private static final String KEY_TEMA_OSCURO = "tema_oscuro";
    private static final String KEY_ULTIMA_SECCION = "ultima_seccion";

    public static final String SECCION_CATALOGO = "catalogo";
    public static final String SECCION_FAVORITOS = "favoritos";

    private Preferencias() {
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static void guardarUsuario(Context context, String usuario) {
        prefs(context).edit().putString(KEY_USUARIO, usuario).apply();
    }

    public static String obtenerUsuario(Context context) {
        return prefs(context).getString(KEY_USUARIO, "");
    }

    public static void guardarTemaOscuro(Context context, boolean oscuro) {
        prefs(context).edit().putBoolean(KEY_TEMA_OSCURO, oscuro).apply();
    }

    public static boolean esTemaOscuro(Context context) {
        return prefs(context).getBoolean(KEY_TEMA_OSCURO, false);
    }

    public static void aplicarTema(Context context) {
        AppCompatDelegate.setDefaultNightMode(
                esTemaOscuro(context)
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    public static void guardarUltimaSeccion(Context context, String seccion) {
        prefs(context).edit().putString(KEY_ULTIMA_SECCION, seccion).apply();
    }

    public static String obtenerUltimaSeccion(Context context) {
        return prefs(context).getString(KEY_ULTIMA_SECCION, SECCION_CATALOGO);
    }
}
