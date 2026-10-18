package com.upiiz.dm_eohs_07.data.entities;

import androidx.room3.ColumnInfo;
import androidx.room3.Entity;
import androidx.room3.Index;
import androidx.room3.PrimaryKey;

@Entity(tableName = "mascotas")
public class Mascota {
    @PrimaryKey(autoGenerate = true) private Long id;
    @ColumnInfo(name = "nombre") private String nombre;
    @ColumnInfo(name = "raza") private String raza;
    @ColumnInfo(name = "edad") private int edad;
    @ColumnInfo(name = "usuario_id") private long usuarioId;

    public Mascota(Long id, String nombre, String raza, int edad, long usuarioId) {
        this.id = id; this.nombre = nombre; this.raza = raza; this.edad = edad; this.usuarioId = usuarioId;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(long usuarioId) { this.usuarioId = usuarioId; }
}


