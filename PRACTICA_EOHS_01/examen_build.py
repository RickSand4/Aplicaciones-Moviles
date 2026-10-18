from pathlib import Path

root = Path(__file__).parent / 'examen_work'
java = root / 'app/src/main/java/com/upiiz/examen_eohs_01'
res = root / 'app/src/main/res'

def put(path, content):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content.strip() + '\n', encoding='utf-8')

def cls(name, content):
    put(java / (name + '.java'), 'package com.upiiz.examen_eohs_01;\n\n' + content)

cls('BaseActivity', '''
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public abstract class BaseActivity extends AppCompatActivity {
    protected void showScreen(int layout) {
        EdgeToEdge.enable(this);
        setContentView(layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(findViewById(R.id.main));
    }
}
''')

cls('Validation', '''
/** Reglas del examen, independientes de las pantallas para poder verificarlas. */
public final class Validation {
    private Validation() { }

    public static boolean validLogin(String username, String password) {
        return "admin".equals(username) && "123456".equals(password);
    }

    public static boolean validName(String name) {
        if (name == null) return false;
        String clean = name.trim();
        return clean.codePointCount(0, clean.length()) >= 3;
    }

    public static boolean matchingPasswords(String password, String confirmation) {
        return password != null && !password.trim().isEmpty() && password.equals(confirmation);
    }
}
''')

cls('MainActivity', '''
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
''')

cls('RegisterActivity', '''
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
''')

cls('User', '''
public final class User {
    public final int id;
    public final String name;
    public final String username;
    public final int color;
    public User(int id, String name, String username, int color) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.color = color;
    }
    public String initials() {
        String[] words = name.split(" ");
        return words[0].substring(0, 1) + words[words.length - 1].substring(0, 1);
    }
}
''')
cls('Message', '''
public final class Message {
    public final String text;
    public final String time;
    public final boolean outgoing;
    public Message(String text, String time, boolean outgoing) {
        this.text = text;
        this.time = time;
        this.outgoing = outgoing;
    }
}
''')
cls('DemoData', '''
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class DemoData {
    private DemoData() { }
    public static List<User> users() {
        String[] names = {"Ana García", "Luis Martínez", "Marta López", "Pedro Sánchez",
                "Sofía Torres", "Juan Rodríguez", "Laura Hernández", "Carlos Ramírez",
                "Valeria Flores", "Diego Gómez", "Camila Pérez", "Miguel Díaz",
                "Lucía Romero", "Jorge Castro", "Elena Vargas", "Andrés Mendoza",
                "Daniela Ruiz", "Fernando Ortiz", "Mariana Silva", "Ricardo Reyes"};
        String[] handles = {"ana123", "luism", "marta_22", "pedro99", "sofia_t", "juanr",
                "laura_h", "carlos_r", "vale_f", "diego_g", "camila_p", "miguel_d",
                "lucia_r", "jorge_c", "elena_v", "andres_m", "dani_ruiz", "fer_ortiz",
                "mariana_s", "ricardo_r"};
        int[] colors = {0xff1d64d8, 0xff7c49b8, 0xff008577, 0xffc06524, 0xffb83d68};
        List<User> users = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            users.add(new User(i + 1, names[i], handles[i], colors[i % colors.length]));
        }
        return users;
    }

    public static User findUser(int id) {
        for (User user : users()) if (user.id == id) return user;
        return null;
    }

    public static List<Message> messages(User user) {
        String firstName = user.name.split(" ")[0];
        String[] texts = {"Hola " + firstName + ", ¿cómo estás?", "¡Hola! Muy bien, ¿y tú?",
                "Todo bien, ¿quieres vernos más tarde?", "Sí, me parece bien. ¿A qué hora?",
                "¿Te parece a las 5?", "Perfecto, nos vemos entonces.",
                "¿Nos encontramos en la biblioteca?", "Sí, en la entrada principal.",
                "Llevaré mis apuntes de aplicaciones móviles.", "¡Genial! Yo llevo mi computadora.",
                "Podemos repasar los ejercicios juntos.", "Me gustaría practicar las listas personalizadas.",
                "También revisamos la validación del login.", "Buena idea, tengo unas dudas del registro.",
                "Las resolvemos paso a paso.", "Gracias, así terminamos el repaso.",
                "¿Llevamos algo de tomar?", "Yo llevo agua para los dos.",
                "¡Perfecto! Te aviso cuando llegue.", "De acuerdo, ¡nos vemos al rato!"};
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < texts.length; i++) {
            messages.add(new Message(texts[i], String.format(Locale.ROOT, "09:%02d", 32 + i), i % 2 == 0));
        }
        return messages;
    }
}
''')

