from pathlib import Path
from xml.dom import minidom
import json
import re

root = Path(__file__).parent / 'examen_reestructura'
src = root / 'app/src/main/java'
package = src / 'com/upiiz/examen_eohs_01'
res = root / 'app/src/main/res'
old_data = (package / 'DemoData.java').read_text(encoding='utf-8')

def write(path, text):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(text.strip() + '\n', encoding='utf-8')

def activity(name, imports, interfaces, variables, layout, setup, methods):
    write(package / (name + '.java'), f'''package com.upiiz.examen_eohs_01;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

{imports}

public class {name} extends AppCompatActivity implements {interfaces} {{
    //1. Declarar variables
{variables}

    @Override
    protected void onCreate(Bundle savedInstanceState) {{
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.{layout});
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {{
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        }});

{setup}
    }}

{methods}
}}
''')

activity('MainActivity', '''import android.content.Intent;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;''',
'View.OnClickListener, TextView.OnEditorActionListener', '''    EditText etUsuario, etClave;
    Button btnIngresar, btnCrearCuenta;
    TextInputLayout tilUsuario, tilClave;''', 'activity_main', '''        //2. Enlazar variables con las vistas
        etUsuario = findViewById(R.id.etUsuario);
        etClave = findViewById(R.id.etClave);
        tilUsuario = findViewById(R.id.tilUsuario);
        tilClave = findViewById(R.id.tilClave);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        //3. Escuchar los clicks
        btnIngresar.setOnClickListener(this);
        btnCrearCuenta.setOnClickListener(this);
        etClave.setOnEditorActionListener(this);''', '''    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnIngresar) {
            validarUsuario();
        } else if (id == R.id.btnCrearCuenta) {
            abrirRegistro();
        }
    }

    public void validarUsuario() {
        String usuario = etUsuario.getText().toString();
        String clave = etClave.getText().toString();
        tilUsuario.setError(null);
        tilClave.setError(null);

        if (usuario.isEmpty()) {
            tilUsuario.setError(getString(R.string.required_user));
            etUsuario.requestFocus();
        } else if (usuario.equals("admin") && clave.equals("123456")) {
            etClave.setText("");
            Intent intent = new Intent(this, CustomListViewActivity.class);
            startActivity(intent);
        } else {
            tilClave.setError(getString(R.string.invalid_login));
            etClave.requestFocus();
        }
    }

    public void abrirRegistro() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            validarUsuario();
            return true;
        }
        return false;
    }''')

activity('RegistroActivity', '''import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;''', 'View.OnClickListener',
'''    EditText etNombre, etCorreo, etUsuario, etClave, etConfirmarClave;
    Button btnRegistrar, btnTengoCuenta;
    TextInputLayout tilNombre, tilClave, tilConfirmarClave;
    MaterialToolbar toolbar;''', 'activity_registro', '''        //2. Enlazar variables con las vistas
        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etUsuario = findViewById(R.id.etUsuario);
        etClave = findViewById(R.id.etClave);
        etConfirmarClave = findViewById(R.id.etConfirmarClave);
        tilNombre = findViewById(R.id.tilNombre);
        tilClave = findViewById(R.id.tilClave);
        tilConfirmarClave = findViewById(R.id.tilConfirmarClave);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnTengoCuenta = findViewById(R.id.btnTengoCuenta);
        toolbar = findViewById(R.id.toolbar);

        //3. Escuchar los clicks
        btnRegistrar.setOnClickListener(this);
        btnTengoCuenta.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(this);''', '''    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegistrar) {
            registrarUsuario();
        } else {
            regresar();
        }
    }

    public void registrarUsuario() {
        String nombre = etNombre.getText().toString().trim();
        String clave = etClave.getText().toString();
        String confirmarClave = etConfirmarClave.getText().toString();
        boolean datosCorrectos = true;

        tilNombre.setError(null);
        tilClave.setError(null);
        tilConfirmarClave.setError(null);

        if (nombre.length() < 3) {
            tilNombre.setError(getString(R.string.invalid_name));
            etNombre.requestFocus();
            datosCorrectos = false;
        }
        if (clave.trim().isEmpty()) {
            tilClave.setError(getString(R.string.required_password));
            datosCorrectos = false;
        }
        if (!clave.equals(confirmarClave) || confirmarClave.trim().isEmpty()) {
            tilConfirmarClave.setError(getString(R.string.password_mismatch));
            datosCorrectos = false;
        }

        if (datosCorrectos) {
            //El registro es simulado. El acceso del examen sigue siendo admin / 123456.
            Toast.makeText(this, R.string.registration_success, Toast.LENGTH_LONG).show();
            regresar();
        }
    }

    public void regresar() {
        //Cerrar el registro para volver al login que lo abrió.
        finish();
    }''')

