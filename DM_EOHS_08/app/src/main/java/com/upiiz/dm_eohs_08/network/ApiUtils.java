package com.upiiz.dm_eohs_08.network;

import com.google.gson.Gson;
import com.upiiz.dm_eohs_08.models.RespuestaEliminarCliente;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;

public final class ApiUtils {

    private static final Gson GSON = new Gson();

    private ApiUtils() {
    }

    public static String obtenerMensajeError(Response<?> response) {
        if (response == null) {
            return "No se recibió respuesta del servidor.";
        }

        String mensaje = null;
        if (response.errorBody() != null) {
            try {
                String json = response.errorBody().string();
                if (json != null && !json.trim().isEmpty()) {
                    RespuestaEliminarCliente error = GSON.fromJson(json, RespuestaEliminarCliente.class);
                    if (error != null && error.getMensaje() != null && !error.getMensaje().trim().isEmpty()) {
                        mensaje = error.getMensaje();
                    }
                }
            } catch (Exception ignored) {
                // El cuerpo del error no es JSON válido.
            }
        }

        if (mensaje != null) {
            return mensaje;
        }

        switch (response.code()) {
            case 400:
                return "Los datos enviados no son válidos.";
            case 404:
                return "El cliente solicitado no existe.";
            case 409:
                return "Ya existe un cliente con ese correo o hay datos duplicados.";
            case 500:
            case 502:
            case 503:
            case 504:
                return "El servidor no está disponible en este momento. Intenta de nuevo.";
            default:
                return "El servidor respondió con el código HTTP " + response.code() + ".";
        }
    }

    public static String obtenerMensajeFallo(Throwable error) {
        if (error instanceof SocketTimeoutException) {
            return "El servidor tardó demasiado en responder. Si estaba en reposo, puede tardar en despertar. Intenta de nuevo.";
        }
        if (error instanceof UnknownHostException) {
            return "No hay conexión a Internet o no se pudo encontrar el servidor.";
        }
        if (error instanceof ConnectException) {
            return "No se pudo conectar con el servidor. Verifica tu conexión a Internet.";
        }
        String detalle = error != null ? error.getMessage() : null;
        return "Error de red" + (detalle != null && !detalle.isEmpty() ? ": " + detalle : ".");
    }
}
