package com.upiiz.practica_eohs_01;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatActivity extends AppCompatActivity {
    public static final String EXTRA_USER_NAME = "nombre_usuario";
    public static final String EXTRA_USER_ID = "usuario_id";
    private DemoStore store;
    private Contact contact;
    private LinearLayout messages;
    private EditText input;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        store = new DemoStore(this);
        contact = store.find(getIntent().getStringExtra(EXTRA_USER_ID));
        if (contact == null) { finish(); return; }
        store.read(contact.id);
        setContentView(R.layout.activity_chat);
        Ui.insets(this);
        LinearLayout header = findViewById(R.id.chat_header);
        Ui.action(header,R.drawable.ic_back,"Volver a chats",this::finish);
        header.addView(Ui.avatar(this,contact,42));
        LinearLayout title = Ui.column(this); Ui.pad(title,10,0);
        String name = getIntent().getStringExtra(EXTRA_USER_NAME);
        title.addView(Ui.text(this,name == null ? contact.name : name,17,Ui.INK,true));
        title.addView(Ui.text(this,"Últ. conexión: " + contact.lastSeen,11,Ui.MUTED,false));
        header.addView(title,new LinearLayout.LayoutParams(0,-2,1));
        Ui.action(header,R.drawable.ic_phone,"Llamar",() -> {
            store.call(contact,false);
            new AlertDialog.Builder(this).setTitle("Llamada simulada")
                    .setMessage("Llamada a " + contact.name + ". No se realiza una llamada real.")
                    .setPositiveButton("Finalizar",null).show();
        });
        messages = findViewById(R.id.messages);
        input = findViewById(R.id.message_input);
        input.setBackground(Ui.rounded(Color.WHITE,26,this));
        if (state != null) input.setText(state.getString("draft",""));
        ImageButton send = findViewById(R.id.send_message);
        send.setImageResource(R.drawable.ic_send); send.setColorFilter(Color.WHITE);
        Ui.clickable(send,Ui.GREEN,24);
        send.setOnClickListener(v -> {
            String body = input.getText().toString().trim();
            if (body.isEmpty()) return;
            store.send(contact.id,body); input.setText(""); renderMessages();
        });
        renderMessages();
    }

    @Override protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (input != null) state.putString("draft",input.getText().toString());
    }

    private void renderMessages() {
        messages.removeAllViews();
        TextView notice = Ui.text(this,"Conversación de práctica · Los mensajes se guardan solo en este dispositivo.",12,Ui.MUTED,false);
        notice.setGravity(Gravity.CENTER); Ui.pad(notice,18,14); messages.addView(notice);
        bubble("¡Hola! Qué gusto verte por aquí.","09:30",false);
        bubble("¡Hola! ¿Cómo va tu día?","09:31",true);
        bubble("Todo muy bien. Luego nos ponemos al día 😊","09:32",false);
        JSONArray saved = store.messages(contact.id);
        for (int i = 0; i < saved.length(); i++) {
            JSONObject message = saved.optJSONObject(i);
            if (message != null) bubble(message.optString("body"),message.optString("time"),true);
        }
        ScrollView scroll = findViewById(R.id.messages_scroll);
        scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
    }

    private void bubble(String body, String time, boolean outgoing) {
        LinearLayout row = Ui.row(this); row.setGravity(outgoing ? Gravity.END : Gravity.START);
        row.setPadding(outgoing ? Ui.dp(this,40) : 0,Ui.dp(this,5),outgoing ? 0 : Ui.dp(this,40),Ui.dp(this,5));
        LinearLayout bubble = Ui.column(this); Ui.pad(bubble,12,8);
        bubble.setBackground(Ui.rounded(outgoing ? 0xFFD9FDD3 : Color.WHITE,14,this));
        TextView text = Ui.text(this,body,16,Ui.INK,false); text.setTextIsSelectable(true); bubble.addView(text);
        TextView stamp = Ui.text(this,time + (outgoing ? " · Local" : ""),10,Ui.MUTED,false);
        stamp.setGravity(Gravity.END); bubble.addView(stamp);
        row.addView(bubble,Ui.size(this,-2,-2)); messages.addView(row,Ui.size(this,-1,-2));
    }
}
