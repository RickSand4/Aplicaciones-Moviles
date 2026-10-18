package com.upiiz.dm_eohs_07;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room3.Room;
import com.upiiz.dm_eohs_07.data.db.AppDataBase;
import com.upiiz.dm_eohs_07.data.db.Migraciones;
import com.upiiz.dm_eohs_07.data.entities.Mascota;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MascotasActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private AppDataBase db;
    private long usuarioId;
    private List<Mascota> mascotas = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private EditText buscar;
    private TextView vacio;
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        usuarioId = getSharedPreferences("sesion", MODE_PRIVATE).getLong("usuario_id", -1);
        if (usuarioId < 0) { irLogin(); return; }
        setContentView(R.layout.activity_mascotas);
        db = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "veterinaria").addMigrations(Migraciones.VERSION_1_A_2, Migraciones.VERSION_2_A_3).build();
        TextView saludo = findViewById(R.id.saludo);
        saludo.setText("Mis mascotas · " + getSharedPreferences("sesion", MODE_PRIVATE).getString("usuario_nombre", ""));
        ListView lista = findViewById(R.id.listaMascotas);
        buscar = findViewById(R.id.buscar);
        vacio = findViewById(R.id.vacio);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        lista.setAdapter(adapter);
        lista.setOnItemClickListener((parent, view, position, id) -> {
            Mascota mascota = mascotaFiltrada(position);
            if (mascota != null) { Intent intent = new Intent(this, MascotaFormActivity.class); intent.putExtra("mascota_id", mascota.getId()); startActivity(intent); }
        });
        lista.setOnItemLongClickListener((parent, view, position, id) -> {
            Mascota mascota = mascotaFiltrada(position);
            if (mascota != null) new AlertDialog.Builder(this).setTitle("Eliminar mascota")
                    .setMessage("¿Estás seguro de que quieres eliminar a " + mascota.getNombre() + "?")
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("Eliminar", (dialog, which) -> eliminar(mascota)).show();
            return true;
        });
        buscar.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s,int st,int c,int a){} public void onTextChanged(CharSequence s,int st,int before,int count){ pintar(); } public void afterTextChanged(android.text.Editable e){}
        });
        findViewById(R.id.btnAgregar).setOnClickListener(v -> startActivity(new Intent(this, MascotaFormActivity.class)));
        findViewById(R.id.btnSalir).setOnClickListener(v -> { getSharedPreferences("sesion", MODE_PRIVATE).edit().clear().apply(); irLogin(); });
        cargar();
    }
    private Mascota mascotaFiltrada(int position) { List<Mascota> visibles = filtradas(); return position >= 0 && position < visibles.size() ? visibles.get(position) : null; }
    private List<Mascota> filtradas() {
        String q = buscar == null ? "" : buscar.getText().toString().trim().toLowerCase();
        List<Mascota> visibles = new ArrayList<>();
        for (Mascota m : mascotas) if (m.getNombre().toLowerCase().contains(q) || m.getRaza().toLowerCase().contains(q)) visibles.add(m);
        return visibles;
    }
    private void pintar() {
        if (adapter == null) return;
        adapter.clear();
        for (Mascota m : filtradas()) adapter.add(m.getNombre() + "\n" + m.getRaza() + " · " + m.getEdad() + (m.getEdad() == 1 ? " año" : " años"));
        vacio.setText(mascotas.isEmpty() ? "Aún no tienes mascotas. Usa + para agregar la primera." : "No hay coincidencias.");
        vacio.setVisibility(filtradas().isEmpty() ? TextView.VISIBLE : TextView.GONE);
    }
    private void cargar() { executor.execute(() -> { List<Mascota> resultado = db.mascotaDAO().obtenerPorUsuario(usuarioId); runOnUiThread(() -> { mascotas = resultado; pintar(); }); }); }
    private void eliminar(Mascota mascota) { executor.execute(() -> { db.mascotaDAO().eliminar(mascota); runOnUiThread(() -> { Toast.makeText(this, "Mascota eliminada", Toast.LENGTH_SHORT).show(); cargar(); }); }); }
    @Override protected void onResume() { super.onResume(); if (db != null) cargar(); }
    private void irLogin() { startActivity(new Intent(this, MainActivity.class)); finish(); }
    @Override protected void onDestroy() { super.onDestroy(); executor.shutdown(); }
}




