package com.example.tp03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnVerCategorias;
    Button btnBuscarTexto;
    Button btnBuscarGeo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObtenerReferencias();
        SetearListeners();
    }

    private void SetearListeners() {
        btnVerCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, VerCategorias.class);
                startActivity(myIntent);
            }
        });

        btnBuscarTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent miIntent = new Intent(MainActivity.this, BuscarTexto.class);
                startActivity(miIntent);
            }
        });
        btnBuscarGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent miIntent1 = new Intent(MainActivity.this, BuscarGeolocalizacion.class);
                startActivity(miIntent1);
            }
        });
    }

    private void ObtenerReferencias() {
        btnVerCategorias = findViewById(R.id.btnVerCat);
        btnBuscarTexto = findViewById(R.id.btnSTexto);
        btnBuscarGeo = findViewById(R.id.btnSGeo);

    }
}
