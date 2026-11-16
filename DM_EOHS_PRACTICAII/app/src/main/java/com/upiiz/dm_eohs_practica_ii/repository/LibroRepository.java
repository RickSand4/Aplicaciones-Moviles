package com.upiiz.dm_eohs_practica_ii.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.upiiz.dm_eohs_practica_ii.api.LibroApiService;
import com.upiiz.dm_eohs_practica_ii.api.RetrofitClient;
import com.upiiz.dm_eohs_practica_ii.db.AppDatabase;
import com.upiiz.dm_eohs_practica_ii.db.LibroDao;
import com.upiiz.dm_eohs_practica_ii.models.ErrorResponse;
import com.upiiz.dm_eohs_practica_ii.models.Libro;
import com.upiiz.dm_eohs_practica_ii.models.LibroEntity;
import com.upiiz.dm_eohs_practica_ii.models.LibrosResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibroRepository {
    private final LibroApiService api;
    private final LibroDao libroDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public LibroRepository(Context context) {
        api = RetrofitClient.getLibroApiService();
        libroDao = AppDatabase.getInstance(context).libroDao();
    }

    public void obtenerLibros(OnLibrosListener listener) {
        api.obtenerLibros().enqueue(new Callback<LibrosResponse>() {
            @Override
            public void onResponse(Call<LibrosResponse> call, Response<LibrosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Libro> libros = response.body().getLibros();
                    listener.onComplete(libros != null ? libros : new ArrayList<>(), null);
                } else {
                    listener.onComplete(null, mensajeError(response));
                }
            }

            @Override
            public void onFailure(Call<LibrosResponse> call, Throwable t) {
                listener.onComplete(null, "No se pudo conectar con el servidor: " + t.getMessage());
            }
        });
    }

    public void obtenerLibro(long id, OnLibroListener listener) {
        api.obtenerLibroPorId(id).enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onComplete(response.body(), null);
                } else {
                    listener.onComplete(null, mensajeError(response));
                }
            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                listener.onComplete(null, "No se pudo conectar con el servidor: " + t.getMessage());
            }
        });
    }

    public void obtenerLibroLocal(long id, OnLibroListener listener) {
        executor.execute(() -> {
            LibroEntity entidad = libroDao.obtenerPorId(id);
            Libro libro = entidad != null ? aLibro(entidad) : null;
            mainHandler.post(() -> listener.onComplete(libro, null));
        });
    }

    public void obtenerFavoritos(OnFavoritosListener listener) {
        executor.execute(() -> {
            List<LibroEntity> entidades = libroDao.obtenerTodos();
            List<Libro> favoritos = new ArrayList<>();
            for (LibroEntity entidad : entidades) {
                favoritos.add(aLibro(entidad));
            }
            mainHandler.post(() -> listener.onComplete(favoritos, null));
        });
    }

    public void esFavorito(long id, OnBooleanListener listener) {
        executor.execute(() -> {
            boolean existe = libroDao.contarPorId(id) > 0;
            mainHandler.post(() -> listener.onComplete(existe));
        });
    }

    public void agregarFavorito(Libro libro, OnBooleanListener listener) {
        executor.execute(() -> {
            libroDao.insertar(aEntidad(libro));
            mainHandler.post(() -> listener.onComplete(true));
        });
    }

    public void eliminarFavorito(long id, OnBooleanListener listener) {
        executor.execute(() -> {
            libroDao.eliminarPorId(id);
            mainHandler.post(() -> listener.onComplete(true));
        });
    }

    private LibroEntity aEntidad(Libro libro) {
        return new LibroEntity(
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getEditorial(),
                libro.getPaginas(),
                libro.isDisponible()
        );
    }

    private Libro aLibro(LibroEntity entidad) {
        return new Libro(
                entidad.getId(),
                entidad.getTitulo(),
                entidad.getAutor(),
                entidad.getEditorial(),
                entidad.getPaginas(),
                entidad.isDisponible()
        );
    }

    private String mensajeError(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                ErrorResponse error = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                if (error != null) {
                    if (error.getMensajes() != null && !error.getMensajes().isEmpty()) {
                        return error.getMensajes().get(0);
                    }
                    if (error.getError() != null && !error.getError().isEmpty()) {
                        return error.getError();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return "Error del servidor (" + response.code() + ")";
    }

    public interface OnLibrosListener {
        void onComplete(List<Libro> libros, String error);
    }

    public interface OnLibroListener {
        void onComplete(Libro libro, String error);
    }

    public interface OnFavoritosListener {
        void onComplete(List<Libro> favoritos, String error);
    }

    public interface OnBooleanListener {
        void onComplete(boolean resultado);
    }
}
