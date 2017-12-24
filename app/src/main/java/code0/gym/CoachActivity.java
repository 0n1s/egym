package code0.gym;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CoachActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    EditText date, time, amt;
    ListView listView;
    Button save,button6;
    EditText editText12;
    String username;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Intent intent = getIntent();
        username= intent.getStringExtra("username");
        getSupportActionBar().setTitle("Coach panel");
        date = (EditText) findViewById(R.id.editText7);
        editText12=(EditText)findViewById(R.id.editText12);
        save = (Button) findViewById(R.id.button3);
        button6=(Button)findViewById(R.id.button6);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(CoachActivity.this, date.getText().toString(), Toast.LENGTH_SHORT).show();
                if(date.getText().toString().isEmpty()||time.getText().toString().isEmpty())
                {
                    Toast.makeText(CoachActivity.this, "Please select time and date!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(editText12.getText().toString().trim().length()==0)
                    {
                        Toast.makeText(CoachActivity.this, "Please set your prices!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    send_to_the_db(username, date.getText().toString(), time.getText().toString(),editText12.getText().toString().trim());
                }
            }
        });
        listView = (ListView) findViewById(R.id.listview1);

        time = (EditText) findViewById(R.id.editText8);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(CoachActivity.this, CoachActivity.this, 2017, 7, 4);
                dialog.show();

            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(CoachActivity.this, CoachActivity.this, 0, 0, true);
                timePickerDialog.show();

            }
        });

        String amt= sharedpreferences.getString("amt", "0");
        editText12.setText(amt);
        editText12.setGravity(1);
        editText12.setEms(5);


        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amt=editText12.getText().toString();

                try{

                    int ammount= Integer.parseInt(amt);
                    SharedPreferences.Editor editor =sharedpreferences.edit();
                    editor.putString("amt",String.valueOf(ammount));
                    editor.commit();
                    Toast.makeText(CoachActivity.this, "Saved!", Toast.LENGTH_SHORT).show();


                   // save_price(username,amt.trim());

                }
                catch (Exception ex)
                {
                    Toast.makeText(CoachActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                }


            }
        });
        getJSON(username);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        date.setText(String.valueOf(i) + "-" + String.valueOf(i1) + "-" + String.valueOf(i2));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        time.setText(String.valueOf(i) + ":" + String.valueOf(i1));
    }


    public void getJSON(final String coach_id)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(CoachActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog.setMessage("Fetching data...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> employees = new HashMap<>();
                employees.put("coach_id", coach_id);
                employees.put("who", "asf");
                String res = rh.sendPostRequest(URLs.main + "fetchcoachtime.php", employees);
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                showthem(s);

            }


        }
        GetJSON jj = new GetJSON();
        jj.execute();
    }






    public void send_to_the_db(final String coach_id, final String date, final String time, final String amt)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(CoachActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(CoachActivity.this, amt, Toast.LENGTH_SHORT).show();
                progressDialog.setMessage("Saving data...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> employees = new HashMap<>();
                employees.put("coach_id", coach_id);
                employees.put("date", date);
                employees.put("time", time);
                employees.put("amt", amt);
                String res = rh.sendPostRequest(URLs.main + "insert_time.php", employees);
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
               // showthem(s);
                getJSON(username);
              //  Toast.makeText(CoachActivity.this, s, Toast.LENGTH_SHORT).show();

            }


        }
        GetJSON jj = new GetJSON();
        jj.execute();
    }



    private void showthem(String s)
    {

        //new AlertDialog.Builder(CoachActivity.this).setMessage(s).show();
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            String succes = "0";
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);


                succes = jo.getString("success");
                if (succes.equals("1")) {
                    String datetime;
                    datetime = jo.getString("datetime");
                    HashMap<String, String> employees = new HashMap<>();
                    employees.put("datetime", datetime);
                    list.add(employees);
                } else {

                }


            }

        } catch (JSONException e) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CoachActivity.this);
            alertDialogBuilder.setTitle("Attention!").setMessage(String.valueOf(e))
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setCancelable(true).show();
        }

        ListAdapter adapter = new SimpleAdapter(CoachActivity.this, list, R.layout.coach_panel_list,
                new String[]{"datetime"}, new int[]{R.id.textView20});
        listView.setAdapter(adapter);
    }


}
