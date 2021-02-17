package com.example.tp03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VerCategorias extends AppCompatActivity {
    TextView tvCategorias;
    Button btnVerCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_categorias);
        ObtenerReferencias();
        SetearListeners();
    }

    private void SetearListeners() {
        btnVerCat.setOnClickListener(btnVerCat_Click);
    }

    View.OnClickListener btnVerCat_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ObtenerCategorias();
        }
    };
    private void ObtenerCategorias() {
        TraerCategoriasTask CategoriasTask = new TraerCategoriasTask();
        CategoriasTask.execute();
    }

    private void ObtenerReferencias() {
        tvCategorias=findViewById(R.id.tvCategorias);
        btnVerCat = findViewById(R.id.btnVerCat);
    }

    private class TraerCategoriasTask extends AsyncTask<Void,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvCategorias.setText("");
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection miConexion = null;
            URL strAPIUrl;
// Estoy en el Background Thread.
            BufferedReader responseReader;
            String responseLine, strResultado = "";
            StringBuilder sbResponse;
            try {
                strAPIUrl = new URL("https://epok.buenosaires.gob.ar/getCategorias/");
                miConexion = (HttpURLConnection) strAPIUrl.openConnection();
                miConexion.setRequestMethod("GET");
                if (miConexion.getResponseCode() == 200) {
                    responseReader = new BufferedReader(new InputStreamReader(miConexion.getInputStream()));
                    sbResponse = new StringBuilder();
                    while ((responseLine = responseReader.readLine()) != null) {
                        sbResponse.append(responseLine);
                    }
                    responseReader.close();
                    strResultado = sbResponse.toString();
                } else {
// Ocurrió algún error.
                }
            } catch (Exception e) {
// Ocurrió algún error al conectarme, serán permisos?
            } finally {
                if (miConexion != null){
                    miConexion.disconnect();
                }
            }
            return strResultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            tvCategorias.setText(resultado);                
            PasarJSON(resultado);
        }

        private void PasarJSON(String resultado) {
            JsonObject JSONrespuesta, JSONCategoria;
            JsonArray categoriasJSONArr;
            int intCantCategorias;
            String sNombre;

            tvCategorias.setText("");
            JSONrespuesta = JsonParser.parseString(resultado).getAsJsonObject();
            intCantCategorias= JSONrespuesta.get("cantidad_de_categorias").getAsInt();

            tvCategorias.append("CantidadCategorias: " + String.valueOf(intCantCategorias) + "\n");

            categoriasJSONArr = JSONrespuesta.getAsJsonArray("categorias");

            for(int i =0; i < categoriasJSONArr.size(); i++){
                JSONCategoria = categoriasJSONArr.get(i).getAsJsonObject();
                sNombre = JSONCategoria.get("nombre").getAsString();

                tvCategorias.append(String.valueOf(i) + " - " + sNombre + "\n");
            }

        }
    }
}
