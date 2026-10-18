package com.upiiz.examen_eohs_01;


public final class User {
    public final int id;
    public final String name;
    public final String username;
    public final int color;
    public User(int id, String name, String username, int color) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.color = color;
    }
    public String initials() {
        String[] words = name.split(" ");
        return words[0].substring(0, 1) + words[words.length - 1].substring(0, 1);
    }
}
