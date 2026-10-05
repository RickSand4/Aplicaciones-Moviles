package com.upiiz.practica_whatsapp;

import com.upiiz.practica_whatsapp.models.Usuario;
import com.upiiz.practica_whatsapp.util.FiltroUsuarios;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class FiltroUsuariosTest {
    private Usuario usuario(long id,String nombre,boolean favorito,boolean noLeido,boolean grupo) {
        Usuario u = new Usuario(id,nombre,"","",0);
        u.setFavorito(favorito); u.setNoLeido(noLeido); u.setGrupo(grupo);
        return u;
    }
    @Test public void limitaBusquedaYRecuperaTodosAlBorrar() {
        ArrayList<Usuario> todos = new ArrayList<>();
        for (int i=0;i<9;i++) todos.add(usuario(i,"Ana " + i,false,false,false));
        assertEquals(5,FiltroUsuarios.filtrar(todos,"ana","Todos").size());
        assertEquals(9,FiltroUsuarios.filtrar(todos,"  ","Todos").size());
        assertEquals(9,todos.size());
    }
    @Test public void combinaCategoriasConTexto() {
        ArrayList<Usuario> todos = new ArrayList<>();
        todos.add(usuario(1,"Ana",true,false,false));
        todos.add(usuario(2,"Ana grupo",false,true,true));
        assertEquals(1,FiltroUsuarios.filtrar(todos,"ana","Favoritos").get(0).getId());
        assertEquals(2,FiltroUsuarios.filtrar(todos,"ana","No leídos").get(0).getId());
        assertEquals(2,FiltroUsuarios.filtrar(todos,"ana","Grupos").get(0).getId());
        assertTrue(FiltroUsuarios.filtrar(todos,"Luis","Favoritos").isEmpty());
    }
    @Test public void ignoraMayusculasYEspaciosDeExtremos() {
        ArrayList<Usuario> todos = new ArrayList<>();
        todos.add(usuario(1,"Mateo",true,false,false));
        assertEquals(1,FiltroUsuarios.filtrar(todos," MA ","Todos").size());
    }
    @Test public void aplicaLimiteDespuesDeCategoria() {
        ArrayList<Usuario> todos = new ArrayList<>();
        for (int i=0;i<8;i++) todos.add(usuario(i,"Ana " + i,false,false,false));
        todos.add(usuario(20,"Ana favorita",true,false,false));
        assertEquals(20,FiltroUsuarios.filtrar(todos,"ana","Favoritos").get(0).getId());
    }
}
