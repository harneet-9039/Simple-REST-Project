package com.myrestproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private Button button;
    private ProgressDialog Progress;
    String StatusCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        Progress = new ProgressDialog(this);
        button.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Progress.show();
        try {
            StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.46:3000/api/values", new Response.
                    Listener<String>() {

                @Override
                public void onResponse(String s) {
                    Progress.dismiss();
                    StatusCode = s.substring(13,16);

                    if (StatusCode.contains("302")) {

                        Intent intent = new Intent(MainActivity.this, Record.class);
                        intent.putExtra("ID", editText.getText().toString());
                        startActivity(intent);
                    }
                    else if(StatusCode.contains("404"))
                    {
                        Toast.makeText(MainActivity.this, "Not Found in Student Database",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Unknown Error", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("HAR",volleyError.toString());
                    Progress.dismiss();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("ID", editText.getText().toString());

                    // if(Name != null)
                    //   parameters.put("Name",Name);
                    return parameters;
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(request);


        } catch (Exception ex) {
            Log.d("HAR",ex.toString());

        }
    }
}
