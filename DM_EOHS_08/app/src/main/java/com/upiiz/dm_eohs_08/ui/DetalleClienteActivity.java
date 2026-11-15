package com.upiiz.dm_eohs_08.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.upiiz.dm_eohs_08.R;
import com.upiiz.dm_eohs_08.models.Cliente;
import com.upiiz.dm_eohs_08.models.Link;
import com.upiiz.dm_eohs_08.models.RespuestaCliente;
import com.upiiz.dm_eohs_08.models.RespuestaEliminarCliente;
import com.upiiz.dm_eohs_08.network.ApiUtils;
import com.upiiz.dm_eohs_08.network.ClienteRetrofit;
import com.upiiz.dm_eohs_08.network.api.APICliente;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleClienteActivity extends AppCompatActivity {

    public static final String EXTRA_ID_CLIENTE = "extra_id_cliente";

    private long idCliente;
    private APICliente apiCliente;
    private Cliente clienteActual;
    private TextView tvIdDetalle;
    private TextView tvNombreDetalle;
    private TextView tvCorreoDetalle;
    private TextView tvTelefonoDetalle;
    private TextView tvDireccionDetalle;
    private TextView tvEstadoDetalle;
    private TextView tvEnlacesDetalle;
    private MaterialButton btnEditar;
    private MaterialButton btnEliminar;
    private ProgressBar progressBar;

    private final ActivityResultLauncher<Intent> editarLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    setResult(RESULT_OK);
                    cargarCliente();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_cliente);
        aplicarInsets();

        idCliente = getIntent().getLongExtra(EXTRA_ID_CLIENTE, -1);
        if (idCliente <= 0) {
            Toast.makeText(this, R.string.error_generico, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationContentDescription(R.string.volver);
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        apiCliente = ClienteRetrofit.getInstance().getApiCliente();

        tvIdDetalle = findViewById(R.id.tvIdDetalle);
        tvNombreDetalle = findViewById(R.id.tvNombreDetalle);
        tvCorreoDetalle = findViewById(R.id.tvCorreoDetalle);
        tvTelefonoDetalle = findViewById(R.id.tvTelefonoDetalle);
        tvDireccionDetalle = findViewById(R.id.tvDireccionDetalle);
        tvEstadoDetalle = findViewById(R.id.tvEstadoDetalle);
        tvEnlacesDetalle = findViewById(R.id.tvEnlacesDetalle);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        progressBar = findViewById(R.id.progressBar);

        btnEditar.setOnClickListener(v -> abrirFormulario());
        btnEliminar.setOnClickListener(v -> confirmarEliminacion());

        cargarCliente();
    }

    private void aplicarInsets() {
        View raiz = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(raiz, (vista, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            vista.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });
    }

    private void cargarCliente() {
        progressBar.setVisibility(View.VISIBLE);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);

        apiCliente.obtenerClientePorId(idCliente).enqueue(new Callback<RespuestaCliente>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaCliente> call,
                                   @NonNull Response<RespuestaCliente> response) {
                progressBar.setVisibility(View.GONE);

                if (!response.isSuccessful()) {
                    mostrarErrorConReintento(ApiUtils.obtenerMensajeError(response));
                    return;
                }

                RespuestaCliente cuerpo = response.body();
                if (cuerpo == null || cuerpo.getDatos() == null) {
                    mostrarErrorConReintento(getString(R.string.error_respuesta_vacia));
                    return;
                }

                clienteActual = cuerpo.getDatos();
                mostrarCliente(clienteActual);
                btnEditar.setEnabled(true);
                btnEliminar.setEnabled(true);
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaCliente> call, @NonNull Throwable error) {
                progressBar.setVisibility(View.GONE);
                mostrarErrorConReintento(ApiUtils.obtenerMensajeFallo(error));
            }
        });
    }

    private void mostrarCliente(Cliente cliente) {
        tvIdDetalle.setText(getString(R.string.cliente_id_formato, cliente.getId()));
        tvNombreDetalle.setText(valorOTexto(cliente.getNombre()));
        tvCorreoDetalle.setText(valorOTexto(cliente.getCorreo()));
        tvTelefonoDetalle.setText(valorOTexto(cliente.getTelefono()));
        tvDireccionDetalle.setText(valorOTexto(cliente.getDireccion()));
        tvEstadoDetalle.setText(cliente.isRegistrado()
                ? R.string.registrado
                : R.string.no_registrado);
        tvEnlacesDetalle.setText(construirEnlaces(cliente.getLinks()));
    }

    private String valorOTexto(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return getString(R.string.valor_no_disponible);
        }
        return valor;
    }

    private String construirEnlaces(List<Link> links) {
        if (links == null || links.isEmpty()) {
            return getString(R.string.sin_enlaces);
        }

        StringBuilder constructor = new StringBuilder();
        for (Link link : links) {
            if (constructor.length() > 0) {
                constructor.append('\n');
            }
            constructor.append(link.getEtiqueta())
                    .append(": ")
                    .append(valorOTexto(link.getHref()));
        }
        return constructor.toString();
    }

    private void abrirFormulario() {
        Intent intent = new Intent(this, FormularioClienteActivity.class);
        intent.putExtra(FormularioClienteActivity.EXTRA_ID_CLIENTE, idCliente);
        editarLauncher.launch(intent);
    }

    private void confirmarEliminacion() {
        if (clienteActual == null) {
            return;
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.confirmar_eliminar_titulo)
                .setMessage(getString(R.string.confirmar_eliminar_mensaje, clienteActual.getNombre()))
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.eliminar, (dialog, which) -> eliminarCliente())
                .show();
    }

    private void eliminarCliente() {
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        apiCliente.eliminarCliente(idCliente).enqueue(new Callback<RespuestaEliminarCliente>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaEliminarCliente> call,
                                   @NonNull Response<RespuestaEliminarCliente> response) {
                progressBar.setVisibility(View.GONE);

                if (!response.isSuccessful()) {
                    btnEditar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                    mostrarError(ApiUtils.obtenerMensajeError(response));
                    return;
                }

                RespuestaEliminarCliente cuerpo = response.body();
                String mensaje = cuerpo != null && cuerpo.getMensaje() != null
                        && !cuerpo.getMensaje().trim().isEmpty()
                        ? cuerpo.getMensaje()
                        : getString(R.string.cliente_eliminado_exito);

                Toast.makeText(DetalleClienteActivity.this, mensaje, Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaEliminarCliente> call, @NonNull Throwable error) {
                progressBar.setVisibility(View.GONE);
                btnEditar.setEnabled(true);
                btnEliminar.setEnabled(true);
                mostrarError(ApiUtils.obtenerMensajeFallo(error));
            }
        });
    }

    private void mostrarError(String mensaje) {
        Snackbar.make(findViewById(R.id.main), mensaje, Snackbar.LENGTH_LONG).show();
    }

    private void mostrarErrorConReintento(String mensaje) {
        Snackbar.make(findViewById(R.id.main), mensaje, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.reintentar, v -> cargarCliente())
                .show();
    }
}
