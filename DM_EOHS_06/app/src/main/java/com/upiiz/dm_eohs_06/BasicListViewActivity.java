package com.upiiz.dm_eohs_06;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class BasicListViewActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables
    Button btnRegresar;
    ListView lvBasico;
    ArrayList<String> listadoproductos;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //enlazar
        btnRegresar=findViewById(R.id.btnRegresar);
        lvBasico=findViewById(R.id.lvBasico);
        //3. acciones
        btnRegresar.setOnClickListener(this);
        cargarDatos();
        //Creamos el adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listadoproductos);
        //Se lo ingresamos a list view
        lvBasico.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        regresar();
    }
    public void regresar(){
        Intent intentRegresar =
                new Intent(this, MainActivity.class);
        startActivity(intentRegresar);
    }
    public void cargarDatos(){
        //Memoria - Array
        listadoproductos = new ArrayList<>();
        // Base de datos - sqlite - room
        //API REST
        //puertos y adaptadores
        //servicio en la nube - realtime
        listadoproductos.add("Fabuloso");
        listadoproductos.add("Cloro");
        listadoproductos.add("Pinol");
        listadoproductos.add("Jabón zote");
        listadoproductos.add("Maestro Limpio");
        listadoproductos.add("Pato Purific");
        listadoproductos.add("AJAX");
        listadoproductos.add("Mr. Musculo");
        listadoproductos.add("Vel Rosita");
        listadoproductos.add("Vinagre de Manzana");
        listadoproductos.add("Salvo");
        listadoproductos.add("Jabon Foca");
        listadoproductos.add("Jabon Blanca Nieves");
        listadoproductos.add("Downy");
        listadoproductos.add("Vanish");
        listadoproductos.add("Escoba");
        listadoproductos.add("Trapeador");





    }
}