package com.example.crianbra.servicios;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button button;
    EditText txtid, txtname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtid = (EditText)findViewById(R.id.txt_id);
        txtname = (EditText)findViewById(R.id.txt_name);
        button = (Button)findViewById(R.id.btn_ingreso);

        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Thread thread = new Thread(){
            @Override
            public void run() {
                final String resultado = enviarDatosGET(txtid.getText().toString(), txtname.getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = obtDatosJSON(resultado);
                        if(r > 0){
                            Intent i = new Intent(getApplicationContext(),Main2Activity.class);
                            i.putExtra("cod",txtid.getText().toString());
                            startActivity(i);
                        }else{
                            Toast.makeText(getApplicationContext(), "Usuario y Contraseña incorrectos",Toast.LENGTH_LONG).show();
                            //Log.d("creando","entrando");
                        }
                    }
                });
            }
        };
        thread.start();

    }

    public String enviarDatosGET(String usu, String pas){

        URL url= null;
        String linea="";
        int respuesta = 0;
        StringBuilder result = null;

        try{

            url= new URL("https://educaapp.000webhostapp.com/persona.php?usu="+usu+"&pas="+pas);
            HttpURLConnection conection=(HttpURLConnection)url.openConnection();
            respuesta = conection.getResponseCode(); //codigo de la respuesta, un numero 200 si es qhay respuesta

            result = new StringBuilder();   //si hay respuesta tomamos o estamos consumiendo el json de la respuesta

            if(respuesta == HttpURLConnection.HTTP_OK){  //
                InputStream in = new BufferedInputStream(conection.getInputStream()); //trae la respuesta
                BufferedReader reader = new BufferedReader(new InputStreamReader(in)); //el BufferedReader se encarga de leer la respuesta

                while((linea = reader.readLine())!= null){   //linea que trae como respuesta esta guardada en resutl
                    result.append(linea); //lineas que retornan
                }

            }

        }catch (Exception e){}

        return result.toString();   //retorna el json del archivo php de nuestro servicio


    }

    public int obtDatosJSON(String response){
        int res = 0;

        try{

            JSONArray json= new JSONArray(response);
            if(json.length()>0){
                res=1;
                Log.d("creando","entrando");
            }
        }catch (Exception e){}

        return res;

    }


}