cls('UserAdapter', '''
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
''')
cls('UsersActivity', '''
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
''')
cls('MessageAdapter', '''
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
''')
cls('ChatActivity', '''
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
''')

put(res / 'values/colors.xml', '''
<resources>
    <color name="primary">#1762CE</color>
    <color name="primary_dark">#124CA3</color>
    <color name="surface">#FFFFFF</color>
    <color name="background">#F6F8FC</color>
    <color name="text_primary">#172B46</color>
    <color name="text_secondary">#52647A</color>
    <color name="divider">#E7EDF5</color>
</resources>
''')
theme = '''
<resources>
    <style name="Base.Theme.EXAMEN_EOHS_01" parent="Theme.Material3.Light.NoActionBar">
        <item name="colorPrimary">@color/primary</item>
        <item name="colorOnPrimary">@color/surface</item>
        <item name="colorSecondary">@color/primary</item>
        <item name="android:colorBackground">@color/background</item>
        <item name="colorSurface">@color/surface</item>
        <item name="colorOnSurface">@color/text_primary</item>
        <item name="android:fontFamily">sans</item>
        <item name="android:windowLightStatusBar">true</item>
        <item name="android:windowLightNavigationBar">true</item>
        <item name="android:windowActionModeOverlay">true</item>
    </style>
    <style name="Theme.EXAMEN_EOHS_01" parent="Base.Theme.EXAMEN_EOHS_01" />
</resources>
'''
put(res / 'values/themes.xml', theme)
put(res / 'values-night/themes.xml', theme)
put(res / 'values/strings.xml', '''
<resources>
    <string name="app_name">ChatApp</string>
    <string name="tagline">Conecta con tus amigos</string>
    <string name="welcome">¡Qué gusto verte!</string>
    <string name="login_subtitle">Inicia sesión para seguir conversando.</string>
    <string name="username">Usuario</string>
    <string name="password">Contraseña</string>
    <string name="login">Iniciar sesión</string>
    <string name="create_account">Crear una cuenta</string>
    <string name="new_here">¿Aún no tienes una cuenta?</string>
    <string name="register">Registro</string>
    <string name="register_heading">Crea tu cuenta</string>
    <string name="register_subtitle">Completa tus datos para comenzar.</string>
    <string name="full_name">Nombre completo</string>
    <string name="email">Correo electrónico</string>
    <string name="confirm_password">Confirmar contraseña</string>
    <string name="register_button">Registrarse</string>
    <string name="have_account">¿Ya tienes una cuenta? Inicia sesión</string>
    <string name="required_user">Escribe tu usuario.</string>
    <string name="invalid_login">Usuario o contraseña incorrectos.</string>
    <string name="invalid_name">El nombre debe tener al menos 3 caracteres.</string>
    <string name="required_password">Escribe una contraseña.</string>
    <string name="password_mismatch">Las contraseñas deben coincidir y no estar vacías.</string>
    <string name="registration_success">Registro validado. Inicia sesión con admin y la clave 123456.</string>
    <string name="users">Usuarios</string>
    <string name="users_caption">20 usuarios · Selecciona una conversación</string>
    <string name="back">Volver</string>
    <string name="logout">Volver al inicio de sesión</string>
    <string name="online">En línea</string>
    <string name="conversation_date">HOY</string>
    <string name="message_hint">Escribe un mensaje…</string>
    <string name="send">Enviar mensaje</string>
    <string name="chevron">›</string>
</resources>
''')
put(res / 'drawable/ic_back.xml', '''
<vector xmlns:android="http://schemas.android.com/apk/res/android" android:width="24dp" android:height="24dp" android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#FFFFFF" android:pathData="M20,11H7.83l5.59,-5.59L12,4l-8,8 8,8 1.42,-1.41L7.83,13H20z" />
</vector>
''')
put(res / 'drawable/ic_chat.xml', '''
<vector xmlns:android="http://schemas.android.com/apk/res/android" android:width="64dp" android:height="64dp" android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#FFFFFF" android:pathData="M20,2H4a2,2 0,0 0,-2 2v18l4,-4h14a2,2 0,0 0,2 -2V4a2,2 0,0 0,-2 -2z" />
    <path android:fillColor="#1762CE" android:pathData="M6,9h2v2H6zM11,9h2v2h-2zM16,9h2v2h-2z" />
</vector>
''')
put(res / 'drawable/ic_send.xml', '''
<vector xmlns:android="http://schemas.android.com/apk/res/android" android:width="24dp" android:height="24dp" android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#FFFFFF" android:pathData="M2,21l21,-9L2,3v7l15,2 -15,2z" />
</vector>
''')
for name, color in [('bubble_in','#E9EEF5'), ('bubble_out','#1762CE'), ('input_background','#FFFFFF')]:
    put(res / f'drawable/{name}.xml', f'''<shape xmlns:android="http://schemas.android.com/apk/res/android">
        <solid android:color="{color}"/><corners android:radius="16dp"/>
    </shape>''')

