package com.example.hyungtaecfigur.dicttalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextNickname;
    private Button buttonSignup;

    private TextView textViewSignin;

    private ProgressDialog progressDialog;


    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    //our database reference object
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //getting the reference of users node
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
        }

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextNickname = (EditText) findViewById(R.id.editTextNickname);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    private void registerUser(){

        //getting email and password from edit texts
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering, Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //TODO Registrar caminho user
                            addUser();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
                        }else{
                            //display some message here
                            Toast.makeText(RegistrationActivity.this,
                                    "Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view) {

        if(view == buttonSignup){
            registerUser();
        }

        if(view == textViewSignin){
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
    private void addUser() {
        //getting the values to save
        String email = editTextEmail.getText().toString().trim().toLowerCase();
        String nickname = editTextNickname.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim();

        //checking if the value is provided
        if (!TextUtils.isEmpty(email)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our User
            String id = firebaseAuth.getCurrentUser().getUid();

            //creating an User Object
            User user = new User(id, nickname, password, email);

            //Saving the User
            databaseUsers.child(id).setValue(user);

            //setting edittext to blank again
            editTextEmail.setText("");
            editTextNickname.setText("");
            editTextPassword.setText("");
            //displaying a success toast
            Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter the informations", Toast.LENGTH_LONG).show();
        }
    }

    //TODO implement the delete method in the profile
    //TODO look for how to delete also in the Firebase auth

    private boolean deleteUser(String id) {
        //getting the specified user reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(id);

        //removing user
        dR.removeValue();

        //TODO VERIFY ALL THE RELATIONS WITH "USERS" IN THE DATABASE

        Toast.makeText(getApplicationContext(), "User Deleted", Toast.LENGTH_LONG).show();

        return true;
    }

    private boolean updateUser(String id, String nickname, String bio, int verified, String password,
                               String email, String image, int reputation, int likes, int dislikes,
                               int comments, int translations, int sentences, int synonym, int antonym,
                               int meaning, int words) {
        //getting the specified user reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(id);

        //TODO GET ALL THE DATA FROM FIREBASE'S PATH USERS

        //updating user
        User user = new User(id, nickname, bio, verified, password, email, image, reputation, likes,
                dislikes, comments, translations, sentences, synonym, antonym, meaning, words);
        dR.setValue(user);
        Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_LONG).show();
        return true;
    }
}

