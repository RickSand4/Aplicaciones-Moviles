package com.upiiz.practica_eohs_01;

public class Contact {
    public final String id, name, preview, lastSeen, time;
    public final int color, unread;
    public final boolean group, favorite;

    public Contact(String id, String name, String preview, String lastSeen, String time,
                   int color, int unread, boolean group, boolean favorite) {
        this.id = id;
        this.name = name;
        this.preview = preview;
        this.lastSeen = lastSeen;
        this.time = time;
        this.color = color;
        this.unread = unread;
        this.group = group;
        this.favorite = favorite;
    }
}
