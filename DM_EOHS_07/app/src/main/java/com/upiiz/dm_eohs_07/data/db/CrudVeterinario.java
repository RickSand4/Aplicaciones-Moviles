package com.upiiz.dm_eohs_07.data.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.room3.Room;
import com.upiiz.dm_eohs_07.data.entities.Veterinario;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Operaciones de veterinarios ejecutadas fuera del hilo de interfaz. */
public class CrudVeterinario {
    private final AppDataBase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public CrudVeterinario(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "veterinaria")
                .addMigrations(Migraciones.VERSION_1_A_2, Migraciones.VERSION_2_A_3)
                .build();
    }

    public void agregar(Veterinario veterinario, OnIdListener callback) {
        executor.execute(() -> {
            long id = db.veterinarioDAO().crear(veterinario);
            mainHandler.post(() -> callback.onComplete(id));
        });
    }

    public void actualizar(Veterinario veterinario, OnBooleanListener callback) {
        executor.execute(() -> {
            boolean actualizado = db.veterinarioDAO().actualizar(veterinario) > 0;
            mainHandler.post(() -> callback.onComplete(actualizado));
        });
    }

    public void eliminar(Veterinario veterinario, OnBooleanListener callback) {
        executor.execute(() -> {
            boolean eliminado = db.veterinarioDAO().eliminar(veterinario) > 0;
            mainHandler.post(() -> callback.onComplete(eliminado));
        });
    }

    public void obtenerTodos(OnVeterinariosLoadedListener callback) {
        executor.execute(() -> {
            List<Veterinario> veterinarios = db.veterinarioDAO().obtenerTodos();
            mainHandler.post(() -> callback.onComplete(veterinarios));
        });
    }

    public void obtenerPorId(long id, OnVeterinarioLoadedListener callback) {
        executor.execute(() -> {
            Veterinario veterinario = db.veterinarioDAO().obtenerPorId(id);
            mainHandler.post(() -> callback.onComplete(veterinario));
        });
    }

    public void cerrar() {
        executor.shutdown();
        db.close();
    }

    public interface OnIdListener { void onComplete(long id); }
    public interface OnBooleanListener { void onComplete(boolean actualizado); }
    public interface OnVeterinariosLoadedListener { void onComplete(List<Veterinario> veterinarios); }
    public interface OnVeterinarioLoadedListener { void onComplete(Veterinario veterinario); }
}
