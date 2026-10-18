package com.upiiz.examen_eohs_01;


import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends BaseActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        showScreen(R.layout.activity_register);
        ((MaterialToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());
        findViewById(R.id.have_account).setOnClickListener(v -> finish());
        findViewById(R.id.register_button).setOnClickListener(v -> register());
    }

    private void register() {
        TextInputEditText name = findViewById(R.id.full_name);
        TextInputEditText password = findViewById(R.id.password);
        TextInputEditText confirmation = findViewById(R.id.confirm_password);
        TextInputLayout nameBox = findViewById(R.id.name_box);
        TextInputLayout passwordBox = findViewById(R.id.password_box);
        TextInputLayout confirmationBox = findViewById(R.id.confirm_box);
        nameBox.setError(null);
        passwordBox.setError(null);
        confirmationBox.setError(null);
        boolean valid = true;
        if (!Validation.validName(name.getText().toString())) {
            nameBox.setError(getString(R.string.invalid_name));
            name.requestFocus();
            valid = false;
        }
        if (password.getText().toString().trim().isEmpty()) {
            passwordBox.setError(getString(R.string.required_password));
            valid = false;
        }
        if (!Validation.matchingPasswords(password.getText().toString(), confirmation.getText().toString())) {
            confirmationBox.setError(getString(R.string.password_mismatch));
            valid = false;
        }
        if (valid) {
            // El examen exige volver al login, cuya única cuenta válida es admin.
            Toast.makeText(this, R.string.registration_success, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
