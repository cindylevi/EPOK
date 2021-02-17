package com.example.tp03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BuscarTexto extends AppCompatActivity {
    Button btnBuscar;
    EditText etPalabraBusca;
    TextView tvResultados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_texto);
        ObtenerReferencias();
        SetearListener();
    }

    private void SetearListener() {
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPalabraBusca.getText().toString().length() != 0) {
                    BuscarTexto.TraerResultadoPorTexto CategoriasTask = new BuscarTexto.TraerResultadoPorTexto();
                    CategoriasTask.execute();
                }
                else {
                    tvResultados.setText("Ingrese una palabra de busqueda");
                }
            }
        });
    }

    private void ObtenerReferencias() {
        btnBuscar=findViewById(R.id.btnBuscar);
        tvResultados = findViewById(R.id.tvResultados);
        etPalabraBusca = findViewById(R.id.edPalabraBusca);
    }

    private class TraerResultadoPorTexto extends AsyncTask<Void,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvResultados.setText("");
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
                strAPIUrl = new URL("https://epok.buenosaires.gob.ar/buscar/?texto=" + etPalabraBusca.getText().toString());
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
            tvResultados.setText(resultado);
            PasarJSON(resultado);
        }

        private void PasarJSON(String resultado) {
            JsonObject JSONrespuesta, JSONCategoria;
            JsonArray categoriasJSONArr;
            int intCantCategorias;
            String sNombre;

            tvResultados.setText("");
            JSONrespuesta = JsonParser.parseString(resultado).getAsJsonObject();
            intCantCategorias= JSONrespuesta.get("total").getAsInt();

            tvResultados.append("Encontrados: " + String.valueOf(intCantCategorias) + "\n");

            categoriasJSONArr = JSONrespuesta.getAsJsonArray("instancias");
            if(intCantCategorias == 0)
            {
              tvResultados.setText("Su busqueda no arrojo resultados");
            }
            else{
                for(int i =0; i < categoriasJSONArr.size(); i++){
                    JSONCategoria = categoriasJSONArr.get(i).getAsJsonObject();
                    sNombre = JSONCategoria.get("nombre").getAsString();

                    tvResultados.append(String.valueOf(i) + " - " + sNombre + "\n");
                }
            }
        }
    }
}