names = json.loads('[' + re.search(r'String\[\] names = \{(.*?)\};', old_data, re.S).group(1) + ']')
handles = json.loads('[' + re.search(r'String\[\] handles = \{(.*?)\};', old_data, re.S).group(1) + ']')
add_users = '\n'.join(f'        listaUsuarios.add(new User({i+1}L, "{name}", "{handle}", R.drawable.usuario));'
                       for i, (name, handle) in enumerate(zip(names, handles)))
activity('CustomListViewActivity', '''import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.appbar.MaterialToolbar;
import com.upiiz.examen_eohs_01.adapters.CustomAdapter;

import java.util.ArrayList;

import model.User;''', 'View.OnClickListener, AdapterView.OnItemClickListener',
'''    ListView lvUsuarios;
    ArrayList<User> listaUsuarios;
    CustomAdapter adapter;
    MaterialToolbar toolbar;''', 'activity_custom_list_view', '''        //2. Enlazar variables con las vistas
        lvUsuarios = findViewById(R.id.lvUsuarios);
        toolbar = findViewById(R.id.toolbar);

        //3. Cargar los datos y crear el adaptador
        cargarUsuarios();
        adapter = new CustomAdapter(this, listaUsuarios);
        lvUsuarios.setAdapter(adapter);

        //4. Escuchar los clicks
        lvUsuarios.setOnItemClickListener(this);
        toolbar.setNavigationOnClickListener(this);''', f'''    public void cargarUsuarios() {{
        //Datos en memoria
        listaUsuarios = new ArrayList<>();
{add_users}
    }}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {{
        User usuario = listaUsuarios.get(position);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("id_usuario", usuario.getId());
        intent.putExtra("nombre_usuario", usuario.getNombre());
        startActivity(intent);
    }}

    @Override
    public void onClick(View v) {{
        regresar();
    }}

    public void regresar() {{
        finish();
    }}''')

messages = [
    '"Hola " + nombreUsuario + ", ¿cómo estás?"', '"¡Hola! Muy bien, ¿y tú?"',
    '"Todo bien, ¿quieres vernos más tarde?"', '"Sí, me parece bien. ¿A qué hora?"',
    '"¿Te parece a las 5?"', '"Perfecto, nos vemos entonces."',
    '"¿Nos encontramos en la biblioteca?"', '"Sí, en la entrada principal."',
    '"Llevaré mis apuntes de aplicaciones móviles."', '"¡Genial! Yo llevo mi computadora."',
    '"Podemos repasar los ejercicios juntos."', '"Me gustaría practicar las listas personalizadas."',
    '"También revisamos la validación del login."', '"Buena idea, tengo unas dudas del registro."',
    '"Las resolvemos paso a paso."', '"Gracias, así terminamos el repaso."',
    '"¿Llevamos algo de tomar?"', '"Yo llevo agua para los dos."',
    '"¡Perfecto! Te aviso cuando llegue."', '"De acuerdo, ¡nos vemos al rato!"'
]
add_messages = '\n'.join(f'        listaMensajes.add(new Mensaje({text}, "09:{32+i}", {str(i%2 == 0).lower()}));'
                          for i, text in enumerate(messages))
