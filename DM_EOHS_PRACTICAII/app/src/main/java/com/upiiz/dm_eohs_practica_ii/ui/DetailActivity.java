package com.upiiz.dm_eohs_practica_ii.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.upiiz.dm_eohs_practica_ii.R;
import com.upiiz.dm_eohs_practica_ii.models.Libro;
import com.upiiz.dm_eohs_practica_ii.viewmodel.LibroViewModel;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "extra_id";

    private LibroViewModel viewModel;
    private Libro libroActual;
    private long id;

    private TextView tvTitulo;
    private TextView tvId;
    private TextView tvAutor;
    private TextView tvEditorial;
    private TextView tvPaginas;
    private TextView tvDisponible;
    private MaterialButton btnFavorito;
    private ProgressBar progressBar;
    private View scrollContenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        id = getIntent().getLongExtra(EXTRA_ID, -1L);
        if (id < 0) {
            finish();
            return;
        }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        tvId = findViewById(R.id.tvId);
        tvTitulo = findViewById(R.id.tvTitulo);
        tvAutor = findViewById(R.id.tvAutor);
        tvEditorial = findViewById(R.id.tvEditorial);
        tvPaginas = findViewById(R.id.tvPaginas);
        tvDisponible = findViewById(R.id.tvDisponible);
        btnFavorito = findViewById(R.id.btnFavorito);
        progressBar = findViewById(R.id.progressBar);
        scrollContenido = findViewById(R.id.scrollContenido);

        viewModel = new ViewModelProvider(this).get(LibroViewModel.class);
        viewModel.getLibro().observe(this, libro -> {
            if (libro != null) {
                libroActual = libro;
                mostrarLibro(libro);
            }
        });
        viewModel.getCargando().observe(this, cargando -> {
            progressBar.setVisibility(cargando ? View.VISIBLE : View.GONE);
            if (!cargando && libroActual != null) {
                scrollContenido.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getError().observe(this, mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Snackbar.make(findViewById(R.id.main), mensaje, Snackbar.LENGTH_LONG)
                        .setAction(R.string.catalog_retry, v -> viewModel.cargarLibro(id))
                        .show();
                viewModel.limpiarError();
            }
        });
        viewModel.getEsFavorito().observe(this, this::actualizarBotonFavorito);

        btnFavorito.setOnClickListener(v -> {
            Boolean favorito = viewModel.getEsFavorito().getValue();
            if (Boolean.TRUE.equals(favorito)) {
                viewModel.eliminarFavorito(id);
            } else if (libroActual != null) {
                viewModel.agregarFavorito(libroActual);
            }
        });

        viewModel.cargarLibro(id);
        viewModel.verificarFavorito(id);
    }

    private void mostrarLibro(Libro libro) {
        tvId.setText(getString(R.string.book_id, libro.getId()));
        tvTitulo.setText(libro.getTitulo() != null ? libro.getTitulo() : "");
        tvAutor.setText(getString(R.string.book_author, libro.getAutor() != null ? libro.getAutor() : ""));
        tvEditorial.setText(getString(R.string.book_editorial, libro.getEditorial() != null ? libro.getEditorial() : ""));
        tvPaginas.setText(getString(R.string.book_pages, libro.getPaginas()));
        tvDisponible.setText(libro.isDisponible() ? R.string.book_available : R.string.book_unavailable);
        tvDisponible.setTextColor(ContextCompat.getColor(
                this,
                libro.isDisponible() ? R.color.disponible : R.color.no_disponible
        ));
        progressBar.setVisibility(View.GONE);
        scrollContenido.setVisibility(View.VISIBLE);
    }

    private void actualizarBotonFavorito(Boolean favorito) {
        boolean esFavorito = Boolean.TRUE.equals(favorito);
        btnFavorito.setText(esFavorito ? R.string.detail_remove_favorite : R.string.detail_add_favorite);
    }
}
