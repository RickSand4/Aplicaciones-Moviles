package com.upiiz.examen_eohs_01;


import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends BaseActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        showScreen(R.layout.activity_main);
        TextInputEditText username = findViewById(R.id.username);
        TextInputEditText password = findViewById(R.id.password);
        TextInputLayout usernameBox = findViewById(R.id.username_box);
        TextInputLayout passwordBox = findViewById(R.id.password_box);
        findViewById(R.id.login_button).setOnClickListener(view -> {
            usernameBox.setError(null);
            passwordBox.setError(null);
            String user = username.getText().toString();
            String pass = password.getText().toString();
            if (user.isEmpty()) {
                usernameBox.setError(getString(R.string.required_user));
                username.requestFocus();
            } else if (!Validation.validLogin(user, pass)) {
                passwordBox.setError(getString(R.string.invalid_login));
                password.requestFocus();
            } else {
                password.setText("");
                startActivity(new Intent(this, UsersActivity.class));
            }
        });
        password.setOnEditorActionListener((view, action, event) -> {
            if (action == EditorInfo.IME_ACTION_DONE) {
                findViewById(R.id.login_button).performClick();
                return true;
            }
            return false;
        });
        findViewById(R.id.create_account).setOnClickListener(view ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }
}