activity('ChatActivity', '''import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.upiiz.examen_eohs_01.adapters.MensajeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import model.Mensaje;''', 'View.OnClickListener, TextView.OnEditorActionListener',
'''    ListView lvMensajes;
    ArrayList<Mensaje> listaMensajes;
    MensajeAdapter adapter;
    EditText etMensaje;
    Button btnEnviar;
    MaterialToolbar toolbar;
    long idUsuario;
    String nombreUsuario;''', 'activity_chat', '''        //2. Enlazar variables con las vistas
        lvMensajes = findViewById(R.id.lvMensajes);
        etMensaje = findViewById(R.id.etMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        toolbar = findViewById(R.id.toolbar);

        //3. Recibir el usuario seleccionado
        idUsuario = getIntent().getLongExtra("id_usuario", -1);
        nombreUsuario = getIntent().getStringExtra("nombre_usuario");
        if (idUsuario < 1 || nombreUsuario == null) {
            finish();
            return;
        }
        toolbar.setTitle(nombreUsuario);

        //4. Cargar los mensajes y crear el adaptador
        cargarMensajes();
        if (savedInstanceState != null) {
            recuperarMensajes(savedInstanceState);
        }
        adapter = new MensajeAdapter(this, listaMensajes);
        lvMensajes.setAdapter(adapter);

        //5. Escuchar los clicks
        btnEnviar.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(this);
        etMensaje.setOnEditorActionListener(this);''', f'''    public void cargarMensajes() {{
        //20 mensajes en una lista: true = enviado, false = recibido.
        listaMensajes = new ArrayList<>();
{add_messages}
    }}

    @Override
    public void onClick(View v) {{
        if (v.getId() == R.id.btnEnviar) {{
            enviarMensaje();
        }} else {{
            regresar();
        }}
    }}

    public void enviarMensaje() {{
        String texto = etMensaje.getText().toString().trim();
        if (texto.isEmpty()) {{
            return;
        }}

        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String hora = formatoHora.format(new Date());
        listaMensajes.add(new Mensaje(texto, hora, true));
        adapter.notifyDataSetChanged();
        etMensaje.setText("");
        lvMensajes.setSelection(listaMensajes.size() - 1);
    }}

    public void regresar() {{
        finish();
    }}

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {{
        if (actionId == EditorInfo.IME_ACTION_SEND) {{
            enviarMensaje();
            return true;
        }}
        return false;
    }}

    @Override
    protected void onSaveInstanceState(Bundle outState) {{
        //Guardar los mensajes nuevos cuando se gira la pantalla.
        if (listaMensajes != null) {{
            ArrayList<String> textos = new ArrayList<>();
            ArrayList<String> horas = new ArrayList<>();
            for (int i = 20; i < listaMensajes.size(); i++) {{
                textos.add(listaMensajes.get(i).getTexto());
                horas.add(listaMensajes.get(i).getHora());
            }}
            outState.putStringArrayList("textos_enviados", textos);
            outState.putStringArrayList("horas_enviadas", horas);
        }}
        super.onSaveInstanceState(outState);
    }}

    public void recuperarMensajes(Bundle savedInstanceState) {{
        ArrayList<String> textos = savedInstanceState.getStringArrayList("textos_enviados");
        ArrayList<String> horas = savedInstanceState.getStringArrayList("horas_enviadas");
        if (textos != null && horas != null && textos.size() == horas.size()) {{
            for (int i = 0; i < textos.size(); i++) {{
                listaMensajes.add(new Mensaje(textos.get(i), horas.get(i), true));
            }}
        }}
    }}''')

#Modelos con constructor, atributos privados y getters/setters, como en DM_EOHS_06.
for name, fields in [('User', [('long','id'), ('String','nombre'), ('String','usuario'), ('int','imagen')]),
                     ('Mensaje', [('String','texto'), ('String','hora'), ('boolean','enviado')])]:
    attributes = '\n'.join(f'    private {kind} {field};' for kind, field in fields)
    params = ', '.join(f'{kind} {field}' for kind, field in fields)
    assignments = '\n'.join(f'        this.{field} = {field};' for kind, field in fields)
    methods = []
    for kind, field in fields:
        suffix = field[0].upper() + field[1:]
        getter = ('is' if kind == 'boolean' else 'get') + suffix
        methods.append(f'''    public {kind} {getter}() {{
        return {field};
    }}

    public void set{suffix}({kind} {field}) {{
        this.{field} = {field};
    }}''')
    write(src / 'model' / (name + '.java'), f'''package model;

public class {name} {{
{attributes}

    public {name}({params}) {{
{assignments}
    }}

{chr(10).join(methods)}
}}''')

