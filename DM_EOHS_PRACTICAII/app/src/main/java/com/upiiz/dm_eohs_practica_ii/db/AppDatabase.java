package com.upiiz.dm_eohs_practica_ii.db;

import android.content.Context;

import androidx.room3.Database;
import androidx.room3.Room;
import androidx.room3.RoomDatabase;

import com.upiiz.dm_eohs_practica_ii.models.LibroEntity;

@Database(entities = {LibroEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instancia;

    public abstract LibroDao libroDao();

    public static AppDatabase getInstance(Context context) {
        if (instancia == null) {
            synchronized (AppDatabase.class) {
                if (instancia == null) {
                    instancia = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "explorador_libros.db"
                    ).build();
                }
            }
        }
        return instancia;
    }
}
