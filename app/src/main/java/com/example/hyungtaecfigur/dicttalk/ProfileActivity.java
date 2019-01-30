package com.example.hyungtaecfigur.dicttalk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity /*implements View.OnClickListener*/ {

    private CircleImageView imageViewProfilePicture;
    private TextView textViewUsername;
    private TextView textViewReputation;
    private TextView textViewBio;
    private TextView textViewLikesCount;
    private TextView textViewDislikesCount;
    private TextView textViewTranslationsCount;
    private TextView textViewSentencesCount;
    private TextView textViewVerifiedCount;

    private ImageButton buttonFriend;
    private ImageButton buttonChat;
    private ImageButton buttonLists;

    private DatabaseReference refUsers;
    private DatabaseReference refFriends;

    private String currentFirebaseUserUId;

    private User thisUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();

        getUser(new FirebaseCallback<User>() {
            @Override
            public void onCallback(User data) {
                Log.i("ProfileActivity", "onDataChange: " + thisUser.getNickname());
                //TODO LEMBRAR QUE SE O USUARIO QUE APARECE FOR O PRÓPRIO USUARIO, TEM QUE IR PARA O MyProfileActivity
                if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(thisUser.getId())){
                    finish();
                    startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
                }
                //setting textViews and image according to the searched User
                textViewUsername.setText(thisUser.getNickname());
                textViewBio.setText(thisUser.getBio());
                textViewDislikesCount.setText(String.valueOf(thisUser.getDislikes()));
                textViewLikesCount.setText(String.valueOf(thisUser.getLikes()));
                textViewSentencesCount.setText(String.valueOf(thisUser.getSentences()));
                textViewTranslationsCount.setText(String.valueOf(thisUser.getTranslations()));
                textViewVerifiedCount.setText(String.valueOf(thisUser.getVerified()));
                textViewReputation.setText(String.valueOf(thisUser.getReputation()));
                Glide.with(getApplicationContext()).load(thisUser.getImage()).into(imageViewProfilePicture);

                isFriend(new FirebaseCallback<Boolean>() {
                    @Override
                    public void onCallback(Boolean isFriend) {
                        if(isFriend){
                            buttonFriend.setImageResource(R.drawable.ic_x);
                        }else{
                            buttonFriend.setImageResource(R.drawable.ic_add_friend);
                        }
                    }
                });

                buttonFriend.setOnClickListener(new View.OnClickListener() {
                    //TODO Considerar apagar todas as conversas ao desfazer amizade
                    @Override
                    public void onClick(View v) {
                        //TODO CONTINUAR
                        refFriends.child(currentFirebaseUserUId).child(thisUser.getId()).
                                addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            refFriends.child(currentFirebaseUserUId).child(thisUser.getId()).removeValue();
                                            buttonFriend.setImageResource(R.drawable.ic_add_friend);
                                        } else {
                                            refFriends.child(currentFirebaseUserUId).child(thisUser.getId()).setValue(true);
                                            buttonFriend.setImageResource(R.drawable.ic_x);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                });
            }
        });

    }
    public void initializeToolbarAndBottomNavigation(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_mainMenu:
                                startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
                                break;
                            case R.id.action_notifications:
                                startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                                break;
                            case R.id.action_profile:
                                startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
                                break;
                        }
                        return true;
                    }
                });
    }
    public void initialize(){
        initializeToolbarAndBottomNavigation();
        imageViewProfilePicture = (CircleImageView) findViewById(R.id.imageViewProfilePicture);
        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewReputation = (TextView) findViewById(R.id.textViewReputation);
        textViewBio = (TextView) findViewById(R.id.textViewBio);
        textViewLikesCount = (TextView) findViewById(R.id.textViewLikesCount);
        textViewDislikesCount = (TextView) findViewById(R.id.textViewDislikesCount);
        textViewTranslationsCount = (TextView) findViewById(R.id.textViewTranslationsCount);
        textViewSentencesCount = (TextView) findViewById(R.id.textViewSentencesCount);
        textViewVerifiedCount = (TextView) findViewById(R.id.textViewVerifiedCount);
        buttonFriend = (ImageButton) findViewById(R.id.buttonFriend);
        buttonChat = (ImageButton) findViewById(R.id.buttonChat);
        buttonLists = (ImageButton) findViewById(R.id.buttonLists);

        currentFirebaseUserUId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        refUsers = FirebaseDatabase.getInstance().getReference("users");
        refFriends = FirebaseDatabase.getInstance().getReference("friends");
    }
    public void getUser(@NonNull final FirebaseCallback<User> firebaseCallback){
        final Intent intent = getIntent();
        refUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        if (user.getKey().equals(intent.getStringExtra(SearchUsersActivity.USER_ID))) {
                            thisUser = user.getValue(User.class);
                            break;
                        }
                    }
                    firebaseCallback.onCallback(thisUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }
    public void isFriend(@NonNull final FirebaseCallback<Boolean> firebaseCallback) {

        refFriends.child(currentFirebaseUserUId).child(thisUser.getId()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            firebaseCallback.onCallback(true);
                        }else{
                            firebaseCallback.onCallback(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public interface FirebaseCallback<T> {
        void onCallback(T data);// T means generic data type
    }
}
