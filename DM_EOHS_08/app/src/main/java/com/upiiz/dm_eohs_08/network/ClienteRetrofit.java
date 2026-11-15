package com.upiiz.dm_eohs_08.network;

import com.upiiz.dm_eohs_08.network.api.APICliente;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ClienteRetrofit {

    private static final String BASE_URL = "https://fw-ejercicio-08.onrender.com/";
    private static volatile ClienteRetrofit instancia;
    private final APICliente apiCliente;

    private ClienteRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiCliente = retrofit.create(APICliente.class);
    }

    public static ClienteRetrofit getInstance() {
        if (instancia == null) {
            synchronized (ClienteRetrofit.class) {
                if (instancia == null) {
                    instancia = new ClienteRetrofit();
                }
            }
        }
        return instancia;
    }

    public APICliente getApiCliente() {
        return apiCliente;
    }
}
