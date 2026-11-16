package com.upiiz.dm_eohs_practica_ii.db;

import androidx.room3.Dao;
import androidx.room3.Insert;
import androidx.room3.OnConflictStrategy;
import androidx.room3.Query;
import androidx.room3.Update;

import com.upiiz.dm_eohs_practica_ii.models.LibroEntity;

import java.util.List;

@Dao
public interface LibroDao {
    @Query("SELECT * FROM libros_favoritos ORDER BY titulo COLLATE NOCASE")
    List<LibroEntity> obtenerTodos();

    @Query("SELECT * FROM libros_favoritos WHERE id = :id LIMIT 1")
    LibroEntity obtenerPorId(long id);

    @Query("SELECT COUNT(*) FROM libros_favoritos WHERE id = :id")
    int contarPorId(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(LibroEntity libro);

    @Update
    int actualizar(LibroEntity libro);

    @Query("DELETE FROM libros_favoritos WHERE id = :id")
    void eliminarPorId(long id);
}
