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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {
    public static final String BIO = "com.example.hyungtaecfigur.dicttalk.bio";

    private Button buttonLogout;
    private ImageButton buttonEditProfile;
    private TextView textViewUsername;
    private TextView textViewDislikesCount;
    private TextView textViewLikesCount;
    private TextView textViewSentencesCount;
    private TextView textViewTranslationsCount;
    private TextView textViewVerifiedCount;
    private TextView textViewBio;
    private TextView textViewReputation;
    private CircleImageView imageViewProfilePicture;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //TODO Ver como faz para iniciar a activity com um item específico selecionado
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_mainMenu:
                        finish();
                        startActivity(new Intent(getApplicationContext(),MainMenuActivity.class));
                        break;
                    case R.id.action_notifications:
                        finish();
                        startActivity(new Intent(getApplicationContext(),NotificationsActivity.class));
                        break;
                    case R.id.action_profile:
//                        Toast.makeText(MyProfileActivity.this, "Action PROFILE clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        //initializing views
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonEditProfile = (ImageButton) findViewById(R.id.buttonEditProfile);
        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewDislikesCount = (TextView) findViewById(R.id.textViewDislikesCount);
        textViewLikesCount = (TextView) findViewById(R.id.textViewLikesCount);
        textViewSentencesCount = (TextView) findViewById(R.id.textViewSentencesCount);
        textViewTranslationsCount = (TextView) findViewById(R.id.textViewTranslationsCount);
        textViewVerifiedCount = (TextView) findViewById(R.id.textViewVerifiedCount);
        textViewBio = (TextView) findViewById(R.id.textViewBio);
        textViewReputation = (TextView) findViewById(R.id.textViewReputation);
        imageViewProfilePicture = (CircleImageView) findViewById(R.id.imageViewProfilePicture);



        //Getting user from database
        firebaseAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("users");
        ValueEventListener myProfileActivity = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        if (user.getKey().equals(firebaseAuth.getCurrentUser().getUid())) {
                            currentUser = user.getValue(User.class);
                            break;
                        }
                    }
                    if(currentUser==null){
                        Log.i("MyProfileActivity", "onDataChange: No user with UID like " + firebaseAuth.getCurrentUser().getUid());
                        //simulates clicking Log out Button (in order to save code since it`s the same function)
                        buttonLogout.performClick();
                    }
                }
                Log.i("MyProfileActivity", "onDataChange: " + currentUser.getNickname());
                //setting textViews and image according to the Current User
                textViewUsername.setText(currentUser.getNickname());
                textViewBio.setText(currentUser.getBio());
                textViewDislikesCount.setText(String.valueOf(currentUser.getDislikes()));
                textViewLikesCount.setText(String.valueOf(currentUser.getLikes()));
                textViewSentencesCount.setText(String.valueOf(currentUser.getSentences()));
                textViewTranslationsCount.setText(String.valueOf(currentUser.getTranslations()));
                textViewVerifiedCount.setText(String.valueOf(currentUser.getVerified()));
                textViewReputation.setText(String.valueOf(currentUser.getReputation()));
                Glide.with(getApplicationContext()).load(currentUser.getImage()).into(imageViewProfilePicture);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                intent.putExtra(BIO, currentUser.getBio());
                finish();
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logging out the user
                firebaseAuth.signOut();
                //closing activity
                finish();
                //starting Home Screen
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
