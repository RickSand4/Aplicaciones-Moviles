package com.upiiz.examen_eohs_01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.upiiz.examen_eohs_01.adapters.CustomAdapter;

import java.util.ArrayList;

import model.User;

public class CustomListViewActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //1. Declarar variables
    ListView lvUsuarios;
    ArrayList<User> listaUsuarios;
    CustomAdapter adapter;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_custom_list_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //2. Enlazar variables con las vistas
        lvUsuarios = findViewById(R.id.lvUsuarios);
        toolbar = findViewById(R.id.toolbar);

        //3. Cargar los datos y crear el adaptador
        cargarUsuarios();
        adapter = new CustomAdapter(this, listaUsuarios);
        lvUsuarios.setAdapter(adapter);

        //4. Escuchar los clicks
        lvUsuarios.setOnItemClickListener(this);
        toolbar.setNavigationOnClickListener(this);
    }

    public void cargarUsuarios() {
        //Datos en memoria
        listaUsuarios = new ArrayList<>();
        listaUsuarios.add(new User(1L, "Ana García", "ana123", R.drawable.usuario));
        listaUsuarios.add(new User(2L, "Luis Martínez", "luism", R.drawable.usuario));
        listaUsuarios.add(new User(3L, "Marta López", "marta_22", R.drawable.usuario));
        listaUsuarios.add(new User(4L, "Pedro Sánchez", "pedro99", R.drawable.usuario));
        listaUsuarios.add(new User(5L, "Sofía Torres", "sofia_t", R.drawable.usuario));
        listaUsuarios.add(new User(6L, "Juan Rodríguez", "juanr", R.drawable.usuario));
        listaUsuarios.add(new User(7L, "Laura Hernández", "laura_h", R.drawable.usuario));
        listaUsuarios.add(new User(8L, "Carlos Ramírez", "carlos_r", R.drawable.usuario));
        listaUsuarios.add(new User(9L, "Valeria Flores", "vale_f", R.drawable.usuario));
        listaUsuarios.add(new User(10L, "Diego Gómez", "diego_g", R.drawable.usuario));
        listaUsuarios.add(new User(11L, "Camila Pérez", "camila_p", R.drawable.usuario));
        listaUsuarios.add(new User(12L, "Miguel Díaz", "miguel_d", R.drawable.usuario));
        listaUsuarios.add(new User(13L, "Lucía Romero", "lucia_r", R.drawable.usuario));
        listaUsuarios.add(new User(14L, "Jorge Castro", "jorge_c", R.drawable.usuario));
        listaUsuarios.add(new User(15L, "Elena Vargas", "elena_v", R.drawable.usuario));
        listaUsuarios.add(new User(16L, "Andrés Mendoza", "andres_m", R.drawable.usuario));
        listaUsuarios.add(new User(17L, "Daniela Ruiz", "dani_ruiz", R.drawable.usuario));
        listaUsuarios.add(new User(18L, "Fernando Ortiz", "fer_ortiz", R.drawable.usuario));
        listaUsuarios.add(new User(19L, "Mariana Silva", "mariana_s", R.drawable.usuario));
        listaUsuarios.add(new User(20L, "Ricardo Reyes", "ricardo_r", R.drawable.usuario));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User usuario = listaUsuarios.get(position);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("id_usuario", usuario.getId());
        intent.putExtra("nombre_usuario", usuario.getNombre());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        regresar();
    }

    public void regresar() {
        finish();
    }
}