ns = 'xmlns:android="http://schemas.android.com/apk/res/android" xmlns:app="http://schemas.android.com/apk/res-auto"'
def screen(body):
    return f'''<LinearLayout {ns} android:id="@+id/main" android:layout_width="match_parent" android:layout_height="match_parent" android:orientation="vertical" android:background="@color/background">{body}</LinearLayout>'''
def toolbar(title, subtitle='', desc='back'):
    sub = f'app:subtitle="@string/{subtitle}" app:subtitleTextColor="#E0ECFF"' if subtitle else ''
    return f'''<com.google.android.material.appbar.MaterialToolbar android:id="@+id/toolbar" android:layout_width="match_parent" android:layout_height="?attr/actionBarSize" android:background="@color/primary" app:title="@string/{title}" app:titleTextColor="@color/surface" app:navigationIcon="@drawable/ic_back" app:navigationContentDescription="@string/{desc}" {sub}/>'''
def field(box, id, hint, inputtype='text', last=False):
    toggle = 'app:endIconMode="password_toggle"' if inputtype == 'textPassword' else ''
    return f'''<com.google.android.material.textfield.TextInputLayout android:id="@+id/{box}" style="@style/Widget.Material3.TextInputLayout.OutlinedBox" android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginBottom="12dp" android:hint="@string/{hint}" {toggle}>
      <com.google.android.material.textfield.TextInputEditText android:id="@+id/{id}" android:layout_width="match_parent" android:layout_height="wrap_content" android:inputType="{inputtype}" android:maxLines="1" android:singleLine="true" android:imeOptions="{'actionDone' if last else 'actionNext'}" android:textSize="16sp"/>
    </com.google.android.material.textfield.TextInputLayout>'''
def button(id, text, outlined=False):
    style = 'style="@style/Widget.Material3.Button.OutlinedButton"' if outlined else ''
    return f'''<com.google.android.material.button.MaterialButton {style} android:id="@+id/{id}" android:layout_width="match_parent" android:layout_height="wrap_content" android:minHeight="54dp" android:text="@string/{text}" android:textSize="15sp" app:cornerRadius="10dp"/>'''
def heading(title, subtitle):
    return f'''<TextView android:layout_width="match_parent" android:layout_height="wrap_content" android:text="@string/{title}" android:textColor="@color/text_primary" android:textSize="26sp" android:textStyle="bold"/>
    <TextView android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginTop="6dp" android:layout_marginBottom="24dp" android:text="@string/{subtitle}" android:textColor="@color/text_secondary" android:textSize="14sp"/>'''
