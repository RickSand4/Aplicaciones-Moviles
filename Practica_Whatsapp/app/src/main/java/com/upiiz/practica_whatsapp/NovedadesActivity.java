package com.upiiz.practica_whatsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.upiiz.practica_whatsapp.datos.DatosDemo;
import com.upiiz.practica_whatsapp.models.*;
import com.upiiz.practica_whatsapp.util.Pantalla;
import de.hdodenhof.circleimageview.CircleImageView;

public class NovedadesActivity extends AppCompatActivity {
    private DatosDemo datos;
    private LinearLayout llEstados, llCanales, llSugerencias;
    private boolean explorarTodos;

    @Override protected void onCreate(Bundle estado) {
        super.onCreate(estado);
        setContentView(R.layout.activity_novedades);
        Pantalla.ajustarBarras(this);
        datos = DatosDemo.obtener();
        llEstados = findViewById(R.id.llEstados);
        llCanales = findViewById(R.id.llCanales);
        llSugerencias = findViewById(R.id.llSugerencias);
        if (estado != null) explorarTodos = estado.getBoolean("explorar");
        findViewById(R.id.btnRegresar).setOnClickListener(v -> finish());
        findViewById(R.id.btnExplorar).setOnClickListener(v -> {
            explorarTodos = true; mostrarCanales();
        });
        mostrarEstados();
        mostrarCanales();
    }

    private void mostrarEstados() {
        llEstados.removeAllViews();
        for (Estado estado : datos.getEstados()) {
            View tarjeta = getLayoutInflater().inflate(R.layout.item_estado,llEstados,false);
            ((ImageView)tarjeta.findViewById(R.id.ivFondoEstado)).setImageResource(estado.getFondo());
            CircleImageView foto = tarjeta.findViewById(R.id.ivFotoEstado);
            foto.setImageResource(estado.getUsuario().getImagen());
            foto.setBorderColor(ContextCompat.getColor(this,
                    estado.isVisto() ? R.color.estado_visto : R.color.estado_nuevo));
            ((TextView)tarjeta.findViewById(R.id.tvNombreEstado)).setText(estado.getUsuario().getNombre());
            tarjeta.setContentDescription(getString(estado.isVisto() ? R.string.estado_ya_visto : R.string.estado_sin_ver,
                    estado.getUsuario().getNombre()));
            tarjeta.setOnClickListener(v -> abrirEstado(estado));
            llEstados.addView(tarjeta);
        }
    }

    private void abrirEstado(Estado estado) {
        estado.setVisto(true);
        View detalle = getLayoutInflater().inflate(R.layout.dialog_estado,null);
        ((ImageView)detalle.findViewById(R.id.ivEstadoCompleto)).setImageResource(estado.getFondo());
        ((TextView)detalle.findViewById(R.id.tvTextoEstado)).setText(estado.getTexto());
        new AlertDialog.Builder(this).setTitle(estado.getUsuario().getNombre()).setView(detalle)
                .setPositiveButton(R.string.cerrar,null).show();
        mostrarEstados();
    }

    private void mostrarCanales() {
        llCanales.removeAllViews();
        llSugerencias.removeAllViews();
        int sugerencias = 0, disponibles = 0, seguidos = 0;
        for (Canal canal : datos.getCanales()) {
            if (canal.isSeguido()) {
                seguidos++;
                View fila = getLayoutInflater().inflate(R.layout.item_canal,llCanales,false);
                ((CircleImageView)fila.findViewById(R.id.ivCanal)).setImageResource(canal.getImagen());
                ((TextView)fila.findViewById(R.id.tvTituloCanal)).setText(canal.getTitulo());
                ((TextView)fila.findViewById(R.id.tvMensajeCanal)).setText(canal.getUltimoMensaje());
                ((TextView)fila.findViewById(R.id.tvFechaCanal)).setText(canal.getFecha());
                TextView contador = fila.findViewById(R.id.tvSinLeer);
                contador.setText(String.valueOf(canal.getSinLeer()));
                contador.setContentDescription(getString(R.string.mensajes_sin_leer,canal.getSinLeer()));
                contador.setVisibility(canal.getSinLeer() == 0 ? View.GONE : View.VISIBLE);
                fila.setOnClickListener(v -> {
                    canal.setSinLeer(0);
                    new AlertDialog.Builder(this).setTitle(canal.getTitulo())
                            .setMessage(canal.getUltimoMensaje())
                            .setPositiveButton(R.string.cerrar,null).show();
                    mostrarCanales();
                });
                llCanales.addView(fila);
            } else {
                disponibles++;
                if (!explorarTodos && sugerencias >= 2) continue;
                sugerencias++;
                View fila = getLayoutInflater().inflate(R.layout.item_sugerencia,llSugerencias,false);
                ((CircleImageView)fila.findViewById(R.id.ivCanal)).setImageResource(canal.getImagen());
                ((TextView)fila.findViewById(R.id.tvTituloCanal)).setText(canal.getTitulo());
                ((TextView)fila.findViewById(R.id.tvSeguidores)).setText(canal.getSeguidores());
                View seguir = fila.findViewById(R.id.btnSeguir);
                seguir.setContentDescription(getString(R.string.seguir_canal,canal.getTitulo()));
                seguir.setOnClickListener(v -> { canal.setSeguido(true); mostrarCanales(); });
                llSugerencias.addView(fila);
            }
        }
        findViewById(R.id.tvSinCanales).setVisibility(seguidos == 0 ? View.VISIBLE : View.GONE);
        findViewById(R.id.tvSinSugerencias).setVisibility(disponibles == 0 ? View.VISIBLE : View.GONE);
        findViewById(R.id.btnExplorar).setVisibility(!explorarTodos && disponibles > sugerencias ? View.VISIBLE : View.GONE);
    }

    @Override protected void onSaveInstanceState(Bundle estado) {
        super.onSaveInstanceState(estado);
        estado.putBoolean("explorar",explorarTodos);
    }
}
