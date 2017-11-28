package code0.gym;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminAdd extends AppCompatActivity {
EditText coach_name, id_number, email_adress, telephone_number;
    Button reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);

        getSupportActionBar().setTitle("Register a coach");



        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Gender");
        spinnerArray.add("Male");
        spinnerArray.add("Female");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);

        coach_name=(EditText)findViewById(R.id.editText3);
        id_number=(EditText)findViewById(R.id.editText4);
        email_adress=(EditText)findViewById(R.id.editText5);
        telephone_number=(EditText)findViewById(R.id.editText6);
        reg=(Button)findViewById(R.id.button2);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=coach_name.getText().toString();
                String idnum=id_number.getText().toString();
                String email=email_adress.getText().toString();
                String tel=telephone_number.getText().toString();
                String gender=sItems.getSelectedItem().toString();


               if(verify(tel, email,idnum))
               {
                   register(name, idnum, email, tel, gender);
               }





            }
        });



    }

    public  boolean  verify(String phone_number, String email, String idnum)
    {
        boolean istrue =true;

        if(phone_number.length()!=10)
        {
            istrue=false;
            telephone_number.setError("Enter a valid phone number");


        }
        if(idnum.length()==0)
        {
            istrue=false;
            id_number.setError("Enter a valid id number");


        }
        if(!isValidEmail(email))
        {
            istrue=false;
            email_adress.setError("Enter a valid email address!");
        }


        return istrue;
    }


    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

   public void register(final String name, final String idnum, final String email, final String tel, final String gender)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog pDialog = new ProgressDialog(AdminAdd.this);



            @Override
            protected void onPreExecute() {
                super.onPreExecute();


                pDialog.setMessage("Adding coach...");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> paramms = new HashMap<>();
                paramms.put("name", name);
                paramms.put("id", idnum);
                paramms.put("email", email);
                paramms.put("gender", gender);
                paramms.put("phone", tel);
                String s = rh.sendPostRequest(URLs.main+"regcoach.php", paramms);
                return s;

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pDialog.dismiss();

                if(s.equals("1"))
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminAdd.this);
                    alertDialogBuilder.setTitle("Attention!").setMessage("Registration success.")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setCancelable(true).show();
                }
                else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminAdd.this);
                    alertDialogBuilder.setTitle("Attention!")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setMessage("Registration Failed.").setCancelable(true).show();
                }

            }


        }
        GetJSON jj = new GetJSON();
        jj.execute();
    }
}
