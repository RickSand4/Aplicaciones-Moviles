package com.upiiz.dm_eohs_08.network.api;

import com.upiiz.dm_eohs_08.models.PeticionCliente;
import com.upiiz.dm_eohs_08.models.RespuestaCliente;
import com.upiiz.dm_eohs_08.models.RespuestaClientes;
import com.upiiz.dm_eohs_08.models.RespuestaEliminarCliente;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APICliente {

    @Headers("Accept: application/json")
    @GET("api/v1/clientes")
    Call<RespuestaClientes> obtenerClientes();

    @Headers("Accept: application/json")
    @GET("api/v1/clientes/{id}")
    Call<RespuestaCliente> obtenerClientePorId(@Path("id") long id);

    @Headers("Accept: application/json")
    @POST("api/v1/clientes")
    Call<RespuestaCliente> crearCliente(@Body PeticionCliente cliente);

    @Headers("Accept: application/json")
    @PUT("api/v1/clientes/{id}")
    Call<RespuestaCliente> actualizarCliente(@Path("id") long id, @Body PeticionCliente clienteActualizado);

    @Headers("Accept: application/json")
    @DELETE("api/v1/clientes/{id}")
    Call<RespuestaEliminarCliente> eliminarCliente(@Path("id") long id);
}
