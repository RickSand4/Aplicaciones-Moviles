package com.upiiz.practica_whatsapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.upiiz.practica_whatsapp.R;
import com.upiiz.practica_whatsapp.models.Usuario;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsuarioAdapter extends BaseAdapter {

    private Context contexto;
    private ArrayList<Usuario> listaUsuarios;

    public UsuarioAdapter(Context contexto,
                          ArrayList<Usuario> listaUsuarios) {
        this.contexto = contexto;
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public int getCount() {
        return listaUsuarios.size();
    }

    @Override
    public Usuario getItem(int position) {
        return listaUsuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {

        // 1. Crear la fila si no hay una disponible para reutilizar
        if (convertView == null) {
            convertView = LayoutInflater.from(contexto).inflate(
                    R.layout.item_usuario,
                    parent,
                    false
            );
        }

        // 2. Enlazar las vistas de esta fila
        CircleImageView ivUsuario =
                convertView.findViewById(R.id.ivUsuario);

        TextView tvNombre =
                convertView.findViewById(R.id.tvNombre);

        TextView tvUltimoMensaje =
                convertView.findViewById(R.id.tvUltimoMensaje);

        TextView tvUltimaConexion =
                convertView.findViewById(R.id.tvUltimaConexion);

        // 3. Obtener el usuario de esta posición
        Usuario usuario = getItem(position);

        // 4. Colocar sus datos en la fila
        ivUsuario.setImageResource(usuario.getImagen());
        tvNombre.setText(usuario.getNombre());
        tvUltimoMensaje.setText(usuario.getUltimoMensaje());
        tvUltimaConexion.setText(usuario.getUltimaConexion());
        convertView.findViewById(R.id.tvNoLeido)
                .setVisibility(usuario.isNoLeido() ? View.VISIBLE : View.GONE);

        // 5. Devolver la fila preparada
        return convertView;
    }
}
