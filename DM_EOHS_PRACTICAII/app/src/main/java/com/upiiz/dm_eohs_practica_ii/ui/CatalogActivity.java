package com.upiiz.dm_eohs_practica_ii.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.upiiz.dm_eohs_practica_ii.R;
import com.upiiz.dm_eohs_practica_ii.adapters.LibroAdapter;
import com.upiiz.dm_eohs_practica_ii.models.Libro;
import com.upiiz.dm_eohs_practica_ii.utils.Preferencias;
import com.upiiz.dm_eohs_practica_ii.viewmodel.LibroViewModel;

import java.util.List;

public class CatalogActivity extends AppCompatActivity {
    private LibroViewModel viewModel;
    private LibroAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catalog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Preferencias.guardarUltimaSeccion(this, Preferencias.SECCION_CATALOGO);

        recyclerView = findViewById(R.id.rvLibros);
        progressBar = findViewById(R.id.progressBar);
        emptyState = findViewById(R.id.emptyState);
        MaterialButton btnReintentar = findViewById(R.id.btnReintentar);

        adapter = new LibroAdapter(this::abrirDetalle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(LibroViewModel.class);
        viewModel.getLibros().observe(this, this::mostrarLibros);
        viewModel.getCargando().observe(this, cargando -> {
            progressBar.setVisibility(cargando ? View.VISIBLE : View.GONE);
            if (cargando) {
                emptyState.setVisibility(View.GONE);
            }
        });
        viewModel.getError().observe(this, mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Snackbar.make(findViewById(R.id.main), mensaje, Snackbar.LENGTH_LONG).show();
                if (adapter.getItemCount() == 0) {
                    mostrarLibros(null);
                }
                viewModel.limpiarError();
            }
        });

        btnReintentar.setOnClickListener(v -> viewModel.cargarLibros());
        viewModel.cargarLibros();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_actualizar) {
            viewModel.cargarLibros();
            return true;
        }
        if (item.getItemId() == R.id.action_favoritos) {
            startActivity(new Intent(this, FavoritesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarLibros(List<Libro> libros) {
        adapter.setLibros(libros);
        boolean vacio = libros == null || libros.isEmpty();
        emptyState.setVisibility(vacio ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(vacio ? View.GONE : View.VISIBLE);
    }

    private void abrirDetalle(Libro libro) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_ID, libro.getId());
        startActivity(intent);
    }
}
