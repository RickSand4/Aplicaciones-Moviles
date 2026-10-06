package model;

public class User {
    private long id;
    private String nombre;
    private String usuario;
    private int imagen;

    public User(long id, String nombre, String usuario, int imagen) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.imagen = imagen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