write(package / 'adapters/CustomAdapter.java', '''package com.upiiz.examen_eohs_01.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.upiiz.examen_eohs_01.R;

import java.util.ArrayList;

import model.User;

public class CustomAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<User> listaUsuarios;

    public CustomAdapter(Context ctx, ArrayList<User> listaUsuarios) {
        this.ctx = ctx;
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public int getCount() {
        return listaUsuarios.size();
    }

    @Override
    public User getItem(int position) {
        return listaUsuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaUsuarios.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. Crear la fila si no hay una disponible
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.custom_list_item, parent, false);
        }

        //2. Enlazar las vistas
        TextView tvNombre = convertView.findViewById(R.id.tvNombre);
        TextView tvUsuario = convertView.findViewById(R.id.tvUsuario);
        ImageView ivUsuario = convertView.findViewById(R.id.ivUsuario);

        //3. Asignar los datos del usuario
        User usuario = listaUsuarios.get(position);
        tvNombre.setText(usuario.getNombre());
        tvUsuario.setText(usuario.getUsuario());
        ivUsuario.setImageResource(usuario.getImagen());
        return convertView;
    }
}''')
write(package / 'adapters/MensajeAdapter.java', '''package com.upiiz.examen_eohs_01.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.upiiz.examen_eohs_01.R;

import java.util.ArrayList;

import model.Mensaje;

public class MensajeAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<Mensaje> listaMensajes;

    public MensajeAdapter(Context ctx, ArrayList<Mensaje> listaMensajes) {
        this.ctx = ctx;
        this.listaMensajes = listaMensajes;
    }

    @Override
    public int getCount() {
        return listaMensajes.size();
    }

    @Override
    public Mensaje getItem(int position) {
        return listaMensajes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. Crear la fila si no hay una disponible
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.custom_list_mensaje, parent, false);
        }

        //2. Enlazar las vistas
        TextView tvMensaje = convertView.findViewById(R.id.tvMensaje);
        TextView tvHora = convertView.findViewById(R.id.tvHora);
        LinearLayout llBurbuja = convertView.findViewById(R.id.llBurbuja);

        //3. Asignar los datos del mensaje
        Mensaje mensaje = listaMensajes.get(position);
        tvMensaje.setText(mensaje.getTexto());
        tvHora.setText(mensaje.getHora());

        //4. Colocar enviados a la derecha y recibidos a la izquierda
        LinearLayout.LayoutParams parametros = (LinearLayout.LayoutParams) llBurbuja.getLayoutParams();
        if (mensaje.isEnviado()) {
            parametros.gravity = Gravity.END;
            llBurbuja.setBackgroundResource(R.drawable.bubble_out);
            tvMensaje.setTextColor(ContextCompat.getColor(ctx, R.color.white));
            tvHora.setTextColor(ContextCompat.getColor(ctx, R.color.white));
        } else {
            parametros.gravity = Gravity.START;
            llBurbuja.setBackgroundResource(R.drawable.bubble_in);
            tvMensaje.setTextColor(ContextCompat.getColor(ctx, R.color.text_primary));
            tvHora.setTextColor(ContextCompat.getColor(ctx, R.color.text_secondary));
        }
        llBurbuja.setLayoutParams(parametros);
        return convertView;
    }
}''')

