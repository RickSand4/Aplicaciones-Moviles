package com.upiiz.examen_eohs_01.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.upiiz.examen_eohs_01.R;

import java.util.ArrayList;

import model.Mensaje;

public class MensajeAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<Mensaje> listaMensajes;

    public MensajeAdapter(Context ctx, ArrayList<Mensaje> listaMensajes) {
        this.ctx = ctx;
        this.listaMensajes = listaMensajes;
    }

    @Override
    public int getCount() {
        return listaMensajes.size();
    }

    @Override
    public Mensaje getItem(int position) {
        return listaMensajes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. Crear la fila si no hay una disponible
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.custom_list_mensaje, parent, false);
        }

        //2. Enlazar las vistas
        TextView tvMensaje = convertView.findViewById(R.id.tvMensaje);
        TextView tvHora = convertView.findViewById(R.id.tvHora);
        LinearLayout llBurbuja = convertView.findViewById(R.id.llBurbuja);

        //3. Asignar los datos del mensaje
        Mensaje mensaje = listaMensajes.get(position);
        tvMensaje.setText(mensaje.getTexto());
        tvHora.setText(mensaje.getHora());

        //4. Colocar enviados a la derecha y recibidos a la izquierda
        LinearLayout.LayoutParams parametros = (LinearLayout.LayoutParams) llBurbuja.getLayoutParams();
        if (mensaje.isEnviado()) {
            parametros.gravity = Gravity.END;
            llBurbuja.setBackgroundResource(R.drawable.bubble_out);
            tvMensaje.setTextColor(ContextCompat.getColor(ctx, R.color.white));
            tvHora.setTextColor(ContextCompat.getColor(ctx, R.color.white));
        } else {
            parametros.gravity = Gravity.START;
            llBurbuja.setBackgroundResource(R.drawable.bubble_in);
            tvMensaje.setTextColor(ContextCompat.getColor(ctx, R.color.text_primary));
            tvHora.setTextColor(ContextCompat.getColor(ctx, R.color.text_secondary));
        }
        llBurbuja.setLayoutParams(parametros);
        return convertView;
    }
}
