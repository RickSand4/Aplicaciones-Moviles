package com.upiiz.practica_eohs_01;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/** Small, bounded demo list rendered in a LinearLayout inside the required ScrollView. */
public class ContactAdapter extends BaseAdapter {
    public interface OnContactClick { void open(Contact contact); }
    private final Context context;
    private final OnContactClick listener;
    private List<Contact> visible = new ArrayList<>();

    public ContactAdapter(Context context, OnContactClick listener) {
        this.context = context; this.listener = listener;
    }

    public void filter(List<Contact> contacts, String query, String category) {
        visible = ContactFilter.apply(contacts, query, category);
        // Correct Android API name: notifyDataSetChanged (not NotifiDataChanged).
        notifyDataSetChanged();
    }
    @Override public int getCount() { return visible.size(); }
    @Override public Contact getItem(int position) { return visible.get(position); }
    @Override public long getItemId(int position) { return getItem(position).id.hashCode(); }
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        Contact c = getItem(position);
        LinearLayout row = Ui.row(context); Ui.pad(row,20,12);
        Ui.clickable(row,Color.WHITE,0);
        row.addView(Ui.avatar(context,c,54));
        LinearLayout details = Ui.column(context); Ui.pad(details,14,0);
        row.addView(details,new LinearLayout.LayoutParams(0,-2,1));
        TextView name = Ui.text(context,c.name,17,Ui.INK,true);
        name.setSingleLine(); name.setEllipsize(TextUtils.TruncateAt.END); details.addView(name);
        TextView message = Ui.text(context,c.preview,14,Ui.MUTED,false);
        message.setSingleLine(); message.setEllipsize(TextUtils.TruncateAt.END); details.addView(message);
        TextView lastSeen = Ui.text(context,"Últ. conexión: " + c.lastSeen,11,Ui.MUTED,false);
        lastSeen.setPadding(0,Ui.dp(context,4),0,0); details.addView(lastSeen);
        LinearLayout trailing = Ui.column(context); trailing.setGravity(android.view.Gravity.END);
        trailing.addView(Ui.text(context,c.time,11,c.unread > 0 ? Ui.GREEN : Ui.MUTED,false));
        if (c.unread > 0) {
            TextView badge = Ui.text(context,String.valueOf(c.unread),12,Color.WHITE,true);
            badge.setGravity(android.view.Gravity.CENTER); badge.setBackground(Ui.rounded(Ui.GREEN,20,context));
            LinearLayout.LayoutParams lp = Ui.size(context,24,24); lp.topMargin = Ui.dp(context,8);
            trailing.addView(badge,lp);
        }
        row.addView(trailing); row.setOnClickListener(v -> listener.open(c));
        return row;
    }
}
