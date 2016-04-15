package com.example.yishafang.healthpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yishafang.healthpro.API.SignUpAndInAPI;
import com.example.yishafang.healthpro.Model.CurDoctor;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;


/**
 * @author yishafang on 9/10/15.
 */
public class SignUpActivity extends FragmentActivity {

    private static final String TAG = "SignUpActivity";

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText passwordConfirmField;

    private String name;
    private String email;
    private String password;
    private String passwordConfirmed;

    private boolean isSamePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        nameField = (EditText) findViewById(R.id.user_name);
        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
        passwordConfirmField = (EditText) findViewById(R.id.password_confirm);

        Button signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputInfo();

                if (isSamePassword) {
                    signUp();
                } else {
                    showAlertDialog("Password is not same!", "Please check your password.");

                    passwordField.setText("");
                    passwordConfirmField.setText("");
                }
            }
        });

        Button toSignInScreen = (Button) findViewById(R.id.to_sign_in_button);
        toSignInScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signUp(){
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", name);
            requestBody.put("password", password);
            requestBody.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TypedInput typedInput = new TypedByteArray("application/json", requestBody.toString().getBytes());

        //RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(baseAPI).build();

        SignUpAndInAPI api = ServiceFactory.createService(SignUpAndInAPI.class);

        //SignUpAndInAPI api = restAdapter.create(SignUpAndInAPI.class);
        api.signUp(typedInput, new Callback<CurDoctor>() {
            @Override
            public void success(CurDoctor doctor, Response response) {
                Log.w(TAG, "Sign up successfully !!");

                // TODO: deal with the sign up with same email situation

                Context context = getApplicationContext();
                CharSequence text = "Sign up successfully! Please sign in.";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                toast.show();

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w(TAG + "=[", error);
                error.printStackTrace();
            }
        });
    }

    private void getInputInfo() {
        name = nameField.getText().toString();
        email = emailField.getText().toString();
        password = passwordField.getText().toString();
        passwordConfirmed = passwordConfirmField.getText().toString();

        // check if input is valid name, email or password
        if (name.equals("")) {
            showAlertDialog("Username is empty!", "Please enter your username.");
        } else if (email.equals("")) {
            showAlertDialog("Email is empty!", "Please enter your email.");
        } else if (password.equals("")) {
            showAlertDialog("Password is empty!", "Please enter your password.");
            isSamePassword = false;
        } else {
            isSamePassword = password.equals(passwordConfirmed);
        }
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message + "\nClick OK to close.").
                setCancelable(false).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // just close the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
