package com.upiiz.dm_eohs_08.models;

public class RespuestaCliente {
    private int estado;
    private String mensaje;
    private Cliente datos;

    public int getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Cliente getDatos() {
        return datos;
    }

    public void setDatos(Cliente datos) {
        this.datos = datos;
    }
}
