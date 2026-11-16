package com.upiiz.dm_eohs_practica_ii;

import android.app.Application;

import com.upiiz.dm_eohs_practica_ii.utils.Preferencias;

public class ExploradorApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Preferencias.aplicarTema(this);
    }
}