ids = {'username':'etUsuario','password':'etClave','username_box':'tilUsuario',
       'password_box':'tilClave','login_button':'btnIngresar','create_account':'btnCrearCuenta',
       'full_name':'etNombre','email':'etCorreo','confirm_password':'etConfirmarClave',
       'name_box':'tilNombre','email_box':'tilCorreo','confirm_box':'tilConfirmarClave',
       'register_button':'btnRegistrar','have_account':'btnTengoCuenta',
       'users_list':'lvUsuarios','messages_list':'lvMensajes','message_input':'etMensaje',
       'send_button':'btnEnviar','user_name':'tvNombre','user_handle':'tvUsuario',
       'avatar':'ivUsuario','message_text':'tvMensaje','message_time':'tvHora'}
renames = {'activity_register.xml':'activity_registro.xml',
           'activity_users.xml':'activity_custom_list_view.xml',
           'row_user.xml':'custom_list_item.xml'}
for path in list((res / 'layout').glob('*.xml')):
    if path.name in ['row_message_in.xml','row_message_out.xml']:
        continue
    text = path.read_text(encoding='utf-8')
    for old, new in ids.items():
        text = text.replace('@+id/' + old + '"', '@+id/' + new + '"')
    if path.name == 'row_user.xml':
        text = re.sub(r'<TextView android:id="@\+id/ivUsuario".*?/>',
            '<de.hdodenhof.circleimageview.CircleImageView android:id="@+id/ivUsuario" '
            'android:layout_width="48dp" android:layout_height="48dp" '
            'android:src="@drawable/usuario" android:importantForAccessibility="no"/>', text, flags=re.S)
    destination = path.with_name(renames.get(path.name,path.name))
    write(destination, text)
    if destination != path:
        path.unlink()

write(res / 'layout/custom_list_mensaje.xml', '''<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:paddingVertical="5dp">

    <LinearLayout
        android:id="@+id/llBurbuja"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="16dp"
        android:orientation="vertical"
        android:padding="12dp"
        android:background="@drawable/bubble_in">

        <TextView
            android:id="@+id/tvMensaje"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:maxWidth="260dp"
            android:textColor="@color/text_primary"
            android:textSize="15sp" />

        <TextView
            android:id="@+id/tvHora"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="end"
            android:layout_marginTop="5dp"
            android:textColor="@color/text_secondary"
            android:textSize="11sp" />
    </LinearLayout>
</LinearLayout>''')

#Un atributo por línea para que los XML se puedan leer y editar como las prácticas.
def pretty_element(element, depth=0):
    pad = '    ' * depth
    attributes = [f'{attr.name}="{attr.value}"' for attr in element.attributes.values()]
    lines = [pad + '<' + element.tagName]
    lines.extend(pad + '    ' + attr for attr in attributes)
    children = [child for child in element.childNodes if child.nodeType == child.ELEMENT_NODE]
    if not children:
        lines[-1] += ' />'
    else:
        lines[-1] += '>'
        for child in children:
            lines.extend(['', pretty_element(child, depth + 1)])
        lines.extend(['', pad + '</' + element.tagName + '>'])
    return '\n'.join(lines)

for path in (res / 'layout').glob('*.xml'):
    if path.name.startswith('row_message_'):
        continue
    document = minidom.parseString(path.read_text(encoding='utf-8'))
    write(path, '<?xml version="1.0" encoding="utf-8"?>\n' + pretty_element(document.documentElement))

manifest = root / 'app/src/main/AndroidManifest.xml'
write(manifest, manifest.read_text(encoding='utf-8').replace('.RegisterActivity','.RegistroActivity')
      .replace('.UsersActivity','.CustomListViewActivity'))

#Actualizar las pruebas de navegación a los nombres y modelos de la estructura nueva.
test = root / 'app/src/androidTest/java/com/upiiz/examen_eohs_01/ExamFlowTest.java'
text = test.read_text(encoding='utf-8')
for old, new in ids.items():
    text = re.sub(r'R\.id\.' + old + r'\b', 'R.id.' + new, text)
text = text.replace('MessageAdapter','MensajeAdapter')
text = text.replace('import android.content.Intent;', 'import android.content.Intent;\nimport com.upiiz.examen_eohs_01.adapters.MensajeAdapter;')
text = text.replace('assertEquals(i % 2 == 0 ? 1 : 0, adapter.getItemViewType(i));',
                    'assertEquals(i % 2 == 0, adapter.getItem(i).isEnviado());')
