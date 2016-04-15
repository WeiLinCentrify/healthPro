package com.example.yishafang.healthpro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yishafang.healthpro.API.SignUpAndInAPI;
import com.example.yishafang.healthpro.Model.CurDoctor;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

import static com.example.yishafang.healthpro.Constants.DOCTOR_ID;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private Button signInButton;
    private Button toSignUpButton;
    private EditText usernameField;
    private EditText passwordField;

    private String username;
    private String password;

    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        usernameField = (EditText) findViewById(R.id.user_name);
        passwordField = (EditText) findViewById(R.id.password);

        signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "signIn button clicked");

                username = usernameField.getText().toString();
                password = passwordField.getText().toString();

//                username = "doctor";
//                password = "12345";

                if (username.equals("")) {
                    showAlertDialog("Empty Username", "Please enter your username.");
                } else if (password.equals("")) {
                    showAlertDialog("Empty Password", "Please enter your password.");
                } else {
                    signIn(username, password);
                }
            }
        });

        toSignUpButton = (Button) findViewById(R.id.to_sign_up_button);
        toSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog(String title, String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set title
        alertDialogBuilder.setTitle(title);

        // Set dialog message
        alertDialogBuilder.setMessage(message + "\nClick OK to close.").
                setCancelable(false).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // just close the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // Create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Show it
        alertDialog.show();
    }

    private void signIn(final String username, final String password) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        TypedInput typedInput = new TypedByteArray("application/json", requestBody.toString().getBytes());
        SignUpAndInAPI api = ServiceFactory.createService(SignUpAndInAPI.class);
        api.login(typedInput, new Callback<CurDoctor>() {
            @Override
            public void success(CurDoctor doctor, Response response) {
                Log.i(TAG, "login success!!"+ doctor.getId());
                doctor.setUsername(username);
                doctor.setPassword(password);
                sessionManager = new UserSessionManager(getApplicationContext());
                sessionManager.createUserLoginSession(doctor);
                int doctorId = doctor.getId();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(DOCTOR_ID, doctorId);
                startActivity(intent);

                // TODO: deal with wrong sign in situation
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w(TAG + ":=[ ", error);
            }
        });
    }

}
