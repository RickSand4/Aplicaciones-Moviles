package com.upiiz.practica_eohs_01;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/** Local demo data; no messages or calls leave this device. */
public class DemoStore {
    private final SharedPreferences prefs;

    public DemoStore(Context context) {
        prefs = context.getSharedPreferences("whatsapp_practice", Context.MODE_PRIVATE);
    }

    public List<Contact> contacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact("ana", "Ana Martínez", "¡Nos vemos en la cafetería! ☕", "Hoy, 10:42", "10:42", "#D69B83", 2, false, true));
        contacts.add(contact("equipo", "Equipo de proyecto", "Diego: Ya subí los archivos", "Hoy, 10:30", "10:30", "#648A76", 5, true, true));
        contacts.add(contact("diego", "Diego Ramírez", "Te mando las fotos más tarde", "Hoy, 09:58", "09:58", "#7D9DB4", 0, false, true));
        contacts.add(contact("sofia", "Sofía López", "¡Gracias! Quedó increíble ✨", "Hoy, 09:15", "09:15", "#B593B4", 1, false, false));
        contacts.add(contact("familia", "Familia", "Mamá: ¿Quién viene a comer?", "Hoy, 08:50", "08:50", "#CBAC68", 0, true, false));
        contacts.add(contact("carlos", "Carlos Hernández", "Perfecto, de acuerdo", "Ayer, 20:12", "Ayer", "#7F9B91", 0, false, false));
        contacts.add(contact("valeria", "Valeria Torres", "Mira este lugar 🌿", "Ayer, 18:40", "Ayer", "#BA8D78", 0, false, false));
        contacts.add(contact("andres", "Andrés García", "¿Jugamos el fin de semana?", "Ayer, 17:05", "Ayer", "#808EAC", 3, false, false));
        contacts.add(contact("diseno", "Club de diseño", "Lucía: Nueva inspiración para hoy", "Ayer, 15:20", "Ayer", "#B39466", 0, true, false));
        contacts.add(contact("mariana", "Mariana Ruiz", "Aquí está la tarea de Android", "Ayer, 14:30", "Ayer", "#A98498", 0, false, false));
        return contacts;
    }

    private Contact contact(String id, String name, String preview, String seen, String time,
                            String color, int unread, boolean group, boolean favorite) {
        return new Contact(id, name, prefs.getString("preview_" + id, preview), seen,
                prefs.getString("time_" + id, time), Color.parseColor(color),
                prefs.getBoolean("read_" + id, false) ? 0 : unread, group,
                prefs.getBoolean("favorite_" + id, favorite));
    }

    public Contact find(String id) {
        for (Contact c : contacts()) if (c.id.equals(id)) return c;
        return null;
    }

    public void read(String id) { prefs.edit().putBoolean("read_" + id, true).apply(); }
    public void favorite(String id, boolean value) { prefs.edit().putBoolean("favorite_" + id, value).apply(); }
    public boolean flag(String key) { return prefs.getBoolean(key, false); }
    public void flag(String key, boolean value) { prefs.edit().putBoolean(key, value).apply(); }

    public JSONArray messages(String id) { return array("messages_" + id); }
    public JSONArray calls() { return array("calls"); }

    private JSONArray array(String key) {
        try { return new JSONArray(prefs.getString(key, "[]")); }
        catch (JSONException e) { return new JSONArray(); }
    }

    public void send(String id, String body) {
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        JSONArray messages = messages(id);
        JSONObject message = new JSONObject();
        try {
            message.put("body", body);
            message.put("time", time);
        } catch (JSONException e) { throw new IllegalStateException(e); }
        messages.put(message);
        prefs.edit().putString("messages_" + id, messages.toString())
                .putString("preview_" + id, "Tú: " + body).putString("time_" + id, time).apply();
    }

    public void call(Contact contact, boolean video) {
        JSONArray calls = calls();
        JSONObject call = new JSONObject();
        try {
            call.put("id", contact.id);
            call.put("video", video);
            call.put("date", new SimpleDateFormat("dd/MM, HH:mm", Locale.getDefault()).format(new Date()));
        } catch (JSONException e) { throw new IllegalStateException(e); }
        calls.put(call);
        prefs.edit().putString("calls", calls.toString()).apply();
    }
}
