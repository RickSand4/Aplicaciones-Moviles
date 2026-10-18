package com.upiiz.dm_eohs_07.data.db;

import androidx.room3.Database;
import androidx.room3.RoomDatabase;
import com.upiiz.dm_eohs_07.data.dao.MascotaDAO;
import com.upiiz.dm_eohs_07.data.dao.UsuarioDAO;
import com.upiiz.dm_eohs_07.data.dao.VeterinarioDAO;
import com.upiiz.dm_eohs_07.data.entities.Mascota;
import com.upiiz.dm_eohs_07.data.entities.Usuario;
import com.upiiz.dm_eohs_07.data.entities.Veterinario;

@Database(entities = {Mascota.class, Usuario.class, Veterinario.class}, version = 3, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract MascotaDAO mascotaDAO();
    public abstract UsuarioDAO usuarioDAO();
    public abstract VeterinarioDAO veterinarioDAO();
}
