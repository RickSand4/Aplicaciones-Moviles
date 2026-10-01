package com.upiiz.dm_eohs_06;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.upiiz.dm_eohs_06.adapters.CustomAdapter;

import java.util.ArrayList;

import model.User;

public class CustomListViewActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    //Variables
    ListView lvUsuarios;
    ArrayList<User> listaUsuarios;
    CustomAdapter adapter;
    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_custom_list_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Enlazar
        lvUsuarios = findViewById(R.id.lvCustom);
        btnRegresar = findViewById(R.id.btnRegresar);
        cargarUsuarios();
        adapter = new CustomAdapter(this, listaUsuarios);
        lvUsuarios.setAdapter(adapter);
        //Acciones
        btnRegresar.setOnClickListener(this);
        lvUsuarios.setOnItemClickListener(this);
        lvUsuarios.setOnItemLongClickListener(this);
    }

    private void cargarUsuarios() {
        //Memoria, DATABASE, API, ETC.
        listaUsuarios = new ArrayList<>();
        listaUsuarios.add(new User(1L, "Sofía", "JAJA el pitillo \uD83D\uDD25", "10p.m.", R.drawable.user));
        listaUsuarios.add(new User(2L, "Mateo", "¿Vas a ir mañana a la facultad?", "9:45p.m.", R.drawable.user));
        listaUsuarios.add(new User(3L, "Ana", "¡Gracias por los apuntes de la clase! 😊", "8:30p.m.", R.drawable.user));
        listaUsuarios.add(new User(4L, "Luis", "No te olvides de subir los cambios al repo de GitHub.", "7:15p.m.", R.drawable.user));
        listaUsuarios.add(new User(5L, "Mamá", "Avísame cuando salgas para cenar.", "6:00p.m.", R.drawable.user));
        listaUsuarios.add(new User(6L, "Grupo Proyecto", "Diego: Yo me encargo de hacer el backend.", "5:20p.m.", R.drawable.user));
        listaUsuarios.add(new User(7L, "Valeria", "Jajaja sí, estuvo buenísimo.", "4:10p.m.", R.drawable.user));
        listaUsuarios.add(new User(8L, "Jorge", "¿Jugamos unas partidas al rato? 🎮", "Ayer", R.drawable.user));
        listaUsuarios.add(new User(9L, "Camila", "Ya te mandé el PDF al correo, revísalo.", "Ayer", R.drawable.user));
        listaUsuarios.add(new User(10L, "Profe Martínez", "Revisen la plataforma, ya subí la rúbrica.", "Ayer", R.drawable.user));
        listaUsuarios.add(new User(11L, "Fernando", "Bro, ¿tienes el contacto del chavo que repara laptops?", "Lunes", R.drawable.user));
        listaUsuarios.add(new User(12L, "Andrea", "¡Mucho éxito en tu presentación! ✨", "Domingo", R.drawable.user));
        listaUsuarios.add(new User(13L, "Roberto", "Confirmado para el viernes a las 8.", "Domingo", R.drawable.user));
        listaUsuarios.add(new User(14L, "Laura", "¿Viste la nueva serie hentai que salió?", "24/09/26", R.drawable.user));
        listaUsuarios.add(new User(15L, "Tío Juan", "Saludos a la familia, un abrazo.", "23/09/26", R.drawable.user));

    }

    @Override
    public void onClick(View v) {
        //Regresar a la MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Le puchaste", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Le dejaste puchao", Toast.LENGTH_SHORT).show();
        return false;
    }
}