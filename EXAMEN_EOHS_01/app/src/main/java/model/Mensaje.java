package model;

public class Mensaje {
    private String texto;
    private String hora;
    private boolean enviado;

    public Mensaje(String texto, String hora, boolean enviado) {
        this.texto = texto;
        this.hora = hora;
        this.enviado = enviado;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }
}