put(res / 'layout/activity_main.xml', screen(f'''
    <ScrollView android:layout_width="match_parent" android:layout_height="match_parent" android:fillViewport="true">
      <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical">
        <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:gravity="center" android:padding="28dp" android:background="@color/primary">
          <ImageView android:layout_width="60dp" android:layout_height="60dp" android:src="@drawable/ic_chat" android:importantForAccessibility="no"/>
          <TextView android:layout_width="wrap_content" android:layout_height="wrap_content" android:layout_marginTop="12dp" android:text="@string/app_name" android:textSize="30sp" android:textStyle="bold" android:textColor="@color/surface"/>
          <TextView android:layout_width="wrap_content" android:layout_height="wrap_content" android:layout_marginTop="6dp" android:text="@string/tagline" android:textSize="15sp" android:textColor="#E0ECFF"/>
        </LinearLayout>
        <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:padding="24dp">
          {heading('welcome','login_subtitle')}
          {field('username_box','username','username')}
          {field('password_box','password','password','textPassword',True)}
          {button('login_button','login')}
          <TextView android:layout_width="match_parent" android:layout_height="wrap_content" android:gravity="center" android:layout_marginTop="24dp" android:layout_marginBottom="8dp" android:text="@string/new_here" android:textColor="@color/text_secondary"/>
          {button('create_account','create_account',True)}
        </LinearLayout>
      </LinearLayout>
    </ScrollView>'''))
put(res / 'layout/activity_register.xml', screen(f'''
    {toolbar('register')}
    <ScrollView android:layout_width="match_parent" android:layout_height="0dp" android:layout_weight="1" android:fillViewport="true">
      <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:padding="24dp">
        {heading('register_heading','register_subtitle')}
        {field('name_box','full_name','full_name','textPersonName')}
        {field('email_box','email','email','textEmailAddress')}
        {field('username_box','username','username')}
        {field('password_box','password','password','textPassword')}
        {field('confirm_box','confirm_password','confirm_password','textPassword',True)}
        {button('register_button','register_button')}
        <com.google.android.material.button.MaterialButton style="@style/Widget.Material3.Button.TextButton" android:id="@+id/have_account" android:layout_width="match_parent" android:layout_height="wrap_content" android:minHeight="48dp" android:layout_marginTop="12dp" android:text="@string/have_account"/>
      </LinearLayout>
    </ScrollView>'''))
