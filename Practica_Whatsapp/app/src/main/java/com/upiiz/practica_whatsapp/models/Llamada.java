package com.upiiz.practica_whatsapp.models;

public class Llamada {
    private final Usuario usuario;
    private final String fecha;
    private final boolean video;
    public Llamada(Usuario usuario, String fecha, boolean video) {
        this.usuario = usuario; this.fecha = fecha; this.video = video;
    }
    public Usuario getUsuario() { return usuario; }
    public String getFecha() { return fecha; }
    public boolean isVideo() { return video; }
}
