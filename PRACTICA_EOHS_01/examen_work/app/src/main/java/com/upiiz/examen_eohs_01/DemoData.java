package com.upiiz.examen_eohs_01;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class DemoData {
    private DemoData() { }
    public static List<User> users() {
        String[] names = {"Ana García", "Luis Martínez", "Marta López", "Pedro Sánchez",
                "Sofía Torres", "Juan Rodríguez", "Laura Hernández", "Carlos Ramírez",
                "Valeria Flores", "Diego Gómez", "Camila Pérez", "Miguel Díaz",
                "Lucía Romero", "Jorge Castro", "Elena Vargas", "Andrés Mendoza",
                "Daniela Ruiz", "Fernando Ortiz", "Mariana Silva", "Ricardo Reyes"};
        String[] handles = {"ana123", "luism", "marta_22", "pedro99", "sofia_t", "juanr",
                "laura_h", "carlos_r", "vale_f", "diego_g", "camila_p", "miguel_d",
                "lucia_r", "jorge_c", "elena_v", "andres_m", "dani_ruiz", "fer_ortiz",
                "mariana_s", "ricardo_r"};
        int[] colors = {0xff1d64d8, 0xff7c49b8, 0xff008577, 0xffc06524, 0xffb83d68};
        List<User> users = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            users.add(new User(i + 1, names[i], handles[i], colors[i % colors.length]));
        }
        return users;
    }

    public static User findUser(int id) {
        for (User user : users()) if (user.id == id) return user;
        return null;
    }

    public static List<Message> messages(User user) {
        String firstName = user.name.split(" ")[0];
        String[] texts = {"Hola " + firstName + ", ¿cómo estás?", "¡Hola! Muy bien, ¿y tú?",
                "Todo bien, ¿quieres vernos más tarde?", "Sí, me parece bien. ¿A qué hora?",
                "¿Te parece a las 5?", "Perfecto, nos vemos entonces.",
                "¿Nos encontramos en la biblioteca?", "Sí, en la entrada principal.",
                "Llevaré mis apuntes de aplicaciones móviles.", "¡Genial! Yo llevo mi computadora.",
                "Podemos repasar los ejercicios juntos.", "Me gustaría practicar las listas personalizadas.",
                "También revisamos la validación del login.", "Buena idea, tengo unas dudas del registro.",
                "Las resolvemos paso a paso.", "Gracias, así terminamos el repaso.",
                "¿Llevamos algo de tomar?", "Yo llevo agua para los dos.",
                "¡Perfecto! Te aviso cuando llegue.", "De acuerdo, ¡nos vemos al rato!"};
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < texts.length; i++) {
            messages.add(new Message(texts[i], String.format(Locale.ROOT, "09:%02d", 32 + i), i % 2 == 0));
        }
        return messages;
    }
}
