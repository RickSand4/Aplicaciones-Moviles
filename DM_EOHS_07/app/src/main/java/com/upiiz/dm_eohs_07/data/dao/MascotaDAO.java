package com.upiiz.dm_eohs_07.data.dao;

import androidx.room3.Dao;
import androidx.room3.Delete;
import androidx.room3.Insert;
import androidx.room3.Query;
import androidx.room3.Update;
import com.upiiz.dm_eohs_07.data.entities.Mascota;
import java.util.List;

@Dao public interface MascotaDAO {
    @Query("SELECT * FROM mascotas WHERE usuario_id = :usuarioId ORDER BY nombre COLLATE NOCASE") List<Mascota> obtenerPorUsuario(long usuarioId);
    @Query("SELECT * FROM mascotas WHERE id = :id AND usuario_id = :usuarioId LIMIT 1") Mascota obtenerPorId(long id, long usuarioId);
    @Query("SELECT COUNT(*) FROM mascotas WHERE usuario_id = :usuarioId AND nombre = :nombre COLLATE NOCASE AND id != :excluirId") int contarNombre(long usuarioId, String nombre, long excluirId);
    @Insert long crear(Mascota mascota);
    @Update int actualizar(Mascota mascota);
    @Delete int eliminar(Mascota mascota);
}

