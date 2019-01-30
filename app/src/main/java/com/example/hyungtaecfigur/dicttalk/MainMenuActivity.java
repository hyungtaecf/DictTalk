package com.example.hyungtaecfigur.dicttalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //TODO Ver como faz para iniciar a activity com um item específico selecionado
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_mainMenu:
//                        startActivity(new Intent(getApplicationContext(),MainMenuActivity.class));
                        break;
                    case R.id.action_notifications:
                        startActivity(new Intent(getApplicationContext(),NotificationsActivity.class));
                        break;
                    case R.id.action_profile:
                        startActivity(new Intent(getApplicationContext(),MyProfileActivity.class));
                        break;
                }
                return true;
            }
        });

        ImageButton dictionaryMainButton = (ImageButton) findViewById(R.id.buttonDictionaryMain);
        dictionaryMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), SelectDictionaryActivity.class));
            }
        });

        ImageButton myListsMainButton = (ImageButton) findViewById(R.id.buttonMyListsMain);
        myListsMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), MyListsMainActivity.class));
            }
        });

        ImageButton wordsOfTheDayMainButton = (ImageButton) findViewById(R.id.buttonWordsOfTheDayMainButton);
        wordsOfTheDayMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wordsOfTheDayIntent = new Intent(getApplicationContext(), WordsOfTheDayMainActivity.class);
                finish();
                startActivity(wordsOfTheDayIntent);
            }
        });

        ImageButton commmunityMainButton = (ImageButton) findViewById(R.id.buttonCommunityMain);
        commmunityMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent communityIntent = new Intent(getApplicationContext(), CommunityMainActivity.class);
                finish();
                startActivity(communityIntent);
            }
        });

    }
}
