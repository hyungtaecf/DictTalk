package com.example.hyungtaecfigur.dicttalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class SearchUsersActivity extends AppCompatActivity {
    public static final String USER_ID = "com.example.hyungtaecfigur.dicttalk.userid";

    //a list to store que query of users
    private List<User> userList;

    private DatabaseReference databaseUsers;

    private EditText editTextSearchUsers;
    private Button buttonSearchUsers;
    private ListView listViewSearchUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        initialize();

    }

    private void initilizeToolbarAndBottomNavigation(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //TODO Ver como faz para iniciar a activity com um item específico selecionado
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

    private void initialize(){
        initilizeToolbarAndBottomNavigation();

        //getting the reference of users node
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        //getting views
        editTextSearchUsers = (EditText) findViewById(R.id.editTextSearchUsers);
        buttonSearchUsers = (Button) findViewById(R.id.buttonSearchUsers);
        listViewSearchUsers = (ListView) findViewById(R.id.listViewSearchUsers);

        //list to store users
        userList = new ArrayList<>();

        //attaching listener to listview
        listViewSearchUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected user
                User user = userList.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);

                //putting user id to intent
                intent.putExtra(USER_ID, user.getId());

                //starting the activity with intent
                startActivity(intent);
            }
        });

        buttonSearchUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUser(new FirebaseCallback<List<User>>() {
                    @Override
                    public void onCallback(List<User> data) {
                        if(!userList.isEmpty()) {
                            //creating adapter
                            UserList userAdapter = new UserList(SearchUsersActivity.this, userList);
                            //attaching adapter to the listview
                            listViewSearchUsers.setAdapter(userAdapter);
                        }
                    }
                });
            }
        });
    }

    public void getUser(@NonNull final FirebaseCallback<List<User>> firebaseCallback){
        Query userQuery = databaseUsers.orderByChild("nickname").startAt(editTextSearchUsers.getText().toString()).endAt(editTextSearchUsers.getText().toString() + "\uf8ff");

        //attaching value event listener
        //TODO FIX: Not working after this line
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous user list
                userList.clear();

                if(dataSnapshot.exists()) {
                    //iterating through all the nodes
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //getting user
                        User user = postSnapshot.getValue(User.class);

                        //adding user to the list
                        userList.add(user);
                    }
                }
                firebaseCallback.onCallback(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface FirebaseCallback<T> {
        void onCallback(T data);
    }
}
