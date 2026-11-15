package com.upiiz.dm_eohs_08.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.upiiz.dm_eohs_08.R;
import com.upiiz.dm_eohs_08.models.Cliente;

import java.util.Objects;

public class ClienteAdapter extends ListAdapter<Cliente, ClienteAdapter.ClienteViewHolder> {

    public interface OnClienteAccionListener {
        void onVerCliente(Cliente cliente);

        void onEditarCliente(Cliente cliente);

        void onEliminarCliente(Cliente cliente);
    }

    private static final DiffUtil.ItemCallback<Cliente> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Cliente>() {
                @Override
                public boolean areItemsTheSame(@NonNull Cliente anterior, @NonNull Cliente actual) {
                    return anterior.getId() == actual.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Cliente anterior, @NonNull Cliente actual) {
                    return anterior.getId() == actual.getId()
                            && Objects.equals(anterior.getNombre(), actual.getNombre())
                            && Objects.equals(anterior.getCorreo(), actual.getCorreo())
                            && Objects.equals(anterior.getTelefono(), actual.getTelefono())
                            && Objects.equals(anterior.getDireccion(), actual.getDireccion())
                            && anterior.isRegistrado() == actual.isRegistrado();
                }
            };

    private final OnClienteAccionListener listener;

    public ClienteAdapter(OnClienteAccionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class ClienteViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvId;
        private final TextView tvNombre;
        private final TextView tvCorreo;
        private final TextView tvTelefono;
        private final Chip chipRegistrado;
        private final ImageButton btnOpciones;

        ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCorreo = itemView.findViewById(R.id.tvCorreo);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
            chipRegistrado = itemView.findViewById(R.id.chipRegistrado);
            btnOpciones = itemView.findViewById(R.id.btnOpciones);
        }

        void bind(final Cliente cliente, final OnClienteAccionListener listener) {
            tvId.setText(itemView.getContext().getString(R.string.cliente_id_formato, cliente.getId()));
            tvNombre.setText(cliente.getNombre());
            tvCorreo.setText(cliente.getCorreo());
            tvTelefono.setText(itemView.getContext().getString(
                    R.string.telefono_formato,
                    cliente.getTelefono() != null ? cliente.getTelefono() : ""));
            chipRegistrado.setText(cliente.isRegistrado() ? R.string.registrado : R.string.no_registrado);

            itemView.setOnClickListener(v -> listener.onVerCliente(cliente));
            btnOpciones.setOnClickListener(v -> mostrarMenu(cliente, listener));
        }

        private void mostrarMenu(final Cliente cliente, final OnClienteAccionListener listener) {
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), btnOpciones);
            popupMenu.inflate(R.menu.menu_item_cliente);
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.accion_editar) {
                    listener.onEditarCliente(cliente);
                    return true;
                }
                if (id == R.id.accion_eliminar) {
                    listener.onEliminarCliente(cliente);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        }
    }
}
