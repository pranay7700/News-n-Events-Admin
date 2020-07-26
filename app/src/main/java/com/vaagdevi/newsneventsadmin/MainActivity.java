package com.vaagdevi.newsneventsadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private DatabaseReference databaseref;
    private FirebaseAuth mAuth;

    EditText Emailid;
    EditText Passid;
    Button login;
    TextView forgotpassid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Emailid = (EditText) findViewById(R.id.ETemailId);
        Passid = (EditText) findViewById(R.id.ETpassId);
        login = (Button) findViewById(R.id.BTNlogin);


        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(MainActivity.this);

        @SuppressLint("WrongViewCast") final AppCompatCheckBox checkBox = (AppCompatCheckBox) findViewById(R.id.show_hide_password);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // show password
                    Passid.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    Passid.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setTitle("Validating");
                progressDialog.setMessage("Please wait...");
                checkConnection();

                String emailid = Emailid.getText().toString();
                final String passid = Passid.getText().toString();

                if (emailid.isEmpty() && passid.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (emailid.isEmpty()) {
                    Emailid.setError("Provide Your Email");
                    Emailid.requestFocus();
                } else if (passid.isEmpty()) {
                    Passid.setError("Enter Your Password");
                    Passid.requestFocus();
                } else if (!(emailid.isEmpty() && passid.isEmpty())) {

                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(emailid, passid)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Welcome to News n Events Admin !",
                                                Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(MainActivity.this, Dashboard.class));
                                        finish();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Signin failed! Try again",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }


            }


        });
    }

    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (null == activeNetwork) {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(MainActivity.this, "Welcome to News n Events Admin", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, Dashboard.class));
            finish();
        }
        super.onStart();
    }
}

