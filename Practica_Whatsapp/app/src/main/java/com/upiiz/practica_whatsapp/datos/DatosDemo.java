package com.upiiz.practica_whatsapp.datos;

import com.upiiz.practica_whatsapp.R;
import com.upiiz.practica_whatsapp.models.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Date;

/**
 * Datos compartidos en memoria durante la sesión de la aplicación.
 * No hay servidor ni llamadas reales. Al cerrar el proceso se reinicia la demostración.
 */
public class DatosDemo {
    private static DatosDemo instancia;
    private final ArrayList<Usuario> usuarios = new ArrayList<>();
    private final ArrayList<Llamada> llamadas = new ArrayList<>();
    private final ArrayList<Estado> estados = new ArrayList<>();
    private final ArrayList<Canal> canales = new ArrayList<>();
    private final HashMap<Long, ArrayList<Mensaje>> mensajes = new HashMap<>();

    public static DatosDemo obtener() {
        if (instancia == null) instancia = new DatosDemo();
        return instancia;
    }

    private DatosDemo() {
        usuarios.add(new Usuario(1L,"Sofía","¿Vas a ir mañana a la facultad?","Últ. conexión: hoy, 10:30",R.drawable.user));
        usuarios.add(new Usuario(2L,"Mateo","Ya terminé la tarea.","Últ. conexión: hoy, 09:45",R.drawable.user));
        usuarios.add(new Usuario(3L,"Ana","Gracias por los apuntes.","Últ. conexión: ayer, 20:15",R.drawable.user));
        usuarios.add(new Usuario(4L,"Luis","Nos vemos en clase.","Últ. conexión: ayer, 18:00",R.drawable.user));
        usuarios.add(new Usuario(5L,"Grupo Proyecto","Diego: ya subí los archivos.","Últ. conexión: ayer, 17:30",R.drawable.user));
        usuarios.add(new Usuario(6L,"Mariana","¿Repasamos la práctica?","Últ. conexión: ayer, 16:00",R.drawable.user));
        usuarios.add(new Usuario(7L,"Carlos","Te comparto las notas.","Últ. conexión: ayer, 15:45",R.drawable.user));
        usuarios.add(new Usuario(8L,"Valeria","La clase empieza a las ocho.","Últ. conexión: ayer, 14:20",R.drawable.user));
        usuarios.add(new Usuario(9L,"Andrea","¡Gracias por avisar!","Últ. conexión: ayer, 13:30",R.drawable.user));
        usuarios.get(0).setNoLeido(true);
        usuarios.get(0).setFavorito(true);
        usuarios.get(2).setFavorito(true);
        usuarios.get(4).setGrupo(true);
        usuarios.get(4).setNoLeido(true);
        usuarios.get(5).setNoLeido(true);
        for (Usuario usuario : usuarios) {
            ArrayList<Mensaje> conversacion = new ArrayList<>();
            conversacion.add(new Mensaje(usuario.getUltimoMensaje(),"09:30",false));
            mensajes.put(usuario.getId(),conversacion);
        }
        llamadas.add(new Llamada(usuarios.get(0),"Hoy, 09:20 · Ejemplo",false));
        llamadas.add(new Llamada(usuarios.get(2),"Ayer, 18:30 · Ejemplo",true));
        llamadas.add(new Llamada(usuarios.get(1),"Ayer, 12:15 · Ejemplo",false));
        estados.add(new Estado(usuarios.get(0),R.drawable.fondo_estado,"Un descanso después de clase.",false));
        estados.add(new Estado(usuarios.get(2),R.drawable.fondo_estado_tarde,"¡Terminando la práctica!",false));
        estados.add(new Estado(usuarios.get(1),R.drawable.fondo_estado,"Un buen día para caminar.",true));
        estados.add(new Estado(usuarios.get(7),R.drawable.fondo_estado_tarde,"Disfrutando la tarde.",false));
        canales.add(new Canal("Noticias UPIIZ","El taller de programación comienza el lunes.","Hoy","1,200 seguidores",R.drawable.user,3,true));
        canales.add(new Canal("Android en español","Un ejemplo de layouts para practicar.","Ayer","8,500 seguidores",R.drawable.user,5,true));
        canales.add(new Canal("Ciencia diaria","Una curiosidad para aprender hoy.","Hoy","12,000 seguidores",R.drawable.user,2,false));
        canales.add(new Canal("Música para estudiar","Una nueva selección de canciones.","Hoy","6,300 seguidores",R.drawable.user,1,false));
        canales.add(new Canal("Rutas de México","Lugares para tu próximo paseo.","Ayer","9,800 seguidores",R.drawable.user,4,false));
        canales.add(new Canal("Recetas sencillas","Una receta para compartir en casa.","Ayer","4,500 seguidores",R.drawable.user,2,false));
    }

    public ArrayList<Usuario> getUsuarios() { return usuarios; }
    public ArrayList<Llamada> getLlamadas() { return llamadas; }
    public ArrayList<Estado> getEstados() { return estados; }
    public ArrayList<Canal> getCanales() { return canales; }
    public Usuario buscarUsuario(long id) {
        for (Usuario usuario : usuarios) if (usuario.getId() == id) return usuario;
        return null;
    }
    public ArrayList<Mensaje> getMensajes(long id) { return mensajes.get(id); }
    public void enviarMensaje(Usuario usuario, String texto) {
        String hora = new SimpleDateFormat("HH:mm",Locale.getDefault()).format(new Date());
        mensajes.get(usuario.getId()).add(new Mensaje(texto,hora,true));
        usuario.setUltimoMensaje(texto);
    }
    public void registrarLlamada(Usuario usuario, boolean video) {
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault()).format(new Date());
        llamadas.add(0,new Llamada(usuario,fecha,video));
    }
}
