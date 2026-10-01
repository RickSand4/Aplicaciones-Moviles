package com.upiiz.dm_eohs_06.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.upiiz.dm_eohs_06.R;

import org.w3c.dom.Text;

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
    public Object getItem(int position) {
        return listaUsuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Comprobar que hay una instancia de convertView
        if (convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.custom_list_item,parent, false);
        }
        TextView tvUser= convertView.findViewById(R.id.tvUser);
        TextView tvLastMessage = convertView.findViewById(R.id.tvUltimoMensaje);
        TextView tvLastConection = convertView.findViewById(R.id.tvUltimaConexion);
        ImageView userImage = convertView.findViewById(R.id.ivUser);
        //seteo
        tvLastConection.setText(listaUsuarios.get(position).getLastConexion());
        tvUser.setText(listaUsuarios.get(position).getNombre());
        tvLastMessage.setText(listaUsuarios.get(position).getLastMessage());
        userImage.setImageResource(listaUsuarios.get(position).getImage());
        return convertView;
    }
}