text = text.replace('intent.putExtra(ChatActivity.USER_ID, 1);',
                    'intent.putExtra("id_usuario", 1L);\n        intent.putExtra("nombre_usuario", "Ana García");')
text = text.replace('adapter.getItem(20).text','adapter.getItem(20).getTexto()')
text = text.replace('adapter.getItem(20).outgoing','adapter.getItem(20).isEnviado()')
write(test, text)

#Quitar únicamente los archivos reemplazados de la implementación anterior.
for name in ['BaseActivity','Validation','DemoData','User','Message','UserAdapter','MessageAdapter',
             'UsersActivity','RegisterActivity']:
    (package / (name + '.java')).unlink()
for name in ['row_message_in.xml','row_message_out.xml']:
    (res / 'layout' / name).unlink()
(root / 'app/src/test/java/com/upiiz/examen_eohs_01/ExamRequirementsTest.java').unlink()

write(root / 'README.md', '''# EXAMEN_EOHS_01

Aplicación Android en Java y XML para los requisitos de Examen 1.pdf.

## Estructura

Se sigue la organización de DM_EOHS_06, MN_eohs_04 y Ejercicio02: variables al inicio, enlace con `findViewById`, eventos mediante `implements View.OnClickListener`, métodos en las actividades y datos en `ArrayList`.

```text
app/src/main/java/
    com/upiiz/examen_eohs_01/
        MainActivity.java
        RegistroActivity.java
        CustomListViewActivity.java
        ChatActivity.java
        adapters/
            CustomAdapter.java
            MensajeAdapter.java
    model/
        User.java
        Mensaje.java
```

- `MainActivity`: enlaza usuario y clave; `validarUsuario()` compara `admin` y `123456`. `abrirRegistro()` abre el registro mediante Intent.
- `RegistroActivity`: `registrarUsuario()` comprueba nombre de 3 caracteres y contraseñas iguales no vacías; `regresar()` vuelve al login.
- `CustomListViewActivity`: `cargarUsuarios()` agrega 20 usuarios al ArrayList. `onItemClick()` manda su id y nombre al chat usando `putExtra()`.
- `ChatActivity`: recibe los extras y `cargarMensajes()` agrega 20 mensajes. `enviarMensaje()` añade nuevos mensajes locales. Los nuevos mensajes se conservan al girar la pantalla.
- `CustomAdapter`: infla `custom_list_item.xml`, enlaza sus vistas y muestra cada usuario.
- `MensajeAdapter`: infla `custom_list_mensaje.xml`, muestra cada mensaje y coloca la burbuja a la izquierda o derecha con un `if`.
- `model`: clases normales con atributos privados, constructor, getters y setters.

Cada actividad hereda directamente de `AppCompatActivity`. Las validaciones y listas se encuentran en la actividad que las usa. No hay clases `final`, `BaseActivity`, `Validation` ni `DemoData`.

## Ejecutar

Abrir esta carpeta en Android Studio, sincronizar Gradle y ejecutar `app`.

Usuario: **admin**. Clave: **123456**.

El registro es una simulación que regresa al login; no sustituye la cuenta fija del examen. Los mensajes nuevos no se guardan en disco al salir del chat.

## Comprobación

```powershell
.\\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleDebugAndroidTest
```

Las pruebas de navegación en `ExamFlowTest` comprueban acceso, registro, los 20 usuarios, mensajes alternados, envío y recreación de la pantalla. Para ejecutarlas se requiere un dispositivo o emulador funcional. La prueba unitaria de plantilla no comprueba el flujo de pantallas.

Para revisar manualmente: ingresar credenciales incorrectas, registrar un nombre corto y contraseñas distintas, corregir el registro, ingresar con admin / 123456, recorrer los 20 usuarios, abrir distintos chats y comprobar las burbujas de ambos lados. Cada chat inicia con 20 mensajes.

APK: `app/build/outputs/apk/debug/app-debug.apk`.
''')
print('Estructura reescrita con el patrón de las prácticas anteriores.')
