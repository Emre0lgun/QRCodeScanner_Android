package com.cdac.qrcodescanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListItem extends AppCompatActivity {

    List<HashMap<String, String>> result = new ArrayList<>();
    ListView listView;
    String jsonString = "";
    SharedPreferences sharedPref;
    String id = "";
    String name = "";
    String username = "";
    String email = "";
    String companyname = "";
    String city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = sharedPref.edit();
        Log.d("emreUSERNAME", sharedPref.getString("userName", "") + "");
        Log.d("emreUSERNAME", sharedPref.getString("userSurname", "") + "");
        Log.d("emreUSERNAME", sharedPref.getString("userEmail", "") + "");
        if (sharedPref.getString("data", "").equals("")) {
            finish();
        } else {
            listView = findViewById(R.id.listView);
            HashMap<String, String> repo = new HashMap<>();
            name = sharedPref.getString("userName", "");
            username = sharedPref.getString("userSurname", "");
            email = sharedPref.getString("userEmail", "");
            /*try {
                JSONObject json = new JSONObject(sharedPref.getString("data",""));
                Log.d("emreJSON",json.toString()+"");
                id = json.getString("id");
                name = json.getString("name");
                username = json.getString("username");
                email = json.getString("phone");
                city = json.getJSONObject("address").getString("city");
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            repo.put("name", name);
            repo.put("username", username);
            repo.put("email", email);
            result.add(repo);
            ListAdapter listAdapter = new SimpleAdapter(
                    ListItem.this,
                    result,
                    R.layout.activity_list,
                    new String[]{"name", "username", "email"},
                    new int[]{R.id.tv_name, R.id.tv_description, R.id.tv_phone}
            );
            listView.setAdapter(listAdapter);
        }
        Log.d("emreshared", sharedPref.getString("data", "") + "emre");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }
        return true;
    }
}
