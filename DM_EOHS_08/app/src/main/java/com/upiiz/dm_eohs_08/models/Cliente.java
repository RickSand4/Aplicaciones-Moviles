package com.upiiz.dm_eohs_08.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Cliente {
    private long id;
    private String nombre;
    private String correo;
    private String telefono;
    private String direccion;
    private boolean registrado;

    @SerializedName("links")
    private List<Link> links;

    @SerializedName("_links")
    private Map<String, Link> linksMap;

    public Cliente() {
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isRegistrado() {
        return registrado;
    }

    public void setRegistrado(boolean registrado) {
        this.registrado = registrado;
    }

    public List<Link> getLinks() {
        if (links != null && !links.isEmpty()) {
            return links;
        }
        if (linksMap != null && !linksMap.isEmpty()) {
            List<Link> resultado = new ArrayList<>();
            for (Map.Entry<String, Link> entrada : linksMap.entrySet()) {
                Link link = entrada.getValue();
                if (link == null) {
                    continue;
                }
                if (link.getRel() == null || link.getRel().isEmpty()) {
                    link.setRel(entrada.getKey());
                }
                resultado.add(link);
            }
            return resultado;
        }
        return Collections.emptyList();
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
