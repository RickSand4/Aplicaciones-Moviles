package com.upiiz.practica_whatsapp.util;

import com.upiiz.practica_whatsapp.models.Usuario;
import java.util.ArrayList;
import java.util.Locale;

public class FiltroUsuarios {
    public static final int LIMITE_BUSQUEDA = 5;
    // Separar esta operación permite probarla sin abrir una pantalla Android.
    public static ArrayList<Usuario> filtrar(ArrayList<Usuario> usuarios, String texto, String filtro) {
        String criterio = texto.trim().toLowerCase(Locale.ROOT);
        ArrayList<Usuario> resultado = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            boolean coincideNombre = usuario.getNombre().toLowerCase(Locale.ROOT).contains(criterio);
            boolean coincideFiltro = true;
            if (filtro.equals("No leídos")) coincideFiltro = usuario.isNoLeido();
            else if (filtro.equals("Favoritos")) coincideFiltro = usuario.isFavorito();
            else if (filtro.equals("Grupos")) coincideFiltro = usuario.isGrupo();
            if (coincideNombre && coincideFiltro) {
                resultado.add(usuario);
                if (!criterio.isEmpty() && resultado.size() >= LIMITE_BUSQUEDA) break;
            }
        }
        return resultado;
    }
}
