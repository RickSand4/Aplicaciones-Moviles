package com.upiiz.dm_eohs_07.data.dao;

import androidx.room3.Dao;
import androidx.room3.Insert;
import androidx.room3.Query;
import com.upiiz.dm_eohs_07.data.entities.Usuario;

@Dao public interface UsuarioDAO {
    @Query("SELECT * FROM usuarios WHERE correo = :correo COLLATE NOCASE LIMIT 1") Usuario buscarPorCorreo(String correo);
    @Insert long crear(Usuario usuario);
}

