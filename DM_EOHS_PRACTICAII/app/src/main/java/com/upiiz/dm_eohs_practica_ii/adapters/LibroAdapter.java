package com.upiiz.dm_eohs_practica_ii.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.upiiz.dm_eohs_practica_ii.R;
import com.upiiz.dm_eohs_practica_ii.models.Libro;

import java.util.ArrayList;
import java.util.List;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.LibroViewHolder> {
    public interface OnLibroClickListener {
        void onLibroClick(Libro libro);
    }

    public interface OnEliminarClickListener {
        void onEliminarClick(Libro libro);
    }

    private final List<Libro> libros = new ArrayList<>();
    private final OnLibroClickListener listener;
    private final OnEliminarClickListener eliminarListener;

    public LibroAdapter(OnLibroClickListener listener) {
        this(listener, null);
    }

    public LibroAdapter(OnLibroClickListener listener, OnEliminarClickListener eliminarListener) {
        this.listener = listener;
        this.eliminarListener = eliminarListener;
    }

    public void setLibros(List<Libro> nuevos) {
        libros.clear();
        if (nuevos != null) {
            libros.addAll(nuevos);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_libro, parent, false);
        return new LibroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        holder.bind(libros.get(position), listener, eliminarListener);
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    static class LibroViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitulo;
        private final TextView tvId;
        private final TextView tvAutor;
        private final TextView tvEditorial;
        private final TextView tvPaginas;
        private final TextView tvDisponible;
        private final ImageButton btnEliminarFavorito;

        LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvAutor = itemView.findViewById(R.id.tvAutor);
            tvEditorial = itemView.findViewById(R.id.tvEditorial);
            tvPaginas = itemView.findViewById(R.id.tvPaginas);
            tvDisponible = itemView.findViewById(R.id.tvDisponible);
            btnEliminarFavorito = itemView.findViewById(R.id.btnEliminarFavorito);
        }

        void bind(Libro libro, OnLibroClickListener listener, OnEliminarClickListener eliminarListener) {
            Context context = itemView.getContext();
            String titulo = libro.getTitulo() != null ? libro.getTitulo() : "";
            String autor = libro.getAutor() != null ? libro.getAutor() : "";
            String editorial = libro.getEditorial() != null ? libro.getEditorial() : "";

            tvId.setText(context.getString(R.string.book_id, libro.getId()));
            tvTitulo.setText(titulo);
            tvAutor.setText(context.getString(R.string.book_author, autor));
            tvEditorial.setText(context.getString(R.string.book_editorial, editorial));
            tvPaginas.setText(context.getString(R.string.book_pages, libro.getPaginas()));
            tvDisponible.setText(libro.isDisponible() ? R.string.book_available : R.string.book_unavailable);
            tvDisponible.setTextColor(ContextCompat.getColor(
                    context,
                    libro.isDisponible() ? R.color.disponible : R.color.no_disponible
            ));
            if (eliminarListener != null) {
                btnEliminarFavorito.setVisibility(View.VISIBLE);
                btnEliminarFavorito.setOnClickListener(v -> eliminarListener.onEliminarClick(libro));
            } else {
                btnEliminarFavorito.setVisibility(View.GONE);
                btnEliminarFavorito.setOnClickListener(null);
            }
            itemView.setOnClickListener(v -> listener.onLibroClick(libro));
        }
    }
}
