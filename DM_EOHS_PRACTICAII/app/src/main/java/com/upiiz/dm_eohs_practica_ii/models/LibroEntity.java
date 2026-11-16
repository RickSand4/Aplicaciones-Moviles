package com.upiiz.dm_eohs_practica_ii.models;

import androidx.room3.ColumnInfo;
import androidx.room3.Entity;
import androidx.room3.Ignore;
import androidx.room3.PrimaryKey;

@Entity(tableName = "libros_favoritos")
public class LibroEntity {
    @PrimaryKey
    private long id;
    @ColumnInfo(name = "titulo")
    private String titulo;
    @ColumnInfo(name = "autor")
    private String autor;
    @ColumnInfo(name = "editorial")
    private String editorial;
    @ColumnInfo(name = "paginas")
    private int paginas;
    @ColumnInfo(name = "disponible")
    private boolean disponible;

    public LibroEntity() {
    }

    @Ignore
    public LibroEntity(long id, String titulo, String autor, String editorial, int paginas, boolean disponible) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.paginas = paginas;
        this.disponible = disponible;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
