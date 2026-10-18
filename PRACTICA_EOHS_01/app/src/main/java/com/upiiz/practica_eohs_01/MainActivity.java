package com.upiiz.practica_eohs_01;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private LinearLayout header, content, navigation, contactList, filters;
    private TextView resultLabel;
    private DemoStore store;
    private ContactAdapter adapter;
    private String tab = "Chats", category = "Todos", query = "";
    private boolean expandedChannels;
    private final String[][] channels = {
            {"design", "Diseño al día", "Ideas que merecen compartirse ✨", "10:20", "12", "245 mil seguidores"},
            {"tech", "Tecnología en español", "Lo nuevo en el mundo de Android", "Ayer", "3", "182 mil seguidores"},
            {"travel", "Rutas y rincones", "Tu próxima aventura empieza aquí", "Ayer", "8", "98 mil seguidores"},
            {"food", "Cocina con calma", "Recetas sencillas para cada día", "Ayer", "2", "76 mil seguidores"},
            {"science", "Ciencia en un minuto", "Una nueva forma de mirar el mundo", "Ayer", "4", "310 mil seguidores"}
    };

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        Ui.insets(this);
        store = new DemoStore(this);
        header = findViewById(R.id.header);
        content = findViewById(R.id.content);
        navigation = findViewById(R.id.navigation);
        if (state != null) {
            tab = state.getString("tab", "Chats");
            category = state.getString("category", "Todos");
            query = state.getString("query", "");
            expandedChannels = state.getBoolean("expanded");
        }
        showTab();
    }

    @Override protected void onResume() {
        super.onResume();
        if (adapter != null && "Chats".equals(tab)) updateContacts();
    }

    @Override protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("tab",tab); state.putString("category",category);
        state.putString("query",query); state.putBoolean("expanded",expandedChannels);
    }

    private void showTab() {
        header.removeAllViews(); content.removeAllViews(); navigation.removeAllViews();
        adapter = null;
        LinearLayout titleRow = Ui.row(this); Ui.pad(titleRow,20,12);
        TextView title = Ui.text(this,"Chats".equals(tab) ? "WhatsApp" : tab,28,
                "Chats".equals(tab) ? Ui.GREEN : Ui.INK,true);
        titleRow.addView(title,new LinearLayout.LayoutParams(0,-2,1));
        Ui.action(titleRow,R.drawable.ic_more,"Acerca de la práctica",() ->
                new AlertDialog.Builder(this).setTitle("WhatsApp · Práctica")
                        .setMessage("Aplicación educativa con datos locales. Los mensajes se guardan en este dispositivo. Las llamadas son simuladas.")
                        .setPositiveButton("Entendido",null).show());
        header.addView(titleRow);
        if ("Chats".equals(tab)) chats();
        else if ("Llamadas".equals(tab)) calls();
        else updates();
        navItem("Chats",R.drawable.ic_chat);
        navItem("Novedades",R.drawable.ic_updates);
        navItem("Llamadas",R.drawable.ic_phone);
        ((ScrollView)findViewById(R.id.content_scroll)).scrollTo(0,0);
    }

    private void navItem(String label, int icon) {
        boolean selected = tab.equals(label);
        LinearLayout item = Ui.column(this); item.setGravity(Gravity.CENTER);
        ImageView image = Ui.icon(this,icon,label,selected ? Ui.GREEN : Ui.INK);
        Ui.pad(image,20,6);
        if (selected) image.setBackground(Ui.rounded(Ui.PALE,24,this));
        item.addView(image,Ui.size(this,64,36));
        TextView text = Ui.text(this,label,12,selected ? Ui.GREEN : Ui.INK,selected);
        text.setPadding(0,Ui.dp(this,4),0,0); item.addView(text);
        Ui.clickable(item,Color.TRANSPARENT,0);
        item.setContentDescription(label + (selected ? ", seleccionada" : ""));
        item.setOnClickListener(v -> { tab = label; showTab(); });
        navigation.addView(item,new LinearLayout.LayoutParams(0,-2,1));
    }

    private void chats() {
        LinearLayout search = Ui.row(this); Ui.pad(search,8,0);
        search.setBackground(Ui.rounded(0xFFF0F3F1,28,this));
        search.addView(Ui.icon(this,R.drawable.ic_search,"Buscar usuarios",Ui.MUTED),Ui.size(this,40,48));
        EditText input = new EditText(this); input.setSingleLine(true); input.setTextSize(16);
        input.setTextColor(Ui.INK); input.setHintTextColor(Ui.MUTED);
        input.setHint("Buscar un nombre"); input.setBackgroundColor(Color.TRANSPARENT);
        input.setContentDescription("Buscar usuarios, máximo cinco resultados");
        input.setText(query);
        search.addView(input,new LinearLayout.LayoutParams(0,Ui.dp(this,48),1));
        LinearLayout.LayoutParams searchLp = Ui.size(this,-1,-2);
        searchLp.setMargins(Ui.dp(this,20),0,Ui.dp(this,20),Ui.dp(this,12)); header.addView(search,searchLp);
        HorizontalScrollView horizontal = new HorizontalScrollView(this); horizontal.setHorizontalScrollBarEnabled(false);
        filters = Ui.row(this); Ui.pad(filters,20,0); horizontal.addView(filters); header.addView(horizontal);
        drawFilters();
        resultLabel = Ui.text(this,"",12,Ui.MUTED,false); Ui.pad(resultLabel,20,12); content.addView(resultLabel);
        contactList = Ui.column(this); content.addView(contactList);
        adapter = new ContactAdapter(this,this::openChat);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override public void onChanged() {
                contactList.removeAllViews();
                for (int i = 0; i < adapter.getCount(); i++)
                    contactList.addView(adapter.getView(i,null,contactList));
                if (adapter.getCount() == 0) {
                    TextView empty = Ui.text(MainActivity.this,"No se encontraron usuarios.\nPrueba otro nombre o filtro.",16,Ui.MUTED,false);
                    Ui.pad(empty,24,40); empty.setGravity(Gravity.CENTER); contactList.addView(empty);
                }
                resultLabel.setText(query.trim().isEmpty() ? adapter.getCount() + " conversaciones"
                        : adapter.getCount() + " resultados · máximo " + ContactFilter.SEARCH_LIMIT);
            }
        });
        input.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s,int start,int count,int after) { }
            @Override public void onTextChanged(CharSequence s,int start,int before,int count) { query = s.toString(); updateContacts(); }
            @Override public void afterTextChanged(Editable s) { }
        });
        updateContacts();
        TextView footer = Ui.text(this,"Tus conversaciones, siempre a mano.",12,Ui.MUTED,false);
        footer.setGravity(Gravity.CENTER); Ui.pad(footer,20,24); content.addView(footer);
    }

    private void drawFilters() {
        filters.removeAllViews();
        for (String value : new String[]{"Todos","No leídos","Favoritos","Grupos"}) {
            TextView chip = Ui.pill(this,value,category.equals(value));
            chip.setSelected(category.equals(value));
            LinearLayout.LayoutParams lp = Ui.size(this,-2,-2); lp.setMarginEnd(Ui.dp(this,8)); filters.addView(chip,lp);
            chip.setOnClickListener(v -> { category = value; drawFilters(); updateContacts(); });
        }
    }
    private void updateContacts() { adapter.filter(store.contacts(),query,category); }
    private void openChat(Contact contact) {
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_USER_NAME,contact.name);
        intent.putExtra(ChatActivity.EXTRA_USER_ID,contact.id);
        startActivity(intent);
    }

    private LinearLayout section(String title, String action, Runnable callback) {
        LinearLayout row = Ui.row(this); Ui.pad(row,20,16);
        row.addView(Ui.text(this,title,21,Ui.INK,true),new LinearLayout.LayoutParams(0,-2,1));
        if (action != null) {
            TextView button = Ui.pill(this,action,true); button.setOnClickListener(v -> callback.run()); row.addView(button);
        }
        content.addView(row); return row;
    }

    private void calls() {
        section("Favoritos","+ Más",this::chooseFavorites);
        int favorites = 0;
        for (Contact c : store.contacts()) if (c.favorite && !c.group) {
            favorites++;
            LinearLayout row = contactRow(c,c.name,"Toca para abrir el chat");
            Ui.action(row,R.drawable.ic_phone,"Llamar a " + c.name,() -> call(c,false));
            Ui.action(row,R.drawable.ic_video,"Videollamar a " + c.name,() -> call(c,true));
            content.addView(row);
        }
        if (favorites == 0) note("Agrega a las personas con las que más hablas.");
        section("Recientes",null,null);
        JSONArray calls = store.calls();
        for (int i = calls.length()-1; i >= 0; i--) {
            JSONObject entry = calls.optJSONObject(i);
            if (entry == null) continue;
            Contact c = store.find(entry.optString("id"));
            if (c != null) recent(c,"↗ " + entry.optString("date") + " · Simulada",entry.optBoolean("video"));
        }
        recent(store.find("ana"),"↗ Hoy, 09:24",false);
        recent(store.find("diego"),"↙ Ayer, 18:42",true);
        recent(store.find("sofia"),"Llamada perdida · Ayer, 16:10",false);
        recent(store.find("carlos"),"↗ 28 de septiembre, 12:35",false);
        note("Las llamadas de esta práctica son simuladas.");
    }

    private LinearLayout contactRow(Contact c, String title, String subtitle) {
        LinearLayout row = Ui.row(this); Ui.pad(row,20,10);
        row.addView(Ui.avatar(this,c,48));
        LinearLayout labels = Ui.column(this); Ui.pad(labels,12,0);
        labels.addView(Ui.text(this,title,16,Ui.INK,true));
        labels.addView(Ui.text(this,subtitle,12,Ui.MUTED,false));
        row.addView(labels,new LinearLayout.LayoutParams(0,-2,1));
        Ui.clickable(labels,Color.TRANSPARENT,0); labels.setOnClickListener(v -> openChat(c));
        return row;
    }

    private void recent(Contact c, String date, boolean video) {
        LinearLayout row = contactRow(c,c.name,date);
        Ui.action(row,video ? R.drawable.ic_video : R.drawable.ic_phone,
                (video ? "Videollamar a " : "Llamar a ") + c.name,() -> call(c,video));
        content.addView(row);
    }

    private void chooseFavorites() {
        List<Contact> people = new ArrayList<>();
        for (Contact c : store.contacts()) if (!c.group) people.add(c);
        String[] names = new String[people.size()]; boolean[] checked = new boolean[people.size()];
        for (int i = 0; i < people.size(); i++) { names[i] = people.get(i).name; checked[i] = people.get(i).favorite; }
        new AlertDialog.Builder(this).setTitle("Tus favoritos")
                .setMultiChoiceItems(names,checked,(dialog,which,value) -> checked[which] = value)
                .setNegativeButton("Cancelar",null)
                .setPositiveButton("Guardar",(dialog,which) -> {
                    for (int i = 0; i < people.size(); i++) store.favorite(people.get(i).id,checked[i]);
                    showTab();
                }).show();
    }

    private void call(Contact c, boolean video) {
        store.call(c,video);
        new AlertDialog.Builder(this).setTitle(video ? "Videollamada simulada" : "Llamada simulada")
                .setMessage(c.name + "\n\nEsta demostración registra la llamada localmente y no contacta a la otra persona.")
                .setPositiveButton("Finalizar",null).show();
        showTab();
    }

    private void updates() {
        section("Estados",null,null);
        HorizontalScrollView horizontal = new HorizontalScrollView(this); horizontal.setHorizontalScrollBarEnabled(false);
        LinearLayout stories = Ui.row(this); Ui.pad(stories,20,0); horizontal.addView(stories);
        content.addView(horizontal);
        String[] ids = {"ana","diego","sofia","valeria"};
        for (int i = 0; i < ids.length; i++) {
            Contact c = store.find(ids[i]); final int seed = i;
            FrameLayout card = new FrameLayout(this); card.setBackground(Ui.rounded(Ui.GREEN,18,this)); card.setClipToOutline(true);
            ImageView background = new ImageView(this); background.setImageBitmap(Ui.landscape(i));
            background.setScaleType(ImageView.ScaleType.CENTER_CROP); background.setContentDescription("Paisaje del estado de " + c.name);
            card.addView(background,new FrameLayout.LayoutParams(-1,-1));
            CircleImageView avatar = Ui.avatar(this,c,44);
            avatar.setBorderWidth(Ui.dp(this,3)); avatar.setBorderColor(store.flag("seen_" + c.id) ? Color.WHITE : 0xFF25D366);
            FrameLayout.LayoutParams av = new FrameLayout.LayoutParams(Ui.dp(this,44),Ui.dp(this,44));
            av.setMargins(Ui.dp(this,10),Ui.dp(this,10),0,0); card.addView(avatar,av);
            TextView name = Ui.text(this,c.name.split(" ")[0],14,Color.WHITE,true); Ui.pad(name,12,14);
            FrameLayout.LayoutParams label = new FrameLayout.LayoutParams(-1,-2,Gravity.BOTTOM); card.addView(name,label);
            LinearLayout.LayoutParams lp = Ui.size(this,116,174); lp.setMarginEnd(Ui.dp(this,10)); stories.addView(card,lp);
            card.setContentDescription("Ver estado de " + c.name); card.setFocusable(true);
            card.setOnClickListener(v -> {
                store.flag("seen_" + c.id,true);
                LinearLayout body = Ui.column(this); Ui.pad(body,20,12);
                ImageView picture = new ImageView(this); picture.setImageBitmap(Ui.landscape(seed));
                picture.setScaleType(ImageView.ScaleType.CENTER_CROP); body.addView(picture,Ui.size(this,-1,300));
                body.addView(Ui.text(this,"Un momento para disfrutar de lo simple 🌿",16,Ui.INK,false));
                new AlertDialog.Builder(this).setTitle(c.name).setView(body).setPositiveButton("Cerrar",null).show();
                showTab();
            });
        }
        section("Canales",null,null);
        note("Sigue de cerca lo que más te interesa.");
        channelRow(channels[0],true);
        for (int i = 1; i < channels.length; i++)
            if (store.flag("follow_" + channels[i][0])) channelRow(channels[i],true);
        section("Busca canales para seguir",null,null);
        int count = 0;
        for (int i = 1; i < channels.length; i++) {
            if (!store.flag("follow_" + channels[i][0]) && (expandedChannels || count < 2)) {
                channelRow(channels[i],false); count++;
            }
        }
        if (count == 0) note("Ya sigues todos los canales disponibles.");
        if (!expandedChannels) {
            TextView explore = Ui.pill(this,"Explorar más",true);
            LinearLayout.LayoutParams lp = Ui.size(this,-2,-2); lp.setMargins(Ui.dp(this,20),Ui.dp(this,16),0,0);
            content.addView(explore,lp); explore.setOnClickListener(v -> { expandedChannels = true; showTab(); });
        }
    }

    private void channelRow(String[] data, boolean followed) {
        Contact identity = new Contact(data[0],data[1],"","","",0xFF669786,0,false,false);
        LinearLayout row = Ui.row(this); Ui.pad(row,20,12); row.addView(Ui.avatar(this,identity,50));
        LinearLayout labels = Ui.column(this); Ui.pad(labels,12,0);
        labels.addView(Ui.text(this,data[1],16,Ui.INK,true));
        labels.addView(Ui.text(this,followed ? data[2] : data[5],13,Ui.MUTED,false));
        row.addView(labels,new LinearLayout.LayoutParams(0,-2,1));
        if (followed) {
            LinearLayout right = Ui.column(this); right.setGravity(Gravity.END);
            right.addView(Ui.text(this,data[3],11,Ui.MUTED,false));
            if (!store.flag("channel_read_" + data[0])) {
                TextView badge = Ui.text(this,data[4],12,Color.WHITE,true); Ui.pad(badge,8,3);
                badge.setBackground(Ui.rounded(Ui.GREEN,20,this)); right.addView(badge);
            }
            row.addView(right); Ui.clickable(row,Color.WHITE,0);
            row.setOnClickListener(v -> {
                store.flag("channel_read_" + data[0],true);
                new AlertDialog.Builder(this).setTitle(data[1])
                        .setMessage(data[2] + "\n\n" + data[5] + "\n\nPublicación de ejemplo del canal.")
                        .setPositiveButton("Cerrar",null).show(); showTab();
            });
        } else {
            TextView follow = Ui.pill(this,"Seguir",true); row.addView(follow);
            follow.setContentDescription("Seguir " + data[1]);
            follow.setOnClickListener(v -> { store.flag("follow_" + data[0],true); showTab(); });
        }
        content.addView(row);
    }

    private void note(String message) {
        TextView t = Ui.text(this,message,13,Ui.MUTED,false); Ui.pad(t,20,8); content.addView(t);
    }
}