put(res / 'layout/activity_users.xml', screen(f'''
    {toolbar('users',desc='logout')}
    <TextView android:layout_width="match_parent" android:layout_height="wrap_content" android:padding="16dp" android:text="@string/users_caption" android:textColor="@color/text_secondary" android:textSize="13sp"/>
    <ListView android:id="@+id/users_list" android:layout_width="match_parent" android:layout_height="0dp" android:layout_weight="1" android:background="@color/surface" android:divider="@color/divider" android:dividerHeight="1dp"/>
'''))
put(res / 'layout/row_user.xml', f'''
<LinearLayout {ns} android:layout_width="match_parent" android:layout_height="wrap_content" android:minHeight="82dp" android:orientation="horizontal" android:gravity="center_vertical" android:paddingHorizontal="20dp" android:paddingVertical="14dp" android:background="?attr/selectableItemBackground">
    <TextView android:id="@+id/avatar" android:layout_width="48dp" android:layout_height="48dp" android:gravity="center" android:textSize="17sp" android:textStyle="bold" android:textColor="@color/surface" android:importantForAccessibility="no"/>
    <LinearLayout android:layout_width="0dp" android:layout_height="wrap_content" android:layout_weight="1" android:layout_marginStart="16dp" android:orientation="vertical">
      <TextView android:id="@+id/user_name" android:layout_width="match_parent" android:layout_height="wrap_content" android:textSize="16sp" android:textStyle="bold" android:textColor="@color/text_primary"/>
      <TextView android:id="@+id/user_handle" android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginTop="4dp" android:textSize="13sp" android:textColor="@color/text_secondary"/>
    </LinearLayout>
    <TextView android:layout_width="24dp" android:layout_height="wrap_content" android:gravity="end" android:text="@string/chevron" android:textSize="28sp" android:textColor="@color/text_secondary" android:importantForAccessibility="no"/>
</LinearLayout>''')
put(res / 'layout/activity_chat.xml', screen(f'''
    {toolbar('app_name','online')}
    <TextView android:layout_width="match_parent" android:layout_height="wrap_content" android:padding="12dp" android:gravity="center" android:text="@string/conversation_date" android:textColor="@color/text_secondary" android:textSize="11sp" android:letterSpacing="0.12"/>
    <ListView android:id="@+id/messages_list" android:layout_width="match_parent" android:layout_height="0dp" android:layout_weight="1" android:divider="@null" android:dividerHeight="0dp" android:paddingHorizontal="12dp" android:clipToPadding="false" android:listSelector="@android:color/transparent" android:transcriptMode="normal"/>
    <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="horizontal" android:gravity="center_vertical" android:padding="12dp" android:background="@color/surface">
      <EditText android:id="@+id/message_input" android:layout_width="0dp" android:layout_height="wrap_content" android:layout_weight="1" android:minHeight="48dp" android:paddingHorizontal="16dp" android:background="@drawable/bubble_in" android:hint="@string/message_hint" android:textColor="@color/text_primary" android:textColorHint="@color/text_secondary" android:textSize="15sp" android:inputType="textCapSentences" android:maxLines="1" android:singleLine="true" android:imeOptions="actionSend" android:importantForAutofill="no"/>
      <com.google.android.material.button.MaterialButton android:id="@+id/send_button" android:layout_width="52dp" android:layout_height="52dp" android:layout_marginStart="8dp" android:padding="0dp" android:insetTop="0dp" android:insetBottom="0dp" android:contentDescription="@string/send" app:cornerRadius="16dp" app:icon="@drawable/ic_send" app:iconGravity="textStart" app:iconPadding="0dp"/>
    </LinearLayout>
'''))
for outgoing in [False, True]:
    side, color, timecolor, bg, suffix = ('end', '#FFFFFF','#DFEBFF','bubble_out','out') if outgoing else ('start','#172B46','#52647A','bubble_in','in')
    margin = 'android:layout_marginStart="48dp"' if outgoing else 'android:layout_marginEnd="48dp"'
    put(res / f'layout/row_message_{suffix}.xml', f'''
<LinearLayout {ns} android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:gravity="{side}" android:paddingVertical="5dp">
  <LinearLayout android:layout_width="wrap_content" android:layout_height="wrap_content" {margin} android:orientation="vertical" android:padding="12dp" android:background="@drawable/{bg}">
    <TextView android:id="@+id/message_text" android:layout_width="wrap_content" android:layout_height="wrap_content" android:maxWidth="280dp" android:textColor="{color}" android:textSize="15sp"/>
    <TextView android:id="@+id/message_time" android:layout_width="wrap_content" android:layout_height="wrap_content" android:layout_gravity="end" android:layout_marginTop="5dp" android:textColor="{timecolor}" android:textSize="11sp"/>
  </LinearLayout>
</LinearLayout>''')

manifest = root / 'app/src/main/AndroidManifest.xml'
text = manifest.read_text(encoding='utf-8')
text = text.replace('        <activity', '''        <activity android:name=".RegisterActivity" android:exported="false" android:windowSoftInputMode="adjustResize" />
        <activity android:name=".UsersActivity" android:exported="false" />
        <activity android:name=".ChatActivity" android:exported="false" android:windowSoftInputMode="adjustResize" />
        <activity''', 1)
put(manifest, text)
gradle = root / 'app/build.gradle.kts'
put(gradle, gradle.read_text(encoding='utf-8').replace('    implementation(libs.circleimageview)\n', ''))

