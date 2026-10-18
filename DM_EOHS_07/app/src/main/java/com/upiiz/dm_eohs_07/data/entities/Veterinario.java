package com.upiiz.dm_eohs_07.data.entities;

import androidx.room3.Entity;
import androidx.room3.PrimaryKey;

@Entity(tableName = "veterinarios")
public class Veterinario {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int edad;
    private String cedula;

    public Veterinario(long id, int edad, String cedula) {
        this.id = id;
        this.edad = edad;
        this.cedula = cedula;
    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
}
