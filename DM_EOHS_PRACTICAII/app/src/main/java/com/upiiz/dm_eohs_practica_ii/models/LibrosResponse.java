package com.upiiz.dm_eohs_practica_ii.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LibrosResponse {
    @SerializedName("_embedded")
    private Embedded embedded;

    public LibrosResponse() {
    }

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }

    public List<Libro> getLibros() {
        return embedded != null ? embedded.getLibroDtoList() : null;
    }

    public static class Embedded {
        @SerializedName("libroDtoList")
        private List<Libro> libroDtoList;

        public Embedded() {
        }

        public List<Libro> getLibroDtoList() {
            return libroDtoList;
        }

        public void setLibroDtoList(List<Libro> libroDtoList) {
            this.libroDtoList = libroDtoList;
        }
    }
}
