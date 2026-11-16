package com.upiiz.dm_eohs_practica_ii.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.upiiz.dm_eohs_practica_ii.models.Libro;
import com.upiiz.dm_eohs_practica_ii.repository.LibroRepository;

import java.util.List;

public class LibroViewModel extends AndroidViewModel {
    private final LibroRepository repository;

    private final MutableLiveData<List<Libro>> libros = new MutableLiveData<>();
    private final MutableLiveData<Libro> libro = new MutableLiveData<>();
    private final MutableLiveData<List<Libro>> favoritos = new MutableLiveData<>();
    private final MutableLiveData<Boolean> cargando = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> esFavorito = new MutableLiveData<>(false);

    public LibroViewModel(@NonNull Application application) {
        super(application);
        repository = new LibroRepository(application);
    }

    public LiveData<List<Libro>> getLibros() {
        return libros;
    }

    public LiveData<Libro> getLibro() {
        return libro;
    }

    public LiveData<List<Libro>> getFavoritos() {
        return favoritos;
    }

    public LiveData<Boolean> getCargando() {
        return cargando;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getEsFavorito() {
        return esFavorito;
    }

    public void cargarLibros() {
        cargando.setValue(true);
        error.setValue(null);
        repository.obtenerLibros((lista, mensaje) -> {
            cargando.setValue(false);
            if (mensaje != null) {
                error.setValue(mensaje);
            } else {
                libros.setValue(lista);
            }
        });
    }

    public void cargarLibro(long id) {
        repository.obtenerLibroLocal(id, (local, ignored) -> {
            if (local != null && libro.getValue() == null) {
                libro.setValue(local);
            }
        });
        cargando.setValue(true);
        error.setValue(null);
        repository.obtenerLibro(id, (resultado, mensaje) -> {
            cargando.setValue(false);
            if (mensaje != null) {
                error.setValue(mensaje);
            } else {
                libro.setValue(resultado);
            }
        });
    }

    public void cargarFavoritos() {
        repository.obtenerFavoritos((lista, mensaje) -> {
            if (mensaje != null) {
                error.setValue(mensaje);
            } else {
                favoritos.setValue(lista);
            }
        });
    }

    public void verificarFavorito(long id) {
        repository.esFavorito(id, resultado -> esFavorito.setValue(Boolean.valueOf(resultado)));
    }

    public void agregarFavorito(Libro libro) {
        repository.agregarFavorito(libro, resultado -> {
            esFavorito.setValue(resultado);
            cargarFavoritos();
        });
    }

    public void eliminarFavorito(long id) {
        repository.eliminarFavorito(id, resultado -> {
            esFavorito.setValue(!resultado);
            cargarFavoritos();
        });
    }

    public void limpiarError() {
        error.setValue(null);
    }
}
