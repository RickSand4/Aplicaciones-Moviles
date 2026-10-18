package com.upiiz.examen_eohs_01;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import com.google.android.material.appbar.MaterialToolbar;

public class UsersActivity extends BaseActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        showScreen(R.layout.activity_users);
        ((MaterialToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());
        ListView list = findViewById(R.id.users_list);
        UserAdapter adapter = new UserAdapter(this, DemoData.users());
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(ChatActivity.USER_ID, adapter.getItem(position).id);
            startActivity(intent);
        });
    }
}
