package com.upiiz.dm_eohs_08.models;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class Datos {
    private List<Cliente> content;
    private List<Link> links;

    @SerializedName("_embedded")
    private Embedded embedded;

    public List<Cliente> getClientes() {
        if (content != null) {
            return content;
        }
        if (embedded != null && embedded.getClienteDtoList() != null) {
            return embedded.getClienteDtoList();
        }
        return Collections.emptyList();
    }

    public void setContent(List<Cliente> content) {
        this.content = content;
    }

    public List<Link> getLinks() {
        return links != null ? links : Collections.emptyList();
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public static class Embedded {
        @SerializedName("clienteDtoList")
        private List<Cliente> clienteDtoList;

        public List<Cliente> getClienteDtoList() {
            return clienteDtoList;
        }
    }
}
