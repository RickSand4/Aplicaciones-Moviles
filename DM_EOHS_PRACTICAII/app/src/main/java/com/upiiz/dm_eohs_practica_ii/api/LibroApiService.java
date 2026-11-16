package com.upiiz.dm_eohs_practica_ii.api;

import com.upiiz.dm_eohs_practica_ii.models.Libro;
import com.upiiz.dm_eohs_practica_ii.models.LibrosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LibroApiService {
    @GET("api/v1/libros")
    Call<LibrosResponse> obtenerLibros();

    @GET("api/v1/libros/{id}")
    Call<Libro> obtenerLibroPorId(@Path("id") long id);
}
