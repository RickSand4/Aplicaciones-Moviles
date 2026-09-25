package com.upiiz.dm_eohs_06;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Variables
    Button btnBasicLV, btnCustomLV, btnBasicRecyclerView, btnCustomRecyclerView;

    @SuppressLint("MissingInflatedId")
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
        //Enlazar
        btnBasicLV = findViewById(R.id.btnBasicListView);
        btnCustomLV = findViewById(R.id.btnCustomListView);
        btnBasicRecyclerView = findViewById(R.id.btnBasicRecyclerView);
        btnCustomRecyclerView = findViewById(R.id.btnCustomRecyclerView);
        //Listener
        btnBasicLV.setOnClickListener(this);
        btnCustomRecyclerView.setOnClickListener(this);
        btnCustomLV.setOnClickListener(this);
        btnBasicRecyclerView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnBasicListView)
            iniciarBasicListView();
        else if (v.getId()==R.id.btnCustomListView){
            iniciarCustomListView();
        }else if (v.getId()==R.id.btnBasicRecyclerView){
            iniciarBasicRecyclerView();
        }else
            iniciarCustomRecyclerView();
    }
    public void iniciarBasicListView(){
        Intent intentBLV = new Intent(this, BasicListViewActivity.class);
        startActivity(intentBLV);
    }
    public void iniciarCustomListView(){
        Intent intentCLV = new Intent(this, CustomListViewActivity.class);
        startActivity(intentCLV);
    }
    public void iniciarBasicRecyclerView(){
        Intent intentBRV = new Intent(this, BasicRecyclerViewActivity.class);
        startActivity(intentBRV);
    }
    public void iniciarCustomRecyclerView(){
        Intent intentCRV = new Intent(this, CustomRecyclerViewActivity.class);
        startActivity(intentCRV);
    }
}