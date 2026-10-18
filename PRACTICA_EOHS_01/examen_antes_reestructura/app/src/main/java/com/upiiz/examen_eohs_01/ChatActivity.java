package com.upiiz.examen_eohs_01;


import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import com.google.android.material.appbar.MaterialToolbar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends BaseActivity {
    public static final String USER_ID = "user_id";
    private List<Message> messages;
    private MessageAdapter adapter;
    private ListView list;
    private EditText input;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        User user = DemoData.findUser(getIntent().getIntExtra(USER_ID, -1));
        if (user == null) { finish(); return; }
        showScreen(R.layout.activity_chat);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(user.name);
        toolbar.setNavigationOnClickListener(v -> finish());
        messages = DemoData.messages(user);
        if (state != null) {
            ArrayList<String> texts = state.getStringArrayList("sent_texts");
            ArrayList<String> times = state.getStringArrayList("sent_times");
            if (texts != null && times != null) {
                for (int i = 0; i < Math.min(texts.size(), times.size()); i++)
                    messages.add(new Message(texts.get(i), times.get(i), true));
            }
        }
        list = findViewById(R.id.messages_list);
        adapter = new MessageAdapter(this, messages);
        list.setAdapter(adapter);
        input = findViewById(R.id.message_input);
        findViewById(R.id.send_button).setOnClickListener(v -> sendMessage());
        input.setOnEditorActionListener((view, action, event) -> {
            if (action == EditorInfo.IME_ACTION_SEND) { sendMessage(); return true; }
            return false;
        });
    }

    private void sendMessage() {
        String text = input.getText().toString().trim();
        if (text.isEmpty()) return;
        messages.add(new Message(text, new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()), true));
        adapter.notifyDataSetChanged();
        input.setText("");
        list.setSelection(messages.size() - 1);
    }

    @Override protected void onSaveInstanceState(Bundle state) {
        if (messages != null) {
            ArrayList<String> texts = new ArrayList<>();
            ArrayList<String> times = new ArrayList<>();
            for (int i = 20; i < messages.size(); i++) {
                texts.add(messages.get(i).text);
                times.add(messages.get(i).time);
            }
            state.putStringArrayList("sent_texts", texts);
            state.putStringArrayList("sent_times", times);
        }
        super.onSaveInstanceState(state);
    }
}
