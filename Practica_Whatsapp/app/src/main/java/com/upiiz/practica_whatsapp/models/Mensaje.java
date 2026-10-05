package com.upiiz.practica_whatsapp.models;

public class Mensaje {
    private final String texto, hora;
    private final boolean propio;
    public Mensaje(String texto, String hora, boolean propio) {
        this.texto = texto; this.hora = hora; this.propio = propio;
    }
    public String getTexto() { return texto; }
    public String getHora() { return hora; }
    public boolean isPropio() { return propio; }
}
