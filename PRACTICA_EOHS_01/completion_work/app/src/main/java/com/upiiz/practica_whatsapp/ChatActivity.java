package com.upiiz.practica_whatsapp;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.upiiz.practica_whatsapp.datos.DatosDemo;
import com.upiiz.practica_whatsapp.models.*;
import com.upiiz.practica_whatsapp.util.Pantalla;

public class ChatActivity extends AppCompatActivity {
    private Usuario usuario;
    private DatosDemo datos;
    private EditText etMensaje;
    private LinearLayout llMensajes;
    private ScrollView svMensajes;

    @Override protected void onCreate(Bundle estado) {
        super.onCreate(estado);
        datos = DatosDemo.obtener();
        usuario = datos.buscarUsuario(getIntent().getLongExtra("id_usuario",-1));
        if (usuario == null) { finish(); return; }
        setContentView(R.layout.activity_chat);
        Pantalla.ajustarBarras(this);
        // El nombre llega por extras; el id identifica la conversación.
        String nombre = getIntent().getStringExtra("nombre_usuario");
        ((TextView)findViewById(R.id.tvNombreChat)).setText(nombre == null ? usuario.getNombre() : nombre);
        ((TextView)findViewById(R.id.tvConexionChat)).setText(usuario.getUltimaConexion());
        etMensaje = findViewById(R.id.etMensaje);
        llMensajes = findViewById(R.id.llMensajes);
        svMensajes = findViewById(R.id.svMensajes);
        if (estado != null) etMensaje.setText(estado.getString("borrador",""));
        findViewById(R.id.btnRegresar).setOnClickListener(v -> finish());
        findViewById(R.id.btnEnviar).setOnClickListener(v -> {
            String texto = etMensaje.getText().toString().trim();
            if (texto.isEmpty()) {
                etMensaje.setError(getString(R.string.mensaje_vacio)); return;
            }
            datos.enviarMensaje(usuario,texto);
            etMensaje.setText("");
            mostrarMensajes();
        });
        usuario.setNoLeido(false);
        mostrarMensajes();
    }

    private void mostrarMensajes() {
        llMensajes.removeAllViews();
        for (Mensaje mensaje : datos.getMensajes(usuario.getId())) {
            // Igual que getView() del adapter: inflar, enlazar y asignar datos.
            View fila = getLayoutInflater().inflate(R.layout.item_mensaje,llMensajes,false);
            LinearLayout contenedor = fila.findViewById(R.id.llBurbuja);
            contenedor.setBackgroundResource(mensaje.isPropio() ? R.drawable.fondo_mensaje_propio : R.drawable.fondo_mensaje);
            LinearLayout.LayoutParams parametros = (LinearLayout.LayoutParams) contenedor.getLayoutParams();
            parametros.gravity = mensaje.isPropio() ? Gravity.END : Gravity.START;
            contenedor.setLayoutParams(parametros);
            ((TextView)fila.findViewById(R.id.tvMensaje)).setText(mensaje.getTexto());
            ((TextView)fila.findViewById(R.id.tvHoraMensaje)).setText(mensaje.getHora());
            llMensajes.addView(fila);
        }
        svMensajes.post(() -> svMensajes.fullScroll(View.FOCUS_DOWN));
    }

    @Override protected void onSaveInstanceState(Bundle estado) {
        super.onSaveInstanceState(estado);
        if (etMensaje != null) estado.putString("borrador",etMensaje.getText().toString());
    }
}
