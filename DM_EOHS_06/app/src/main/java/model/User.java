package model;

public class User {
    private Long id;
    private String nombre;
    private String lastMessage;
    private String lastConexion;
    private String image;

    public User(Long id, String nombre, String lastMessage, String lastConexion, String image) {
        this.id = id;
        this.nombre = nombre;
        this.lastMessage = lastMessage;
        this.lastConexion = lastConexion;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastConexion() {
        return lastConexion;
    }

    public void setLastConexion(String lastConexion) {
        this.lastConexion = lastConexion;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
