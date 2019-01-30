package com.example.hyungtaecfigur.dicttalk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class NotificationsActivity extends AppCompatActivity {

    //TODO Notifications when friends make a request
    //TODO Notifications when someone answers a request
    //TODO Notifications when someone like a contribution
    //TODO Notifications when someone dislike a contribution
    //TODO Notifications when someone comment a contribution
    //TODO Notifications when someone makes a mention
    //TODO Notifications when someone add a contribution to a word that the user added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        initializeToolbarAndBottomNavigation();
    }

    public void initializeToolbarAndBottomNavigation() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
}
