package code0.gym;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class ResetPassword extends AppCompatActivity {
EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        username =  findViewById(R.id.editText15);
        password= findViewById(R.id.editText16);
        final int selected_item = Integer.parseInt(getIntent().getStringExtra("selected"));

 findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         reset_password(username.getText().toString(), password.getText().toString(), selected_item);
     }
 });


    }

    public void reset_password(final String username, final String password , final int selected_item)
    {


        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(ResetPassword.this);

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                progressDialog.setMessage("Resetting password...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> employees = new HashMap<>();
                employees.put("username", username);
                employees.put("password", password);
                employees.put("selected", String.valueOf(selected_item));
                String res=rh.sendPostRequest(URLs.main+"pass_reset.php",employees);
                return res;

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                Toast.makeText(ResetPassword.this, s, Toast.LENGTH_SHORT).show();
                if(s.equals("1"))
                {

                    final  AlertDialog.Builder builder = new AlertDialog.Builder(ResetPassword.this);
                    builder.setMessage("Password reset success!");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                            ResetPassword.this.finish();

                        }
                    })
                            .show();
                }
                else
                {
                    final  AlertDialog.Builder builder = new AlertDialog.Builder(ResetPassword.this);
                    builder.setMessage("Password reset failed");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                            ResetPassword.this.finish();

                        }
                    }).setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    })
                            .show();
                }







            }


        }
        GetJSON jj =new GetJSON();
        jj.execute();


    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);

        } catch (Exception ex) {

        }
    }
}
