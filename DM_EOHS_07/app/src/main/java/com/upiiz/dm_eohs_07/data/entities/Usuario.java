package com.upiiz.dm_eohs_07.data.entities;

import androidx.room3.ColumnInfo;
import androidx.room3.Entity;
import androidx.room3.Index;
import androidx.room3.PrimaryKey;

@Entity(tableName = "usuarios", indices = {@Index(value = {"correo"}, unique = true)})
public class Usuario {
    @PrimaryKey(autoGenerate = true) private long id;
    @ColumnInfo(name = "nombre") private String nombre;
    @ColumnInfo(name = "correo") private String correo;
    @ColumnInfo(name = "password") private String password;

    public Usuario(String nombre, String correo, String password) {
        this.nombre = nombre; this.correo = correo; this.password = password;
    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
}

