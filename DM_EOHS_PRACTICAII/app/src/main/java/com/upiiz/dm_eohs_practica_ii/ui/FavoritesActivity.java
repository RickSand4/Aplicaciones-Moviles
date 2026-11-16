package com.upiiz.dm_eohs_practica_ii.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.upiiz.dm_eohs_practica_ii.R;
import com.upiiz.dm_eohs_practica_ii.adapters.LibroAdapter;
import com.upiiz.dm_eohs_practica_ii.models.Libro;
import com.upiiz.dm_eohs_practica_ii.utils.Preferencias;
import com.upiiz.dm_eohs_practica_ii.viewmodel.LibroViewModel;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private LibroViewModel viewModel;
    private LibroAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorites);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.rvFavoritos);
        emptyState = findViewById(R.id.emptyState);

        adapter = new LibroAdapter(this::abrirDetalle, this::confirmarEliminarFavorito);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnIrCatalogo).setOnClickListener(v -> startActivity(new Intent(this, CatalogActivity.class)));

        viewModel = new ViewModelProvider(this).get(LibroViewModel.class);
        viewModel.getFavoritos().observe(this, this::mostrarFavoritos);
        viewModel.getError().observe(this, mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Snackbar.make(findViewById(R.id.main), mensaje, Snackbar.LENGTH_LONG).show();
                viewModel.limpiarError();
            }
        });

        Preferencias.guardarUltimaSeccion(this, Preferencias.SECCION_FAVORITOS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.cargarFavoritos();
        }
    }

    private void mostrarFavoritos(List<Libro> favoritos) {
        adapter.setLibros(favoritos);
        boolean vacio = favoritos == null || favoritos.isEmpty();
        emptyState.setVisibility(vacio ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(vacio ? View.GONE : View.VISIBLE);
    }

    private void abrirDetalle(Libro libro) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_ID, libro.getId());
        startActivity(intent);
    }

    private void confirmarEliminarFavorito(Libro libro) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.favorites_title)
                .setMessage(getString(R.string.favorites_remove_confirm, libro.getTitulo()))
                .setPositiveButton(R.string.detail_remove_favorite,
                        (dialog, which) -> viewModel.eliminarFavorito(libro.getId()))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
