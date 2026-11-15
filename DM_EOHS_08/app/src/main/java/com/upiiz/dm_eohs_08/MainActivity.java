package com.upiiz.dm_eohs_08;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.upiiz.dm_eohs_08.adapter.ClienteAdapter;
import com.upiiz.dm_eohs_08.models.Cliente;
import com.upiiz.dm_eohs_08.models.RespuestaClientes;
import com.upiiz.dm_eohs_08.models.RespuestaEliminarCliente;
import com.upiiz.dm_eohs_08.network.ApiUtils;
import com.upiiz.dm_eohs_08.network.ClienteRetrofit;
import com.upiiz.dm_eohs_08.network.api.APICliente;
import com.upiiz.dm_eohs_08.ui.DetalleClienteActivity;
import com.upiiz.dm_eohs_08.ui.FormularioClienteActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private APICliente apiCliente;
    private ClienteAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView textoVacio;
    private boolean cargando = false;
    private boolean errorCarga = false;

    private final ActivityResultLauncher<Intent> formularioLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    cargarClientes();
                }
            });

    private final ActivityResultLauncher<Intent> detalleLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    cargarClientes();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        aplicarInsets();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiCliente = ClienteRetrofit.getInstance().getApiCliente();

        swipeRefresh = findViewById(R.id.swipeRefresh);
        progressBar = findViewById(R.id.progressBar);
        textoVacio = findViewById(R.id.textoVacio);
        RecyclerView recyclerClientes = findViewById(R.id.recyclerClientes);
        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregar);

        adapter = new ClienteAdapter(new ClienteAdapter.OnClienteAccionListener() {
            @Override
            public void onVerCliente(Cliente cliente) {
                abrirDetalle(cliente);
            }

            @Override
            public void onEditarCliente(Cliente cliente) {
                abrirFormulario(cliente);
            }

            @Override
            public void onEliminarCliente(Cliente cliente) {
                confirmarEliminacion(cliente);
            }
        });

        recyclerClientes.setLayoutManager(new LinearLayoutManager(this));
        recyclerClientes.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::cargarClientes);
        fabAgregar.setOnClickListener(v -> formularioLauncher.launch(
                new Intent(this, FormularioClienteActivity.class)));

        cargarClientes();
    }

    private void aplicarInsets() {
        View raiz = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(raiz, (vista, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            vista.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.accion_actualizar) {
            swipeRefresh.setRefreshing(true);
            cargarClientes();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirDetalle(Cliente cliente) {
        Intent intent = new Intent(this, DetalleClienteActivity.class);
        intent.putExtra(DetalleClienteActivity.EXTRA_ID_CLIENTE, cliente.getId());
        detalleLauncher.launch(intent);
    }

    private void abrirFormulario(Cliente cliente) {
        Intent intent = new Intent(this, FormularioClienteActivity.class);
        intent.putExtra(FormularioClienteActivity.EXTRA_ID_CLIENTE, cliente.getId());
        formularioLauncher.launch(intent);
    }

    private void cargarClientes() {
        if (cargando) {
            return;
        }
        cargando = true;
        textoVacio.setVisibility(View.GONE);
        if (adapter.getItemCount() == 0 && !swipeRefresh.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }

        apiCliente.obtenerClientes().enqueue(new Callback<RespuestaClientes>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaClientes> call,
                                   @NonNull Response<RespuestaClientes> response) {
                cargando = false;
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);

                if (!response.isSuccessful()) {
                    errorCarga = true;
                    mostrarError(ApiUtils.obtenerMensajeError(response));
                    actualizarEstadoVacio();
                    return;
                }

                RespuestaClientes cuerpo = response.body();
                if (cuerpo == null || cuerpo.getDatos() == null) {
                    errorCarga = true;
                    mostrarError(getString(R.string.error_respuesta_vacia));
                    actualizarEstadoVacio();
                    return;
                }

                errorCarga = false;
                List<Cliente> clientes = cuerpo.getDatos().getClientes();
                adapter.submitList(new ArrayList<>(clientes), MainActivity.this::actualizarEstadoVacio);
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaClientes> call, @NonNull Throwable error) {
                cargando = false;
                errorCarga = true;
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                Log.e(TAG, "Error de red al cargar clientes", error);
                mostrarErrorConReintento(ApiUtils.obtenerMensajeFallo(error));
                actualizarEstadoVacio();
            }
        });
    }

    private void actualizarEstadoVacio() {
        if (adapter.getItemCount() > 0) {
            textoVacio.setVisibility(View.GONE);
            return;
        }
        textoVacio.setText(errorCarga ? R.string.lista_error : R.string.lista_vacia);
        textoVacio.setVisibility(View.VISIBLE);
    }

    private void confirmarEliminacion(Cliente cliente) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.confirmar_eliminar_titulo)
                .setMessage(getString(R.string.confirmar_eliminar_mensaje, cliente.getNombre()))
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.eliminar, (dialog, which) -> eliminarCliente(cliente))
                .show();
    }

    private void eliminarCliente(Cliente cliente) {
        apiCliente.eliminarCliente(cliente.getId()).enqueue(new Callback<RespuestaEliminarCliente>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaEliminarCliente> call,
                                   @NonNull Response<RespuestaEliminarCliente> response) {
                if (!response.isSuccessful()) {
                    mostrarError(ApiUtils.obtenerMensajeError(response));
                    return;
                }

                RespuestaEliminarCliente cuerpo = response.body();
                String mensaje = cuerpo != null && cuerpo.getMensaje() != null
                        && !cuerpo.getMensaje().trim().isEmpty()
                        ? cuerpo.getMensaje()
                        : getString(R.string.cliente_eliminado_exito);
                Snackbar.make(findViewById(R.id.main), mensaje, Snackbar.LENGTH_LONG).show();

                swipeRefresh.setRefreshing(true);
                cargarClientes();
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaEliminarCliente> call, @NonNull Throwable error) {
                mostrarErrorConReintento(ApiUtils.obtenerMensajeFallo(error));
            }
        });
    }

    private void mostrarError(String mensaje) {
        Snackbar.make(findViewById(R.id.main), mensaje, Snackbar.LENGTH_LONG).show();
    }

    private void mostrarErrorConReintento(String mensaje) {
        Snackbar.make(findViewById(R.id.main), mensaje, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.reintentar, v -> {
                    swipeRefresh.setRefreshing(true);
                    cargarClientes();
                })
                .show();
    }
}
