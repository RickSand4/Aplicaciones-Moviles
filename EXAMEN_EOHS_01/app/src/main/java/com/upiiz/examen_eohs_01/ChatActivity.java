package com.upiiz.examen_eohs_01;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.upiiz.examen_eohs_01.adapters.MensajeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import model.Mensaje;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    //1. Declarar variables
    ListView lvMensajes;
    ArrayList<Mensaje> listaMensajes;
    MensajeAdapter adapter;
    EditText etMensaje;
    Button btnEnviar;
    MaterialToolbar toolbar;
    long idUsuario;
    String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //2. Enlazar variables con las vistas
        lvMensajes = findViewById(R.id.lvMensajes);
        etMensaje = findViewById(R.id.etMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        toolbar = findViewById(R.id.toolbar);

        //3. Recibir el usuario seleccionado
        idUsuario = getIntent().getLongExtra("id_usuario", -1);
        nombreUsuario = getIntent().getStringExtra("nombre_usuario");
        if (idUsuario < 1 || nombreUsuario == null) {
            finish();
            return;
        }
        toolbar.setTitle(nombreUsuario);

        //4. Cargar los mensajes y crear el adaptador
        cargarMensajes();
        if (savedInstanceState != null) {
            recuperarMensajes(savedInstanceState);
        }
        adapter = new MensajeAdapter(this, listaMensajes);
        lvMensajes.setAdapter(adapter);

        //5. Escuchar los clicks
        btnEnviar.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(this);
        etMensaje.setOnEditorActionListener(this);
    }

    public void cargarMensajes() {
        //20 mensajes en una lista: true = enviado, false = recibido.
        listaMensajes = new ArrayList<>();
        listaMensajes.add(new Mensaje("Hola " + nombreUsuario + ", ¿cómo estás?", "09:32", true));
        listaMensajes.add(new Mensaje("¡Hola! Muy bien, ¿y tú?", "09:33", false));
        listaMensajes.add(new Mensaje("Todo bien, ¿quieres vernos más tarde?", "09:34", true));
        listaMensajes.add(new Mensaje("Sí, me parece bien. ¿A qué hora?", "09:35", false));
        listaMensajes.add(new Mensaje("¿Te parece a las 5?", "09:36", true));
        listaMensajes.add(new Mensaje("Perfecto, nos vemos entonces.", "09:37", false));
        listaMensajes.add(new Mensaje("¿Nos encontramos en la biblioteca?", "09:38", true));
        listaMensajes.add(new Mensaje("Sí, en la entrada principal.", "09:39", false));
        listaMensajes.add(new Mensaje("Llevaré mis apuntes de aplicaciones móviles.", "09:40", true));
        listaMensajes.add(new Mensaje("¡Genial! Yo llevo mi computadora.", "09:41", false));
        listaMensajes.add(new Mensaje("Podemos repasar los ejercicios juntos.", "09:42", true));
        listaMensajes.add(new Mensaje("Me gustaría practicar las listas personalizadas.", "09:43", false));
        listaMensajes.add(new Mensaje("También revisamos la validación del login.", "09:44", true));
        listaMensajes.add(new Mensaje("Buena idea, tengo unas dudas del registro.", "09:45", false));
        listaMensajes.add(new Mensaje("Las resolvemos paso a paso.", "09:46", true));
        listaMensajes.add(new Mensaje("Gracias, así terminamos el repaso.", "09:47", false));
        listaMensajes.add(new Mensaje("¿Llevamos algo de tomar?", "09:48", true));
        listaMensajes.add(new Mensaje("Yo llevo agua para los dos.", "09:49", false));
        listaMensajes.add(new Mensaje("¡Perfecto! Te aviso cuando llegue.", "09:50", true));
        listaMensajes.add(new Mensaje("De acuerdo, ¡nos vemos al rato!", "09:51", false));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnEnviar) {
            enviarMensaje();
        } else {
            regresar();
        }
    }

    public void enviarMensaje() {
        String texto = etMensaje.getText().toString().trim();
        if (texto.isEmpty()) {
            return;
        }

        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String hora = formatoHora.format(new Date());
        listaMensajes.add(new Mensaje(texto, hora, true));
        adapter.notifyDataSetChanged();
        etMensaje.setText("");
        lvMensajes.setSelection(listaMensajes.size() - 1);
    }

    public void regresar() {
        finish();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            enviarMensaje();
            return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Guardar los mensajes nuevos cuando se gira la pantalla.
        if (listaMensajes != null) {
            ArrayList<String> textos = new ArrayList<>();
            ArrayList<String> horas = new ArrayList<>();
            for (int i = 20; i < listaMensajes.size(); i++) {
                textos.add(listaMensajes.get(i).getTexto());
                horas.add(listaMensajes.get(i).getHora());
            }
            outState.putStringArrayList("textos_enviados", textos);
            outState.putStringArrayList("horas_enviadas", horas);
        }
        super.onSaveInstanceState(outState);
    }

    public void recuperarMensajes(Bundle savedInstanceState) {
        ArrayList<String> textos = savedInstanceState.getStringArrayList("textos_enviados");
        ArrayList<String> horas = savedInstanceState.getStringArrayList("horas_enviadas");
        if (textos != null && horas != null && textos.size() == horas.size()) {
            for (int i = 0; i < textos.size(); i++) {
                listaMensajes.add(new Mensaje(textos.get(i), horas.get(i), true));
            }
        }
    }
}
