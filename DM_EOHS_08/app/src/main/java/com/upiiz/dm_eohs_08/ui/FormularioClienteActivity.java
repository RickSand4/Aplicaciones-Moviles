package com.upiiz.dm_eohs_08.ui;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.upiiz.dm_eohs_08.R;
import com.upiiz.dm_eohs_08.models.Cliente;
import com.upiiz.dm_eohs_08.models.PeticionCliente;
import com.upiiz.dm_eohs_08.models.RespuestaCliente;
import com.upiiz.dm_eohs_08.network.ApiUtils;
import com.upiiz.dm_eohs_08.network.ClienteRetrofit;
import com.upiiz.dm_eohs_08.network.api.APICliente;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioClienteActivity extends AppCompatActivity {

    public static final String EXTRA_ID_CLIENTE = "extra_id_cliente";

    private long idCliente = -1;
    private APICliente apiCliente;
    private TextInputLayout tilNombre;
    private TextInputLayout tilCorreo;
    private TextInputLayout tilTelefono;
    private TextInputLayout tilDireccion;
    private TextInputEditText etNombre;
    private TextInputEditText etCorreo;
    private TextInputEditText etTelefono;
    private TextInputEditText etDireccion;
    private MaterialSwitch switchRegistrado;
    private MaterialButton btnGuardar;
    private ProgressBar progressBar;
    private boolean enviando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formulario_cliente);
        aplicarInsets();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationContentDescription(R.string.volver);
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        apiCliente = ClienteRetrofit.getInstance().getApiCliente();

        tilNombre = findViewById(R.id.tilNombre);
        tilCorreo = findViewById(R.id.tilCorreo);
        tilTelefono = findViewById(R.id.tilTelefono);
        tilDireccion = findViewById(R.id.tilDireccion);
        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        etDireccion = findViewById(R.id.etDireccion);
        switchRegistrado = findViewById(R.id.switchRegistrado);
        btnGuardar = findViewById(R.id.btnGuardar);
        progressBar = findViewById(R.id.progressBar);

        idCliente = getIntent().getLongExtra(EXTRA_ID_CLIENTE, -1);
        if (idCliente > 0) {
            toolbar.setTitle(R.string.editar_cliente);
            btnGuardar.setEnabled(false);
            cargarCliente();
        } else {
            toolbar.setTitle(R.string.nuevo_cliente);
            switchRegistrado.setChecked(true);
        }

        btnGuardar.setOnClickListener(v -> validarYGuardar());
    }

    private void aplicarInsets() {
        View raiz = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(raiz, (vista, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets teclado = insets.getInsets(WindowInsetsCompat.Type.ime());
            vista.setPadding(barras.left, barras.top, barras.right,
                    Math.max(barras.bottom, teclado.bottom));
            return insets;
        });
    }

    private void cargarCliente() {
        mostrarCargando(true);
        apiCliente.obtenerClientePorId(idCliente).enqueue(new Callback<RespuestaCliente>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaCliente> call,
                                   @NonNull Response<RespuestaCliente> response) {
                mostrarCargando(false);
                if (!response.isSuccessful()) {
                    mostrarErrorConReintento(ApiUtils.obtenerMensajeError(response));
                    return;
                }

                RespuestaCliente cuerpo = response.body();
                if (cuerpo == null || cuerpo.getDatos() == null) {
                    mostrarErrorConReintento(getString(R.string.error_respuesta_vacia));
                    return;
                }

                llenarFormulario(cuerpo.getDatos());
                btnGuardar.setEnabled(true);
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaCliente> call, @NonNull Throwable error) {
                mostrarCargando(false);
                mostrarErrorConReintento(ApiUtils.obtenerMensajeFallo(error));
            }
        });
    }

    private void llenarFormulario(Cliente cliente) {
        etNombre.setText(cliente.getNombre());
        etCorreo.setText(cliente.getCorreo());
        etTelefono.setText(cliente.getTelefono());
        etDireccion.setText(cliente.getDireccion());
        switchRegistrado.setChecked(cliente.isRegistrado());
    }

    private void validarYGuardar() {
        if (enviando) {
            return;
        }

        limpiarErrores();
        String nombre = textoDe(etNombre);
        String correo = textoDe(etCorreo);
        String telefono = textoDe(etTelefono);
        String direccion = textoDe(etDireccion);
        boolean valido = true;
        View primerError = null;

        if (nombre.isEmpty()) {
            tilNombre.setError(getString(R.string.error_campo_obligatorio));
            primerError = etNombre;
            valido = false;
        }

        if (correo.isEmpty()) {
            tilCorreo.setError(getString(R.string.error_campo_obligatorio));
            if (primerError == null) {
                primerError = etCorreo;
            }
            valido = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            tilCorreo.setError(getString(R.string.error_correo_invalido));
            if (primerError == null) {
                primerError = etCorreo;
            }
            valido = false;
        }

        if (telefono.isEmpty()) {
            tilTelefono.setError(getString(R.string.error_campo_obligatorio));
            if (primerError == null) {
                primerError = etTelefono;
            }
            valido = false;
        } else if (!telefonoValido(telefono)) {
            tilTelefono.setError(getString(R.string.error_telefono_invalido));
            if (primerError == null) {
                primerError = etTelefono;
            }
            valido = false;
        }

        if (direccion.isEmpty()) {
            tilDireccion.setError(getString(R.string.error_campo_obligatorio));
            if (primerError == null) {
                primerError = etDireccion;
            }
            valido = false;
        }

        if (!valido) {
            if (primerError != null) {
                primerError.requestFocus();
            }
            return;
        }

        PeticionCliente peticion = new PeticionCliente(
                nombre, correo, telefono, direccion, switchRegistrado.isChecked());
        enviar(peticion);
    }

    private void enviar(PeticionCliente peticion) {
        enviando = true;
        btnGuardar.setEnabled(false);
        btnGuardar.setText(R.string.guardando);
        progressBar.setVisibility(View.VISIBLE);

        Call<RespuestaCliente> llamada = idCliente > 0
                ? apiCliente.actualizarCliente(idCliente, peticion)
                : apiCliente.crearCliente(peticion);

        llamada.enqueue(new Callback<RespuestaCliente>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaCliente> call,
                                   @NonNull Response<RespuestaCliente> response) {
                progressBar.setVisibility(View.GONE);

                if (!response.isSuccessful()) {
                    restaurarBoton();
                    mostrarError(ApiUtils.obtenerMensajeError(response));
                    return;
                }

                RespuestaCliente cuerpo = response.body();
                String mensaje = cuerpo != null && cuerpo.getMensaje() != null
                        && !cuerpo.getMensaje().trim().isEmpty()
                        ? cuerpo.getMensaje()
                        : getString(idCliente > 0
                        ? R.string.cliente_actualizado_exito
                        : R.string.cliente_registrado_exito);

                Toast.makeText(FormularioClienteActivity.this, mensaje, Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaCliente> call, @NonNull Throwable error) {
                progressBar.setVisibility(View.GONE);
                restaurarBoton();
                mostrarError(ApiUtils.obtenerMensajeFallo(error));
            }
        });
    }

    private void restaurarBoton() {
        enviando = false;
        btnGuardar.setEnabled(true);
        btnGuardar.setText(R.string.guardar);
    }

    private void limpiarErrores() {
        tilNombre.setError(null);
        tilCorreo.setError(null);
        tilTelefono.setError(null);
        tilDireccion.setError(null);
    }

    private String textoDe(TextInputEditText campo) {
        return campo.getText() != null ? campo.getText().toString().trim() : "";
    }

    private boolean telefonoValido(String telefono) {
        if (!telefono.matches("[0-9+()\\-\\s]{7,25}")) {
            return false;
        }
        int digitos = 0;
        for (int i = 0; i < telefono.length(); i++) {
            if (Character.isDigit(telefono.charAt(i))) {
                digitos++;
            }
        }
        return digitos >= 7;
    }

    private void mostrarCargando(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
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
