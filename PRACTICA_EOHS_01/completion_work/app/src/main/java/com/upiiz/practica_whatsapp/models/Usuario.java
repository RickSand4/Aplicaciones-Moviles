package com.upiiz.practica_whatsapp.models;

public class Usuario {

    // Datos de cada usuario
    private long id;
    private String nombre;
    private String ultimoMensaje;
    private String ultimaConexion;
    private int imagen;
    private boolean noLeido;
    private boolean favorito;
    private boolean grupo;
    // Constructor
    public Usuario(long id, String nombre, String ultimoMensaje,
                   String ultimaConexion, int imagen) {
        this.id = id;
        this.nombre = nombre;
        this.ultimoMensaje = ultimoMensaje;
        this.ultimaConexion = ultimaConexion;
        this.imagen = imagen;
    }

    // Métodos para consultar los datos
    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }

    public String getUltimaConexion() {
        return ultimaConexion;
    }

    public int getImagen() {
        return imagen;
    }
    public boolean isNoLeido() {
        return noLeido;
    }

    public void setNoLeido(boolean noLeido) {
        this.noLeido = noLeido;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public boolean isGrupo() {
        return grupo;
    }

    public void setGrupo(boolean grupo) {
        this.grupo = grupo;
    }
}
