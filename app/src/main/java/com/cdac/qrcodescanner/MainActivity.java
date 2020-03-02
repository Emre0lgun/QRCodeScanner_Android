package com.cdac.qrcodescanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView;
    String jsonString = "";
    SharedPreferences sharedPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        sharedPref = this.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (getIntent().getStringExtra("data") != null) {
            jsonString = getIntent().getStringExtra("data");
        }

        if (getIntent().getStringExtra("data") != null) {
            textView.setText(getIntent().getStringExtra("data"));
            editor.putString("data", getIntent().getStringExtra("data"));
            String lines[] = getIntent().getStringExtra("data").split("\\r?\\n");
            for (int i = 0; i < lines.length; i++) {
                Log.d("emrepreferencesarray",lines[i]+"");
            }
            editor.putString("userName",lines[0]);
            editor.putString("userSurname",lines[1]);
            editor.putString("userEmail",lines[2]);
            editor.commit();
            Log.d("emrepreferences", sharedPref.getString("data", "") + "emre");
            /*try {
                JSONObject jsonObj = new JSONObject(jsonString);
                Log.d("emreJSON", jsonObj.toString() + "");
                String id = jsonObj.getString("id");
                String name = jsonObj.getString("name");
                String username = jsonObj.getString("username");
                String email = jsonObj.getString("email");
                String companyname = jsonObj.getJSONObject("company").getString("name");
                String city = jsonObj.getJSONObject("address").getString("city");
                Log.d("emreid", id);
                Log.d("emrename", name);
                Log.d("emreusername", username);
                Log.d("emremail", email);
                Log.d("emrecompany", companyname);
                Log.d("emrecity", city);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        } else {
            textView.setText("QR Code is not Checked!");
        }
    }


    private void initComponents() {
        findViewById(R.id.buttonTakePicture).setOnClickListener(this);
        findViewById(R.id.buttonScanBarcode).setOnClickListener(this);
        textView = findViewById(R.id.txt);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonScanBarcode:
                startActivity(new Intent(this, ScannerBarcodeActivity.class));
                break;
            case R.id.buttonTakePicture:
                startActivity(new Intent(this, ListItem.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.qrshow:
                Intent intent = new Intent(MainActivity.this, QRCodeGenerator.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}