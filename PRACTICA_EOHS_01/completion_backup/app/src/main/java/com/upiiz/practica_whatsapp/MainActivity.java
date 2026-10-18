package com.upiiz.practica_whatsapp;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.Locale;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.upiiz.practica_whatsapp.adapters.UsuarioAdapter;
import com.upiiz.practica_whatsapp.models.Usuario;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    // 1. Declarar variables
    EditText etBuscar;
    ListView lvUsuarios;

    Button btnTodos, btnNoLeidos, btnFavoritos, btnGrupos;
    Button btnChats, btnLlamadas, btnNovedades;
    ArrayList<Usuario> listaUsuarios;
    ArrayList<Usuario> usuariosFiltrados;
    UsuarioAdapter adapter;
    private String filtroActual = "Todos";
    private static final int LIMITE_BUSQUEDA = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Evitar que el contenido quede debajo de las barras del teléfono
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets barras = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            barras.left,
                            barras.top,
                            barras.right,
                            barras.bottom
                    );

                    return insets;
                }
        );

        // 2. Enlazar variables con las vistas
        etBuscar = findViewById(R.id.etBuscar);
        lvUsuarios = findViewById(R.id.lvUsuarios);

        btnTodos = findViewById(R.id.btnTodos);
        btnNoLeidos = findViewById(R.id.btnNoLeidos);
        btnFavoritos = findViewById(R.id.btnFavoritos);
        btnGrupos = findViewById(R.id.btnGrupos);

        btnChats = findViewById(R.id.btnChats);
        btnLlamadas = findViewById(R.id.btnLlamadas);
        btnNovedades = findViewById(R.id.btnNovedades);

        // 3. Aquí agregaremos los datos, el adapter y las acciones
        // Cargar la lista completa
        cargarUsuarios();

        // Crear otra lista que inicialmente contiene todos los usuarios
        usuariosFiltrados = new ArrayList<>(listaUsuarios);

        // El adapter mostrará la lista filtrada
        adapter = new UsuarioAdapter(this, usuariosFiltrados);

        lvUsuarios.setAdapter(adapter);
        lvUsuarios.setOnItemClickListener(
                (parent, view, position, id) -> {

                    // Obtener el usuario de la fila que se tocó
                    Usuario seleccionado = adapter.getItem(position);

                    // Indicar qué actividad vamos a abrir
                    Intent intent = new Intent(
                            MainActivity.this,
                            ChatActivity.class
                    );

                    // Agregar el nombre como extra
                    intent.putExtra(
                            "nombre_usuario",
                            seleccionado.getNombre()
                    );

                    // Abrir el chat
                    startActivity(intent);
                }
        );
        etBuscar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // No necesitamos realizar acciones aquí
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                filtrarUsuarios(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No necesitamos realizar acciones aquí
            }
        });
        btnTodos.setOnClickListener(this);
        btnNoLeidos.setOnClickListener(this);
        btnFavoritos.setOnClickListener(this);
        btnGrupos.setOnClickListener(this);
        btnLlamadas.setOnClickListener(this);
    }
    private void filtrarUsuarios(String texto) {

        String criterio = texto.trim().toLowerCase(Locale.ROOT);

        usuariosFiltrados.clear();

        for (Usuario usuario : listaUsuarios) {

            String nombre = usuario.getNombre()
                    .toLowerCase(Locale.ROOT);

            // Primera condición: coincidir con el texto
            boolean coincideNombre =
                    criterio.isEmpty() || nombre.contains(criterio);

            // Segunda condición: pertenecer al filtro seleccionado
            boolean coincideFiltro = true;

            if (filtroActual.equals("No leídos")) {
                coincideFiltro = usuario.isNoLeido();

            } else if (filtroActual.equals("Favoritos")) {
                coincideFiltro = usuario.isFavorito();

            } else if (filtroActual.equals("Grupos")) {
                coincideFiltro = usuario.isGrupo();
            }

            // Agregar solo si cumple las dos condiciones
            if (coincideNombre && coincideFiltro) {

                usuariosFiltrados.add(usuario);

                // Limitar únicamente cuando hay texto de búsqueda
                if (!criterio.isEmpty()
                        && usuariosFiltrados.size() >= LIMITE_BUSQUEDA) {
                    break;
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
    private void cargarUsuarios() {

        listaUsuarios = new ArrayList<>();

        listaUsuarios.add(new Usuario(
                1L,
                "Sofía",
                "¿Vas a ir mañana a la facultad?",
                "Últ. conexión: hoy, 10:30",
                R.drawable.user
        ));

        listaUsuarios.add(new Usuario(
                2L,
                "Mateo",
                "Ya terminé la tarea.",
                "Últ. conexión: hoy, 09:45",
                R.drawable.user
        ));

        listaUsuarios.add(new Usuario(
                3L,
                "Ana",
                "Gracias por los apuntes.",
                "Últ. conexión: ayer, 20:15",
                R.drawable.user
        ));

        listaUsuarios.add(new Usuario(
                4L,
                "Luis",
                "Nos vemos en clase.",
                "Últ. conexión: ayer, 18:00",
                R.drawable.user
        ));

        listaUsuarios.add(new Usuario(
                5L,
                "Grupo Proyecto",
                "Diego: ya subí los archivos.",
                "Últ. conexión: ayer, 17:30",
                R.drawable.user
        ));
        // Sofía: no leído y favorito
        listaUsuarios.get(0).setNoLeido(true);
        listaUsuarios.get(0).setFavorito(true);

        // Ana: favorito
        listaUsuarios.get(2).setFavorito(true);

        // Grupo Proyecto: grupo y no leído
        listaUsuarios.get(4).setGrupo(true);
        listaUsuarios.get(4).setNoLeido(true);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.btnLlamadas) {

            Intent intent = new Intent(
                    MainActivity.this,
                    LlamadasActivity.class
            );

            startActivity(intent);
            return;
        }
        if (id == R.id.btnTodos) {
            filtroActual = "Todos";

        } else if (id == R.id.btnNoLeidos) {
            filtroActual = "No leídos";

        } else if (id == R.id.btnFavoritos) {
            filtroActual = "Favoritos";

        } else if (id == R.id.btnGrupos) {
            filtroActual = "Grupos";

        } else {
            return;
        }

        // Aplicar el filtro conservando lo escrito en el buscador
        filtrarUsuarios(etBuscar.getText().toString());
    }
}