package com.upiiz.examen_eohs_01.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.upiiz.examen_eohs_01.R;

import java.util.ArrayList;

import model.User;

public class CustomAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<User> listaUsuarios;

    public CustomAdapter(Context ctx, ArrayList<User> listaUsuarios) {
        this.ctx = ctx;
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public int getCount() {
        return listaUsuarios.size();
    }

    @Override
    public User getItem(int position) {
        return listaUsuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaUsuarios.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. Crear la fila si no hay una disponible
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.custom_list_item, parent, false);
        }

        //2. Enlazar las vistas
        TextView tvNombre = convertView.findViewById(R.id.tvNombre);
        TextView tvUsuario = convertView.findViewById(R.id.tvUsuario);
        ImageView ivUsuario = convertView.findViewById(R.id.ivUsuario);

        //3. Asignar los datos del usuario
        User usuario = listaUsuarios.get(position);
        tvNombre.setText(usuario.getNombre());
        tvUsuario.setText(usuario.getUsuario());
        ivUsuario.setImageResource(usuario.getImagen());
        return convertView;
    }
}
