package com.upiiz.examen_eohs_01;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

/** CustomListView de mensajes: dos tipos de fila evitan reciclar burbujas del lado contrario. */
public class MessageAdapter extends BaseAdapter {
    private final List<Message> messages;
    private final LayoutInflater inflater;
    public MessageAdapter(Context context, List<Message> messages) {
        this.messages = messages;
        inflater = LayoutInflater.from(context);
    }
    @Override public int getCount() { return messages.size(); }
    @Override public Message getItem(int position) { return messages.get(position); }
    @Override public long getItemId(int position) { return position; }
    @Override public int getViewTypeCount() { return 2; }
    @Override public int getItemViewType(int position) { return getItem(position).outgoing ? 1 : 0; }
    @Override public View getView(int position, View row, ViewGroup parent) {
        Holder holder;
        if (row == null) {
            row = inflater.inflate(getItemViewType(position) == 1
                    ? R.layout.row_message_out : R.layout.row_message_in, parent, false);
            holder = new Holder(row);
            row.setTag(holder);
        } else holder = (Holder) row.getTag();
        Message message = getItem(position);
        holder.text.setText(message.text);
        holder.time.setText(message.time);
        return row;
    }
    private static class Holder {
        final TextView text, time;
        Holder(View view) {
            text = view.findViewById(R.id.message_text);
            time = view.findViewById(R.id.message_time);
        }
    }
}
