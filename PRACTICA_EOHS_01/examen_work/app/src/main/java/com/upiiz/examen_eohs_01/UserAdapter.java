package com.upiiz.examen_eohs_01;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

/** CustomListView: adapta objetos User a un diseño propio para cada fila. */
public class UserAdapter extends BaseAdapter {
    private final List<User> users;
    private final LayoutInflater inflater;
    public UserAdapter(Context context, List<User> users) {
        this.users = users;
        inflater = LayoutInflater.from(context);
    }
    @Override public int getCount() { return users.size(); }
    @Override public User getItem(int position) { return users.get(position); }
    @Override public long getItemId(int position) { return getItem(position).id; }
    @Override public boolean hasStableIds() { return true; }
    @Override public View getView(int position, View row, ViewGroup parent) {
        Holder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.row_user, parent, false);
            holder = new Holder(row);
            row.setTag(holder);
        } else holder = (Holder) row.getTag();
        User user = getItem(position);
        holder.name.setText(user.name);
        holder.username.setText(user.username);
        holder.avatar.setText(user.initials());
        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setColor(user.color);
        holder.avatar.setBackground(circle);
        return row;
    }
    private static class Holder {
        final TextView name, username, avatar;
        Holder(View row) {
            name = row.findViewById(R.id.user_name);
            username = row.findViewById(R.id.user_handle);
            avatar = row.findViewById(R.id.avatar);
        }
    }
}
