package com.upiiz.mn_eohs_04;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //1. Declarar variables
    EditText etnum1,etnum2;
    TextView tvnum1,tvnum2,tvResultado;
    Button btnsum,btnrest,btnmult,btndiv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //Revisar siempre que corresponda la vista con lo que se carga.
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //2. Enlazar vairbales con vistas.
        etnum1 = findViewById(R.id.etnum1);
        etnum2 = findViewById(R.id.etnum2);
        tvnum1 = findViewById(R.id.tvnum1);
        tvnum2 = findViewById(R.id.tvnum2);
        tvResultado = findViewById(R.id.tvResultado);
        btnsum = findViewById(R.id.btnsum);
        btnrest = findViewById(R.id.btnrest);
        btnmult = findViewById(R.id.btnmult);
        btndiv = findViewById(R.id.btndiv);
        //3. Desarrollo
        //3.1 el boton que escuche click o touch
        btnsum.setOnClickListener(this);
        btnrest.setOnClickListener(this);
        btnmult.setOnClickListener(this);
        btndiv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Función principal de desarrollo de muestras.
        String entrada1 = etnum1.getText().toString().trim();
        String entrada2 = etnum2.getText().toString().trim();

        if(entrada1.isEmpty() || entrada2.isEmpty()){
            tvResultado.setText("Ingrese ambos números, porfavor");
            return;
        }
        double num1 =  Double.parseDouble(entrada1);
        double num2 =  Double.parseDouble(entrada2);
        double resultado = 0;
        int id = v.getId();
        if(id == R.id.btnsum){
            resultado = num1 + num2;
        } else if(id == R.id.btnrest){
            resultado = num1 - num2;
        }else if(id == R.id.btnmult){
            resultado = num1 * num2;
        } else if(id== R.id.btndiv){
            if(num2 == 0){
                tvResultado.setText("Error: no se puede dividir entre 0");
                return;
            }
            resultado = num1 / num2;
        }
        tvResultado.setText(String.valueOf(resultado));
    }
}