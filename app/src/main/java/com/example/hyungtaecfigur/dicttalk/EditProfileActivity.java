package com.example.hyungtaecfigur.dicttalk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

//TODO Storage and Retrieving of images in an EditProfileActivity

public class EditProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;

    private ImageButton buttonAddPhoto;
    private ImageButton buttonCancel;
    private Button buttonSaveChanges;
    private SquareImageView imageViewProfilePicture;

    private EditText editTextBio;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //TODO Ver como faz para iniciar a activity com um item específico selecionado
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_mainMenu:
                        startActivity(new Intent(getApplicationContext(),MainMenuActivity.class));
                        break;
                    case R.id.action_notifications:
                        startActivity(new Intent(getApplicationContext(),MainMenuActivity.class));
                        break;
                    case R.id.action_profile:
//                        Toast.makeText(EditProfileActivity.this, "Action PROFILE clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getUid());

        buttonAddPhoto = (ImageButton) findViewById(R.id.buttonAddPhoto);
        buttonCancel = (ImageButton) findViewById(R.id.buttonCancel);
        buttonSaveChanges = (Button) findViewById(R.id.buttonSaveChanges);
        imageViewProfilePicture = (SquareImageView) findViewById(R.id.imageViewProfilePicture) ;
        editTextBio = (EditText) findViewById(R.id.editTextBio);

        firebaseAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("users");

        editTextBio.setText(getIntent().getStringExtra(MyProfileActivity.BIO));

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        if (user.getKey().equals(firebaseAuth.getCurrentUser().getUid())) {
                            currentUser = user.getValue(User.class);
                            break;
                        }
                    }
                    if (currentUser == null) {
                        Log.i("EditProfileActivity", "onDataChange: No user with UID like " + firebaseAuth.getCurrentUser().getUid());
                        //logging out the user
                        firebaseAuth.signOut();
                        //closing activity
                        finish();
                        //starting Home Screen
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }
                Log.i("EditProfileActivity", "onDataChange: " + currentUser.getNickname());
                //setting image
                Glide.with(getApplicationContext()).load(currentUser.getImage()).into(imageViewProfilePicture);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),MyProfileActivity.class));
            }
        });

        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(currentUser.getId()).child("bio").setValue(editTextBio.getText()+"");
                Toast.makeText(EditProfileActivity.this, "The changes were successfully saved", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(),MyProfileActivity.class));
            }
        });
    }
}
