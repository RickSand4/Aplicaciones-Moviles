package com.upiiz.dm_eohs_07.data.dao;

import androidx.room3.Dao;
import androidx.room3.Delete;
import androidx.room3.Insert;
import androidx.room3.Query;
import androidx.room3.Update;
import com.upiiz.dm_eohs_07.data.entities.Veterinario;
import java.util.List;

@Dao
public interface VeterinarioDAO {
    @Query("SELECT * FROM veterinarios ORDER BY cedula COLLATE NOCASE")
    List<Veterinario> obtenerTodos();

    @Query("SELECT * FROM veterinarios WHERE id = :id LIMIT 1")
    Veterinario obtenerPorId(long id);

    @Insert
    long crear(Veterinario veterinario);

    @Update
    int actualizar(Veterinario veterinario);

    @Delete
    int eliminar(Veterinario veterinario);
}
