package com.example.hyungtaecfigur.dicttalk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.hyungtaecfigur.dicttalk.fragment.AccountFragment;
import com.example.hyungtaecfigur.dicttalk.fragment.ChatFragment;
import com.example.hyungtaecfigur.dicttalk.fragment.PeopleFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class DictChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict_chat);

        BottomNavigationView bottomNavigationView = findViewById(R.id.mainActivity_bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_chat: {
                        getFragmentManager().beginTransaction().replace(R.id.mainActivity_frameLayout, new ChatFragment()).commit();
                        return true;
                    }
                    case R.id.action_people: {
                        getFragmentManager().beginTransaction().replace(R.id.mainActivity_frameLayout, new PeopleFragment()).commit();
                        return true;
                    }
                    case R.id.action_account: {
                        getFragmentManager().beginTransaction().replace(R.id.mainActivity_frameLayout, new AccountFragment()).commit();
                    }
                }

                return false;
            }
        });

        passPushTokenToServer();
    }

    //푸시
    void passPushTokenToServer() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String, Object> map = new HashMap<>();
        map.put("pushToken", token);

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);
    }
}
