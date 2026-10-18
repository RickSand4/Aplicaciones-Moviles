package com.upiiz.practica_whatsapp;

import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.upiiz.practica_whatsapp.adapters.UsuarioAdapter;
import com.upiiz.practica_whatsapp.models.Usuario;
import com.upiiz.practica_whatsapp.datos.DatosDemo;
import com.upiiz.practica_whatsapp.util.FiltroUsuarios;
import com.upiiz.practica_whatsapp.util.Pantalla;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etBuscar;
    ListView lvUsuarios;
    TextView tvResultados;
    Button btnTodos, btnNoLeidos, btnFavoritos, btnGrupos;
    Button btnChats, btnLlamadas, btnNovedades;
    ArrayList<Usuario> listaUsuarios;
    ArrayList<Usuario> usuariosFiltrados;
    UsuarioAdapter adapter;
    private String filtroActual = "Todos";

    @Override protected void onCreate(Bundle estado) {
        super.onCreate(estado);
        setContentView(R.layout.activity_main);
        Pantalla.ajustarBarras(this);
        // 1. Enlazar vistas.
        etBuscar = findViewById(R.id.etBuscar);
        lvUsuarios = findViewById(R.id.lvUsuarios);
        tvResultados = findViewById(R.id.tvResultados);
        btnTodos = findViewById(R.id.btnTodos);
        btnNoLeidos = findViewById(R.id.btnNoLeidos);
        btnFavoritos = findViewById(R.id.btnFavoritos);
        btnGrupos = findViewById(R.id.btnGrupos);
        btnChats = findViewById(R.id.btnChats);
        btnLlamadas = findViewById(R.id.btnLlamadas);
        btnNovedades = findViewById(R.id.btnNovedades);
        // 2. Conectar datos y adapter.
        listaUsuarios = DatosDemo.obtener().getUsuarios();
        usuariosFiltrados = new ArrayList<>();
        adapter = new UsuarioAdapter(this,usuariosFiltrados);
        lvUsuarios.setAdapter(adapter);
        lvUsuarios.setEmptyView(findViewById(R.id.tvSinUsuarios));
        if (estado != null) {
            filtroActual = estado.getString("filtro","Todos");
            etBuscar.setText(estado.getString("busqueda",""));
        }
        // 3. Escuchar las acciones.
        lvUsuarios.setOnItemClickListener((parent,view,position,id) -> {
            Usuario seleccionado = adapter.getItem(position);
            Intent intent = new Intent(this,ChatActivity.class);
            intent.putExtra("nombre_usuario",seleccionado.getNombre());
            intent.putExtra("id_usuario",seleccionado.getId());
            startActivity(intent);
        });
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s,int start,int count,int after) { }
            @Override public void onTextChanged(CharSequence s,int start,int before,int count) {
                filtrarUsuarios(s.toString());
            }
            @Override public void afterTextChanged(Editable s) { }
        });
        btnTodos.setOnClickListener(this);
        btnNoLeidos.setOnClickListener(this);
        btnFavoritos.setOnClickListener(this);
        btnGrupos.setOnClickListener(this);
        btnChats.setOnClickListener(this);
        btnLlamadas.setOnClickListener(this);
        btnNovedades.setOnClickListener(this);
        filtrarUsuarios(etBuscar.getText().toString());
    }

    @Override protected void onResume() {
        super.onResume();
        // Actualizar favoritos, mensajes y lecturas al volver de otra pantalla.
        if (adapter != null) filtrarUsuarios(etBuscar.getText().toString());
    }

    @Override protected void onSaveInstanceState(Bundle estado) {
        super.onSaveInstanceState(estado);
        estado.putString("filtro",filtroActual);
        estado.putString("busqueda",etBuscar.getText().toString());
    }

    private void filtrarUsuarios(String texto) {
        usuariosFiltrados.clear();
        usuariosFiltrados.addAll(FiltroUsuarios.filtrar(listaUsuarios,texto,filtroActual));
        adapter.notifyDataSetChanged();
        tvResultados.setText(texto.trim().isEmpty()
                ? getString(R.string.resultados,usuariosFiltrados.size())
                : getString(R.string.resultados_busqueda,usuariosFiltrados.size(),FiltroUsuarios.LIMITE_BUSQUEDA));
        // El botón desactivado identifica la categoría actual.
        btnTodos.setEnabled(!filtroActual.equals("Todos"));
        btnNoLeidos.setEnabled(!filtroActual.equals("No leídos"));
        btnFavoritos.setEnabled(!filtroActual.equals("Favoritos"));
        btnGrupos.setEnabled(!filtroActual.equals("Grupos"));
    }

    @Override public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnLlamadas) {
            startActivity(new Intent(this,LlamadasActivity.class)); return;
        }
        if (id == R.id.btnNovedades) {
            startActivity(new Intent(this,NovedadesActivity.class)); return;
        }
        if (id == R.id.btnTodos || id == R.id.btnChats) filtroActual = "Todos";
        else if (id == R.id.btnNoLeidos) filtroActual = "No leídos";
        else if (id == R.id.btnFavoritos) filtroActual = "Favoritos";
        else if (id == R.id.btnGrupos) filtroActual = "Grupos";
        filtrarUsuarios(etBuscar.getText().toString());
    }
}
