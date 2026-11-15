package com.upiiz.dm_eohs_08.models;

public class RespuestaClientes {
    private int estado;
    private String mensaje;
    private Datos datos;

    public int getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Datos getDatos() {
        return datos;
    }

    public void setDatos(Datos datos) {
        this.datos = datos;
    }
}
