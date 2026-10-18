package com.upiiz.practica_whatsapp.models;

public class Canal {
    private final String titulo, ultimoMensaje, fecha, seguidores;
    private final int imagen;
    private int sinLeer;
    private boolean seguido;
    public Canal(String titulo, String ultimoMensaje, String fecha, String seguidores,
                 int imagen, int sinLeer, boolean seguido) {
        this.titulo = titulo; this.ultimoMensaje = ultimoMensaje;
        this.fecha = fecha; this.seguidores = seguidores; this.imagen = imagen;
        this.sinLeer = sinLeer; this.seguido = seguido;
    }
    public String getTitulo() { return titulo; }
    public String getUltimoMensaje() { return ultimoMensaje; }
    public String getFecha() { return fecha; }
    public String getSeguidores() { return seguidores; }
    public int getImagen() { return imagen; }
    public int getSinLeer() { return sinLeer; }
    public void setSinLeer(int valor) { sinLeer = valor; }
    public boolean isSeguido() { return seguido; }
    public void setSeguido(boolean valor) { seguido = valor; }
}
