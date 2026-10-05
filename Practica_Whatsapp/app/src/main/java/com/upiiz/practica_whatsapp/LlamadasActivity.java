package com.upiiz.practica_whatsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.upiiz.practica_whatsapp.models.*;
import com.upiiz.practica_whatsapp.datos.DatosDemo;
import com.upiiz.practica_whatsapp.util.Pantalla;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;

public class LlamadasActivity extends AppCompatActivity {
    private LinearLayout llFavoritos, llRecientes;
    private DatosDemo datos;

    @Override protected void onCreate(Bundle estado) {
        super.onCreate(estado);
        setContentView(R.layout.activity_llamadas);
        Pantalla.ajustarBarras(this);
        datos = DatosDemo.obtener();
        llFavoritos = findViewById(R.id.llFavoritos);
        llRecientes = findViewById(R.id.llRecientes);
        findViewById(R.id.btnRegresar).setOnClickListener(v -> finish());
        findViewById(R.id.btnMas).setOnClickListener(v -> seleccionarFavoritos());
        mostrarLlamadas();
    }

    private void mostrarLlamadas() {
        llFavoritos.removeAllViews();
        int favoritos = 0;
        for (Usuario usuario : datos.getUsuarios()) {
            if (!usuario.isFavorito() || usuario.isGrupo()) continue;
            favoritos++;
            View fila = getLayoutInflater().inflate(R.layout.item_favorito,llFavoritos,false);
            ((CircleImageView)fila.findViewById(R.id.ivFavorito)).setImageResource(usuario.getImagen());
            ((TextView)fila.findViewById(R.id.tvFavorito)).setText(usuario.getNombre());
            View telefono = fila.findViewById(R.id.ivTelefono);
            View video = fila.findViewById(R.id.ivVideo);
            telefono.setContentDescription(getString(R.string.llamar_a,usuario.getNombre()));
            video.setContentDescription(getString(R.string.video_a,usuario.getNombre()));
            telefono.setOnClickListener(v -> simularLlamada(usuario,false));
            video.setOnClickListener(v -> simularLlamada(usuario,true));
            llFavoritos.addView(fila);
        }
        findViewById(R.id.tvSinFavoritos).setVisibility(favoritos == 0 ? View.VISIBLE : View.GONE);
        llRecientes.removeAllViews();
        for (Llamada llamada : datos.getLlamadas()) {
            Usuario usuario = llamada.getUsuario();
            View fila = getLayoutInflater().inflate(R.layout.item_llamada,llRecientes,false);
            ((CircleImageView)fila.findViewById(R.id.ivLlamada)).setImageResource(usuario.getImagen());
            ((TextView)fila.findViewById(R.id.tvNombreLlamada)).setText(usuario.getNombre());
            ((TextView)fila.findViewById(R.id.tvFechaLlamada)).setText(getString(
                    llamada.isVideo() ? R.string.fecha_video : R.string.fecha_voz,llamada.getFecha()));
            View telefono = fila.findViewById(R.id.ivTelefono);
            telefono.setContentDescription(getString(R.string.llamar_a,usuario.getNombre()));
            telefono.setOnClickListener(v -> simularLlamada(usuario,false));
            llRecientes.addView(fila);
        }
    }

    private void seleccionarFavoritos() {
        ArrayList<Usuario> personas = new ArrayList<>();
        for (Usuario usuario : datos.getUsuarios()) if (!usuario.isGrupo()) personas.add(usuario);
        String[] nombres = new String[personas.size()];
        boolean[] seleccionados = new boolean[personas.size()];
        for (int i=0;i<personas.size();i++) {
            nombres[i] = personas.get(i).getNombre();
            seleccionados[i] = personas.get(i).isFavorito();
        }
        new AlertDialog.Builder(this).setTitle(R.string.elegir_favoritos)
                .setMultiChoiceItems(nombres,seleccionados,(dialogo,posicion,marcado) -> seleccionados[posicion] = marcado)
                .setNegativeButton(R.string.cancelar,null)
                .setPositiveButton(R.string.guardar,(dialogo,boton) -> {
                    for (int i=0;i<personas.size();i++) personas.get(i).setFavorito(seleccionados[i]);
                    mostrarLlamadas();
                }).show();
    }

    private void simularLlamada(Usuario usuario, boolean video) {
        datos.registrarLlamada(usuario,video);
        mostrarLlamadas();
        new AlertDialog.Builder(this)
                .setTitle(video ? R.string.videollamada_simulada : R.string.llamada_simulada)
                .setMessage(getString(R.string.detalle_llamada,usuario.getNombre()))
                .setPositiveButton(R.string.finalizar,null).show();
    }
}
