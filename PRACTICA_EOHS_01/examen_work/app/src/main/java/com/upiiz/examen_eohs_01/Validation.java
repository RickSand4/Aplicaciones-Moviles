package com.upiiz.examen_eohs_01;


/** Reglas del examen, independientes de las pantallas para poder verificarlas. */
public final class Validation {
    private Validation() { }

    public static boolean validLogin(String username, String password) {
        return "admin".equals(username) && "123456".equals(password);
    }

    public static boolean validName(String name) {
        if (name == null) return false;
        String clean = name.trim();
        return clean.codePointCount(0, clean.length()) >= 3;
    }

    public static boolean matchingPasswords(String password, String confirmation) {
        return password != null && !password.trim().isEmpty() && password.equals(confirmation);
    }
}
