package com.upiiz.dm_eohs_05;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvDisplay;
    Button btn_residuo, btn_par, boton_par, btnlimpiar, btnNueve, btnOcho, btnSiete, btnSeis;
    Button btnCinco, btnCuatro, btnTres, btnDos, btnUno, btnCero;
    Button btnMas, btnMenos, btnDivision, btnPunto, btnIgual, btnMultiplicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        iniciar();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnlimpiar) {
            clearDisplay();
        } else if (id == R.id.btnIgual) {
            calcularResultado();
        } else {
            Button btn = (Button) v;
            String currentText = tvDisplay.getText().toString();
            if (currentText.equals("Error")) {
                currentText = "";
            }
            tvDisplay.setText(currentText + btn.getText().toString());
        }
    }
    public void clearDisplay() {
        tvDisplay.setText("");
    }
    private void calcularResultado() {
        String expression = tvDisplay.getText().toString();
        if (expression.isEmpty()) {
            return;
        }
        try {
            double result = eval(expression);
            if (result == (long) result) {
                tvDisplay.setText(String.format("%d", (long) result));
            } else {
                tvDisplay.setText(String.valueOf(result));
            }
        } catch (Exception e) {
            tvDisplay.setText("Error");
        }
    }

    public void iniciar() {
        // Vinculamos
        tvDisplay = findViewById(R.id.tvDisplay);
        btn_residuo = findViewById(R.id.btn_residuo);
        btn_par = findViewById(R.id.btn_par);
        boton_par = findViewById(R.id.boton_par);
        btnlimpiar = findViewById(R.id.btnlimpiar);
        btnNueve = findViewById(R.id.btnNueve);
        btnOcho = findViewById(R.id.btnOcho);
        btnSiete = findViewById(R.id.btnSiete);
        btnSeis = findViewById(R.id.btnSeis);
        btnCinco = findViewById(R.id.btnCinco);
        btnCuatro = findViewById(R.id.btnCuatro);
        btnTres = findViewById(R.id.btnTres);
        btnDos = findViewById(R.id.btnDos);
        btnUno = findViewById(R.id.btnUno);
        btnCero = findViewById(R.id.btnCero);
        btnMas = findViewById(R.id.btnMas);
        btnMenos = findViewById(R.id.btnMenos);
        btnDivision = findViewById(R.id.btnDivision);
        btnPunto = findViewById(R.id.btnPunto);
        btnIgual = findViewById(R.id.btnIgual);
        btnMultiplicacion = findViewById(R.id.btnMultiplicacion);

        // Escuchar los clicks - Touch
        btn_residuo.setOnClickListener(this);
        btn_par.setOnClickListener(this);
        boton_par.setOnClickListener(this);
        btnlimpiar.setOnClickListener(this);
        btnNueve.setOnClickListener(this);
        btnOcho.setOnClickListener(this);
        btnSiete.setOnClickListener(this);
        btnSeis.setOnClickListener(this);
        btnCinco.setOnClickListener(this);
        btnCuatro.setOnClickListener(this);
        btnTres.setOnClickListener(this);
        btnDos.setOnClickListener(this);
        btnUno.setOnClickListener(this);
        btnCero.setOnClickListener(this);
        btnMas.setOnClickListener(this);
        btnMenos.setOnClickListener(this);
        btnDivision.setOnClickListener(this);
        btnPunto.setOnClickListener(this);
        btnIgual.setOnClickListener(this);
        btnMultiplicacion.setOnClickListener(this);
    }

    // Evaluador de expresiones matemáticas (Shunting-yard / Recursive descent parser)
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm(); // suma
                    else if (eat('-')) x -= parseTerm(); // resta
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor(); // multiplicacion
                    else if (eat('/')) x /= parseFactor(); // division
                    else if (eat('%')) x %= parseFactor(); // modulo
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // más unario
                if (eat('-')) return -parseFactor(); // menos unario

                double x;
                int startPos = this.pos;
                if (eat('(')) { // paréntesis
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // números
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return x;
            }
        }.parse();
    }
}
