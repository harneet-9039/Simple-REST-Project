package com.myrestproject;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Record extends AppCompatActivity {

    private TextView ID_User;
    private TextView Name;
    private TextView College;
    private TextView Mob;
    private TextView Marks;
    private ProgressDialog Progress;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        SetFields();
        Progress.show();
        callService();
    }

    private void SetFields()
    {
        Progress = new ProgressDialog(this);
        ID_User = findViewById(R.id.ID_User);
        Name = findViewById(R.id.Name);
        College = findViewById(R.id.CollegeName);
        Mob = findViewById(R.id.Mob);
        Marks = findViewById(R.id.Marks);
        id = getIntent().getStringExtra("ID");


    }

    private void callService()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://192.168.43.46:3000/api/values/"+id, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response1) {
                Progress.dismiss();
                JSONArray response = null;
                JSONObject json;
                String str = response1.toString();

                try {
                    response = new JSONArray(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(response!=null)
                {
                    try {
                        json = response.getJSONObject(0);
                        ID_User.setText(id);
                        Name.setText(json.getString("sName"));
                        College.setText(json.getString("collegeName"));
                        Mob.setText(json.getString("mob"));
                        Marks.setText(json.getString("percentage_marks"));

                    }
                    catch (Exception e)
                    {
                        Log.e("HAR",e.toString());
                    }
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Record.this, "Volley Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToJsonRequestQueue(jsonArrayRequest);
    }
}