put(root / 'app/src/test/java/com/upiiz/examen_eohs_01/ExamRequirementsTest.java', '''
package com.upiiz.examen_eohs_01;
import org.junit.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.*;

public class ExamRequirementsTest {
    @Test public void onlyExamCredentialsAreAccepted() {
        assertTrue(Validation.validLogin("admin", "123456"));
        assertFalse(Validation.validLogin("Admin", "123456"));
        assertFalse(Validation.validLogin("admin", "123456 "));
        assertFalse(Validation.validLogin("other", "123456"));
        assertFalse(Validation.validLogin("admin", "wrong"));
        assertFalse(Validation.validLogin("", ""));
        assertFalse(Validation.validLogin(null, null));
    }
    @Test public void nameMustHaveThreeRealCharacters() {
        assertFalse(Validation.validName("  "));
        assertFalse(Validation.validName(" Al "));
        assertTrue(Validation.validName("Ana"));
        assertTrue(Validation.validName(" José "));
        assertFalse(Validation.validName(null));
    }
    @Test public void passwordsMustMatchAndNotBeBlank() {
        assertTrue(Validation.matchingPasswords("secreto", "secreto"));
        assertFalse(Validation.matchingPasswords("secreto", "Secreto"));
        assertFalse(Validation.matchingPasswords("", ""));
        assertFalse(Validation.matchingPasswords("   ", "   "));
        assertFalse(Validation.matchingPasswords(null, null));
    }
    @Test public void twentyDistinctUsersCanBeSelected() {
        List<User> users = DemoData.users();
        assertEquals(20, users.size());
        Set<Integer> ids = new HashSet<>();
        Set<String> handles = new HashSet<>();
        for (User user : users) {
            assertTrue(ids.add(user.id));
            assertTrue(handles.add(user.username));
            assertEquals(user.name, DemoData.findUser(user.id).name);
        }
        assertNull(DemoData.findUser(-1));
    }
    @Test public void everyChatStartsWithTwentyAlternatingMessages() {
        for (User user : DemoData.users()) {
            List<Message> messages = DemoData.messages(user);
            assertEquals(20, messages.size());
            assertTrue(messages.get(0).text.contains(user.name.split(" ")[0]));
            for (int i = 0; i < messages.size(); i++) {
                assertEquals(i % 2 == 0, messages.get(i).outgoing);
                assertFalse(messages.get(i).text.isEmpty());
            }
            messages.remove(0);
            assertEquals(20, DemoData.messages(user).size());
        }
    }
}
''')
put(root / 'README.md', '''
# EXAMEN_EOHS_01 · ChatApp

Aplicación Android en Java y XML conforme a **Examen 1.pdf**.

## Ejecutar

Abrir esta carpeta en Android Studio, sincronizar Gradle y ejecutar `app` en un dispositivo o emulador (Android 8.0 / API 26 o posterior). El proyecto conserva su configuración original de SDK 37 y JDK 25.

**Acceso:** usuario `admin`, contraseña `123456`.

## Requisitos implementados

| Valor | Requisito | Implementación |
| --- | --- | --- |
| 25% | Login, validar admin / 123456, abrir listado o registro | `MainActivity`, `Validation` |
| 25% | Nombre de al menos 3 caracteres, contraseñas coincidentes, volver al login | `RegisterActivity`, `Validation` |
| 20% | Mostrar 20 usuarios en CustomListView y abrir chat al tocar | `UsersActivity`, `UserAdapter`, `row_user.xml` |
| 30% | 20 mensajes guardados en List, CustomListView, izquierda y derecha | `ChatActivity`, `MessageAdapter`, `row_message_in.xml`, `row_message_out.xml` |

CustomListView se implementa con `ListView` y adaptadores propios derivados de `BaseAdapter`. Los 20 usuarios y las 20 intervenciones iniciales por conversación están en `DemoData`. Cada chat muestra el nombre elegido. También permite enviar mensajes locales y conserva los nuevos mensajes al girar la pantalla.

El registro es una simulación: valida los datos y vuelve al login. No crea una cuenta en un servidor ni sustituye las credenciales fijas exigidas por el examen. Correo y usuario se muestran como en la referencia; las reglas de aprobación son el nombre y las contraseñas. No se necesitan permisos de Internet. Al salir del chat, los mensajes adicionales no se guardan en disco.

## Comprobación

```powershell
.\\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

1. Intentar ingresar con valores vacíos y credenciales incorrectas; permanecer en login con error.
2. Abrir Crear una cuenta; probar nombre de 2 caracteres y contraseñas diferentes.
3. Usar nombre de 3 o más caracteres y contraseñas iguales no vacías; volver al login. Probar también Ya tienes una cuenta y la flecha de regreso.
4. Ingresar con `admin` / `123456`; recorrer los 20 usuarios hasta Ricardo Reyes.
5. Abrir distintos usuarios; verificar nombre y 20 mensajes alternados, desplazándose por toda la conversación.
6. Enviar un mensaje, girar el dispositivo y comprobar que se conserva. Volver al listado con la flecha.

El APK se genera en `app/build/outputs/apk/debug/app-debug.apk`.
''')
print('Proyecto generado en', root)
