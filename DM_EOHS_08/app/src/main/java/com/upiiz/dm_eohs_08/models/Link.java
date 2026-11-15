package com.upiiz.dm_eohs_08.models;

public class Link {
    private String rel;
    private String href;
    private String hreflang;
    private String title;
    private String type;
    private String name;
    private Boolean templated;

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getHreflang() {
        return hreflang;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Boolean getTemplated() {
        return templated;
    }

    public String getEtiqueta() {
        if (rel != null && !rel.isEmpty()) {
            return rel;
        }
        if (name != null && !name.isEmpty()) {
            return name;
        }
        if (title != null && !title.isEmpty()) {
            return title;
        }
        return "enlace";
    }
}
