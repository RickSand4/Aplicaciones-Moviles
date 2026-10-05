package com.upiiz.practica_whatsapp.models;

public class Estado {
    private final Usuario usuario;
    private final int fondo;
    private final String texto;
    private boolean visto;
    public Estado(Usuario usuario, int fondo, String texto, boolean visto) {
        this.usuario = usuario; this.fondo = fondo; this.texto = texto; this.visto = visto;
    }
    public Usuario getUsuario() { return usuario; }
    public int getFondo() { return fondo; }
    public String getTexto() { return texto; }
    public boolean isVisto() { return visto; }
    public void setVisto(boolean visto) { this.visto = visto; }
}
