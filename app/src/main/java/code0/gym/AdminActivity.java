package code0.gym;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity
{
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().setTitle("Admin Panel");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AdminAdd.class));
            }
        });

        getJSON();
        listView=(ListView)findViewById(R.id.listview);


        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
                final  String coach_id =   map.get("id");



                new AlertDialog.Builder(AdminActivity.this).setTitle("Please confirm").setMessage("Do you want to delete the selected user?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteUser(coach_id);
                            }
                        }).show();


            }});

    }



    public void deleteUser(final String username)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(AdminActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog.setMessage("Fetching data...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> employees = new HashMap<>();
                employees.put("owner_id", "");
                String res=rh.sendPostRequest(URLs.main+"deletecoach.php?username="+username,employees);
                return res;

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                //showthem(s);
                 Toast.makeText(AdminActivity.this, s, Toast.LENGTH_SHORT).show();
                getJSON();
            }


        }
        GetJSON jj =new GetJSON();
        jj.execute();
    }


    public void getJSON()
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

           ProgressDialog progressDialog = new ProgressDialog(AdminActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog.setMessage("Fetching data...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> employees = new HashMap<>();
                employees.put("owner_id", "");
                String res=rh.sendPostRequest(URLs.main+"fetchcoaches.php",employees);
                //http://192.168.0.35/gym/fetchcoaches.php

                return res;

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                showthem(s);
              //  Toast.makeText(AdminActivity.this, s, Toast.LENGTH_SHORT).show();

            }


        }
        GetJSON jj =new GetJSON();
        jj.execute();
    }


    private void showthem(String s) {

        //new AlertDialog.Builder(AdminActivity.this).setMessage(s).show();
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            String succes="0";
            for (int i = 0; i < result.length(); i++)
            {  JSONObject jo = result.getJSONObject(i);


                succes=jo.getString("success");
                if (succes.equals("1"))
                {
                    String id, name,gender, phone,email;

                    id=jo.getString("id");
                    name=jo.getString("name");
                    gender=jo.getString("gender");
                    email=jo.getString("email");
                    phone=jo.getString("phone");

                    HashMap<String, String> employees = new HashMap<>();
                    employees.put("id", id);
                    employees.put("name", name);
                    employees.put("gender", gender);
                    employees.put("email", email);
                    employees.put("phone", phone);
                    list.add(employees);
                }
                else
                {

                }





            }





        } catch (JSONException e) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminActivity.this);
            alertDialogBuilder.setTitle("Attention!").setMessage(String.valueOf(e))
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setCancelable(true).show();
        }

        ListAdapter adapter = new SimpleAdapter(AdminActivity.this, list, R.layout.admin_view_coaches,
                new String[]{"id", "name","gender","email","phone"}, new int[]{R.id.textView27, R.id.textView26,R.id.textView30, R.id.textView28, R.id.textView29});
        listView.setAdapter(adapter);
    }
}
