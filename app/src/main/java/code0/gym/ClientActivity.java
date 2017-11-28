package code0.gym;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientActivity extends AppCompatActivity {
ListView listView;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);



        Intent intent = getIntent();
        user_id=intent.getStringExtra("username");
        listView=(ListView)findViewById(R.id.listview);
        getJSON();

getSupportActionBar().setTitle("Available Coaches");

        /*
        eti payment should be working during progress, apo kwa payment itakuwa two types(gold and silver) so
        if a person is willing to pay gold class then he is considered first.. if they are more than one for gold
         we use FCFS nd the other one is rescheduled
         */


        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);

                final  String coach_phone =   map.get("coach_phone");
                final  String coach_name=map.get("coach_name");
                final String idd=map.get("id");
                final String timebooked= map.get("datetime");
                final String price=map.get("price");
               // Toast.makeText(ClientActivity.this, coach_phone, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ClientActivity.this, ClientBook.class)
                    .putExtra("coach_phone",coach_phone)
                    .putExtra("coach_name",coach_name)
                    .putExtra("id", idd)
                            .putExtra("price", price)
                    .putExtra("username", user_id)
                    .putExtra("timebooked",timebooked));



            }});




    }



    /*



     */






    public void getJSON()
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(ClientActivity.this);

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
                employees.put("coach_id", "");
                employees.put("who", "1");
                String res = rh.sendPostRequest(URLs.main + "fetchcoachtime.php", employees);
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                showthem(s);
                //Toast.makeText(Utensils.this, s, Toast.LENGTH_SHORT).show();

            }


            /*
           eti payment should be working during progress,
           apo kwa payment itakuwa two types(gold and silver)
            so if a person is willing to pay gold class then he is considered first..
            if they are more than one for gold we use FCFS nd the other one is rescheduled
             */

        }
        GetJSON jj = new GetJSON();
        jj.execute();
    }


    private void showthem(String s) {

       // Toast.makeText(this, s, Toast.LENGTH_LONG).show();

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            String succes = "0";
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
String price;

                succes = jo.getString("success");
                if (succes.equals("1")) {
                    String datetime,coach_name,coach_gender, coach_phone, id;
id=jo.getString("id");
                    coach_name = jo.getString("coach_name");
                    price=jo.getString("price");
                    coach_gender = jo.getString("coach_gender");
                    coach_phone = jo.getString("coach_phone");
                    datetime = jo.getString("datetime");

                    HashMap<String, String> employees = new HashMap<>();
                    employees.put("id", id);
                    employees.put("price",price);
                    employees.put("datetime", datetime);
                    employees.put("coach_phone", coach_phone);
                    employees.put("coach_gender", coach_gender);
                    employees.put("coach_name", coach_name);
                    list.add(employees);

                } else {

                }


            }

        } catch (JSONException e)
        {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ClientActivity.this);
            alertDialogBuilder.setTitle("Attention!").setMessage(String.valueOf(e))
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setCancelable(true).show();
        }

        ListAdapter adapter = new SimpleAdapter(ClientActivity.this, list, R.layout.client_coach_list,
                new String[]{"datetime","coach_phone","coach_gender","coach_name","price"}, new int[]{
                R.id.textView37,R.id.textView36,R.id.textView35,R.id.textView20, R.id.textView40});
        listView.setAdapter(adapter);
    }
}
