package com.example.hyungtaecfigur.dicttalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CommunityMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);

        Button searchListsButton = (Button) findViewById(R.id.searchListsButton);
        searchListsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchListsIntent = new Intent(getApplicationContext(), SearchListsActivity.class);
                startActivity(searchListsIntent);
            }
        });

        Button searchUsersButton = (Button) findViewById(R.id.searchUsersButton);
        searchUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchUsersIntent = new Intent(getApplicationContext(), SearchUsersActivity.class);
                startActivity(searchUsersIntent);
            }
        });

        Button chatMainButton = (Button) findViewById(R.id.chatMainButton);
        chatMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(getApplicationContext(), DictChatActivity.class); //Chat main activity
                startActivity(chatIntent);
            }
        });
    }
}
