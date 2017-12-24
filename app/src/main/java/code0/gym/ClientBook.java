package code0.gym;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientBook extends AppCompatActivity {

    Button  button4;
    EditText amt, editText14;
    String coach_phone,coach_name, timebooked, idd;
    String user_id;
    String price;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_book);


        Intent intent = getIntent();
        coach_phone=intent.getStringExtra("coach_phone");
        coach_name=intent.getStringExtra("coach_name");
        timebooked=intent.getStringExtra("timebooked");
        user_id=intent.getStringExtra("username");
        idd=intent.getStringExtra("id");
        price= intent.getStringExtra("price");

       // Toast.makeText(this, user_id, Toast.LENGTH_SHORT).show();

        amt=(EditText)findViewById(R.id.editText9);
        amt.setText(price);

        editText14=(EditText)findViewById(R.id.editText14);
        button4=(Button)findViewById(R.id.button4);

        TextView t16= (TextView)findViewById(R.id.textView16);
        TextView t17= (TextView)findViewById(R.id.textView17);

        t16.setText(coach_name);
        t17.setText(coach_phone);



        final EditText sItems2 = (EditText) findViewById(R.id.spinner5);
        final Spinner sItems = (Spinner) findViewById(R.id.spinner4);
        final Spinner spinner=(Spinner)findViewById(R.id.spinner3);


        List<String> spinnerArray1 =  new ArrayList<String>();
        spinnerArray1.add("Choose Package!");
        spinnerArray1.add("Gold");
        spinnerArray1.add("Silver");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray1);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);





        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Choose your preferred mode!");
        spinnerArray.add("Host Coach");
        spinnerArray.add("Go to Gym");



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner.getSelectedItem().toString().equals("Gold"))
                {
                    int total_amt=Integer.parseInt(price)+200;
                    amt.setText(String.valueOf(total_amt));
                }
                else
                    amt.setText(String.valueOf(price));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i)
                {
                    case 1:
                        sItems2.setVisibility(View.VISIBLE);
                        break;
                    default:
                        sItems2.setVisibility(View.GONE);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerArray =  new ArrayList<String>();

        spinnerArray.add("Choose location");
        spinnerArray.add("GreenFields");
        spinnerArray.add("Savanna");
        spinnerArray.add("GreenSpan");
        spinnerArray.add("Tena");
        spinnerArray.add("Umoja");
        spinnerArray.add("Buruburu");

        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //sItems2.setAdapter(adapter);
        sItems2.setVisibility(View.GONE);


        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                final String ammount= amt.getText().toString();

                if(ammount.isEmpty()||editText14.getText().toString().trim().length()!=10)
                {
                    Toast.makeText(ClientBook.this, "Invalid amount or phone number!", Toast.LENGTH_SHORT).show();
                }
                else
                {


                    if(spinner.getSelectedItem().toString().equals("Gold"))
                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ClientBook.this);
                        builder.setMessage("Are you sure want to pay "+ ammount+" ?" +
                                "You selected GOLD package, you will be charged higher but given more priority!")
                                .setTitle("Please confirm.")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        String category="GOLD";
                                       String phone="254"+String.valueOf(Integer.parseInt(editText14.getText().toString())+200);
                                        book(phone,coach_phone, ammount, timebooked, category);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {

                                    }
                                });
                        builder.show();
                    }
                    else
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ClientBook.this);
                            builder.setMessage("Are you sure want to pay "+ ammount+" ?")
                                    .setTitle("Please confirm")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {


                                            String category="SILVER";
                                            String phone="254"+String.valueOf(Integer.parseInt(editText14.getText().toString()));
                                            book(phone, coach_phone, ammount, timebooked, category);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {

                                        }
                                    });
                            builder.show();


                        }



                }





            }
        });

    }

public void book(final String phone, final String coach_phone, final String ammt, final String time, final String category)
{
    class GetJSON extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog = new ProgressDialog(ClientBook.this);

        @Override
        protected void onPreExecute()
        {

            super.onPreExecute();
            progressDialog.setMessage("Applying...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params)
        {
            RequestHandler rh = new RequestHandler();
            HashMap<String, String> employees = new HashMap<>();
            employees.put("customer_id)", user_id);
            employees.put("coach_phone", coach_phone);
            employees.put("ammount", ammt);
            employees.put("phone",phone);
            employees.put("time", time);
            employees.put("coach", idd);
            employees.put("user", user_id);
            employees.put("category", category);
            String res=rh.sendPostRequest(URLs.main+"mpesa/home.php",employees);
            return res;

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();


            Toast.makeText(ClientBook.this, "Result: "+s, Toast.LENGTH_SHORT).show();

//            if (s.substring(0,1).equals("1"))
//            {
//                new AlertDialog.Builder(ClientBook.this)
//                        .setMessage("Your request has been received")
//                        .show();
//
//            }
//            else
//            {
//                new AlertDialog.Builder(ClientBook.this)
//                        .setMessage("Request failed!")
//                        .show();
//            }

        }


    }
    GetJSON jj =new GetJSON();
    jj.execute();
}

    public void getJSON(final String coach_phone_, final String ammount)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(ClientBook.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog.setMessage("Applying...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> employees = new HashMap<>();
                employees.put("coach_phone", coach_phone_);
                employees.put("ammount", ammount);
                employees.put("user_id", user_id);
                employees.put("date_time", "");
                String res=rh.sendPostRequest(URLs.main+"fetchcoaches.php",employees);
                return res;

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
               // showthem(s);
                Toast.makeText(ClientBook.this, s, Toast.LENGTH_SHORT).show();

            }


        }
        GetJSON jj =new GetJSON();
        jj.execute();
    }


}
