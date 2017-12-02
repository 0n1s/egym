package code0.gym;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientRegistration extends AppCompatActivity
{

    Button button_register;
    EditText name, phone, username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_registration);

        button_register=findViewById(R.id.button5);


        name=(EditText) findViewById(R.id.editText10);
        phone=(EditText)findViewById(R.id.editText11);
        password=(EditText)findViewById(R.id.editText13);

       final Spinner spinner6= (Spinner)findViewById(R.id.spinner6);

        //ArrayList<String, String>arrayList;


        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Male");
        spinnerArray.add("Female");



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner6.setAdapter(adapter);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String phonne=phone.getText().toString();

                if(verify(phonne))
                {
                    EditText lnmae= findViewById(R.id.editText17);
                    EditText surname=findViewById(R.id.editText18);
                    Register(name.getText().toString() + lnmae.getText().toString()+" " + surname.getText().toString(),
                            phone.getText().toString(),
                            password.getText().toString(),
                            spinner6.getSelectedItem().toString()
                    );
                }

            }
        });


     }

     public  boolean  verify(String phone_number)
     {
         boolean istrue =true;

         if(phone_number.length()!=10)
         {
             istrue=false;
             phone.setError("Enter a valid phone number!");

         }

         return istrue;
     }
     public  void Register(final String name, final String phone, final String password, final String gender)
     {
         class GetJSON extends AsyncTask<Void, Void, String> {

             ProgressDialog pDialog = new ProgressDialog(ClientRegistration.this);



             @Override
             protected void onPreExecute() {
                 super.onPreExecute();
                 pDialog.setMessage("Registering you...");
                 pDialog.setCancelable(false);
                 pDialog.show();
             }

             @Override
             protected String doInBackground(Void... params) {
                 RequestHandler rh = new RequestHandler();
                 HashMap<String, String> paramms = new HashMap<>();
                 paramms.put("name", name);
                 paramms.put("gender", gender);
                 paramms.put("phone", phone);
                 paramms.put("password", password);
                 String s = rh.sendPostRequest(URLs.main+"user_reg.php", paramms);
                 return s;

             }

             @Override
             protected void onPostExecute(String s) {
                 super.onPostExecute(s);
                 pDialog.dismiss();

                 if(s.equals("1"))
                 {
                     AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ClientRegistration.this);
                     alertDialogBuilder.setTitle("Attention!").setMessage("Registration success.")
                             .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialogInterface, int i) {

                                 }
                             })
                             .setCancelable(true).show();
                 }
                 else {
                     AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ClientRegistration.this);
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
