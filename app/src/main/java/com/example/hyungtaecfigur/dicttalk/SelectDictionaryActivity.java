package com.example.hyungtaecfigur.dicttalk;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SelectDictionaryActivity extends AppCompatActivity {
    public static final String DICTIONARY_NAME = "com.example.hyungtaecfigur.dicttalk.dictionaryname";
    public static final String DICTIONARY_ID = "com.example.hyungtaecfigur.dicttalk.dictionaryid";
    //TODO FIX: problema que tem que selecionar duas vezes o spinner

    private Spinner spinnerDictionaryLanguage;
    private ListView listViewDictionaries;

    private DatabaseReference dictionaries;
    private DatabaseReference languages_dictionaries;
    private DatabaseReference currentLanguage; //Para comparar com a linguagem escolhida no spinner

    private String dictionary_id;
    private String language;
    private String spinnersPrevItem; // In order to compare if the value of the spinner was changed

    //a list to store que query of dictionaries
    private List<Dictionary> dictionaryList;

    //private List<String> dictionary_idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dictionary);

        initialize();
        getAndShowDictionaries();
    }

    public void initialize(){
        setToolbarAndBottomNavigation();//TODO IMPLEMENT THIS IN EVERY ACTIVITY

        dictionaries = FirebaseDatabase.getInstance().getReference("dictionaries");
        languages_dictionaries = FirebaseDatabase.getInstance().getReference("languages_dictionaries");
        currentLanguage = FirebaseDatabase.getInstance().getReference("string_translations/languages")
                .child(Locale.getDefault().getDisplayLanguage());
        //TODO Se o sistema tiver um idioma diferente de portugues, ingles, ou coreano, da problema, entao tem que botar um padrao

        spinnerDictionaryLanguage = (Spinner) findViewById(R.id.spinnerDictionaryLanguage);
        listViewDictionaries = (ListView) findViewById(R.id.listViewDictionaries);

        spinnersPrevItem = spinnerDictionaryLanguage.getSelectedItem().toString();
        //list to store dictionaries
        dictionaryList = new ArrayList<>();

        listViewDictionaries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //getting the selected dictionary
                Dictionary dictionary = dictionaryList.get(position);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), DictionarySearchActivity.class);

                //putting dictionary name and id to intent
                intent.putExtra(DICTIONARY_ID, dictionary.getId());
                intent.putExtra(DICTIONARY_NAME, dictionary.getFirstLanguage() + " - " + dictionary.getSecondLanguage());

                //starting the activity with intent
                startActivity(intent);
            }
        });
    }

    private void setToolbarAndBottomNavigation() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_mainMenu:
                        startActivity(new Intent(getApplicationContext(),MainMenuActivity.class));
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
    }

    private void translateSpinner(final FirebaseCallback callback) {
        //Getting translations of spinners
        language = null;
        currentLanguage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.getKey().equals(spinnerDictionaryLanguage.getSelectedItem().toString())){
                            language = postSnapshot.getValue(String.class);
                            break;
                        }
                    }
                    if(language == null){
                        Log.i("SelectDictionary", "onDataChange (language): No Language like "
                                + spinnerDictionaryLanguage.getSelectedItem().toString());
                    }
                }
                Log.i("WordRegistration", "onDataChange (spinnerDictionaryLanguage): "
                        + spinnerDictionaryLanguage.getSelectedItem().toString());
                callback.onCallback(language);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    private void getAndShowDictionaries(){
        spinnerDictionaryLanguage.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        //If the spinner item changed
                        if(!spinnersPrevItem.equals(spinnerDictionaryLanguage.getSelectedItem().toString())) {
                            spinnersPrevItem = spinnerDictionaryLanguage.getSelectedItem().toString();
                            translateSpinner(new FirebaseCallback<String>() {
                                @Override
                                public void onCallback(String data) {
                                    //query to get dictionary_id's referring to the selected language
                                    if(language!=null) {
                                        getDictionaryList(new FirebaseCallback<List<Dictionary>>() {
                                            @Override
                                            public void onCallback(List<Dictionary> data) {
                                                //creating adapter
                                                DictionaryList dictionaryAdapter = new DictionaryList(SelectDictionaryActivity.this, dictionaryList);
                                                //Wait 1 second because por some reason it doesn't appears in the first time it not wait
                                                try {
                                                    TimeUnit.SECONDS.sleep(1);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                //attaching adapter to the listview
                                                listViewDictionaries.setAdapter(dictionaryAdapter);
                                            }
                                        });
                                    } else{
                                        System.out.println("SelectDictionaryActivity: language is null");
                                    }
                                }
                            });
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }

    public void getDictionaryList(final FirebaseCallback callback) {
        Query query = languages_dictionaries.child(language);

        //attaching value event listener
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing dictionary list
                dictionaryList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    dictionary_id = postSnapshot.getValue(String.class);
                    //getting dictionary_id and adding it to the list
                    //dictionary_idList.add(postSnapshot.getValue(String.class));

                    dictionaries.child(dictionary_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dictionaryList.add(dataSnapshot.getValue(Dictionary.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                callback.onCallback(dictionaryList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public interface FirebaseCallback<T> {
        void onCallback(T data);// T means generic data type
    }
}

