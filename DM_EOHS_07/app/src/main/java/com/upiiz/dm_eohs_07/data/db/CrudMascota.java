package com.upiiz.dm_eohs_07.data.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.room3.Room;
import com.upiiz.dm_eohs_07.data.entities.Mascota;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrudMascota {
    private final AppDataBase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public CrudMascota(Context context) { db = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "veterinaria").addMigrations(Migraciones.VERSION_1_A_2, Migraciones.VERSION_2_A_3).build(); }
    public void agregar(Mascota mascota, OnIdListener callback) { executor.execute(() -> { long id = db.mascotaDAO().crear(mascota); new Handler(Looper.getMainLooper()).post(() -> callback.onComplete(id)); }); }
    public void actualizar(Mascota mascota, OnBooleanListener callback) { executor.execute(() -> { boolean ok = db.mascotaDAO().actualizar(mascota) > 0; new Handler(Looper.getMainLooper()).post(() -> callback.onComplete(ok)); }); }
    public void eliminar(Mascota mascota, OnBooleanListener callback) { executor.execute(() -> { boolean ok = db.mascotaDAO().eliminar(mascota) > 0; new Handler(Looper.getMainLooper()).post(() -> callback.onComplete(ok)); }); }
    public void obtenerPorUsuario(long usuarioId, OnMascotasLoadedListener callback) { executor.execute(() -> { List<Mascota> resultado = db.mascotaDAO().obtenerPorUsuario(usuarioId); new Handler(Looper.getMainLooper()).post(() -> callback.onComplete(resultado)); }); }
    public void obtenerPorId(long id, long usuarioId, OnMascotaLoadedListener callback) { executor.execute(() -> { Mascota mascota = db.mascotaDAO().obtenerPorId(id, usuarioId); new Handler(Looper.getMainLooper()).post(() -> callback.onComplete(mascota)); }); }
    public interface OnMascotasLoadedListener { void onComplete(List<Mascota> mascotas); }
    public interface OnMascotaLoadedListener { void onComplete(Mascota mascota); }
    public interface OnIdListener { void onComplete(long id); }
    public interface OnBooleanListener { void onComplete(boolean ok); }
}



