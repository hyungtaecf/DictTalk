package com.example.hyungtaecfigur.dicttalk;

import android.content.Intent;
import android.os.Bundle;
import android.os.WorkSource;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WordMeaningsActivity extends AppCompatActivity {
    public static final String MEANING_ID = "com.example.hyungtaecfigur.dicttalk.meaningid";
    public static final String WORD_ID = "com.example.hyungtaecfigur.dicttalk.wordid";
    public static final String DICTIONARY_ID = "com.example.hyungtaecfigur.dicttalk.dictionaryid";
    public static final String WORD_TEXT = "com.example.hyungtaecfigur.dicttalk.wordtext";

    private ListView listViewMeanings;
    private TextView selectMeaningLabel;

    private DatabaseReference databaseMeanings;

    private List<MeaningItem> items;

    private long index; //index auxiliary variable to create the list of items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meanings);
        //TODO SE TIVER APENAS UM MEANING, IR DIRETO PARA ELE DE ACORDO COM O DICIONÁRIO DE ONDE VEIO, E NAO ESCOLHER -> CONFIGURAR

        //TODO ADDITIONAL HORIZONTAL SCROLL FOR HASHTAGS

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

    private void initialize() {
        initilizeToolbarAndBottomNavigation();

        listViewMeanings = (ListView) findViewById(R.id.listViewMeanings);
        selectMeaningLabel = (TextView) findViewById(R.id.selectMeaningLabel);

        databaseMeanings = FirebaseDatabase.getInstance().getReference("meanings");

        items = new ArrayList<>();

        index = 0;

        selectMeaningLabel.setText(String.format(getResources().getString(R.string.select_meaning1)
                + getIntent().getStringExtra(WORD_TEXT) + getResources().getString(R.string.select_meaning2)));

        getMeaningItemList(new FirebaseCallback<List<MeaningItem>>() {
            @Override
            public void onCallback(List<MeaningItem> items) {
                //creating adapter
                MeaningList adapter = new MeaningList(WordMeaningsActivity.this, items);
                //attaching adapter to the listview
                listViewMeanings.setAdapter(adapter);
            }
        });
        listViewMeanings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeaningItem item = items.get(position);

                Intent intent = new Intent(getApplicationContext(), WordActivity.class);
                intent.putExtra(MEANING_ID, item.getMeaning_id());
                intent.putExtra(DICTIONARY_ID, item.getDictionary_id());
                intent.putExtra(WORD_ID, item.getWord_id());
                intent.putExtra(WORD_TEXT, item.getWord());
                finish();
                startActivity(intent);
            }
        });
    }

    public void getDictionary(String dictionary_id, final FirebaseCallback<Dictionary> firebaseCallback){
        FirebaseDatabase.getInstance().getReference("dictionaries").child(dictionary_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Dictionary dictionary = null;
                        if(dataSnapshot.exists()){
                            dictionary = dataSnapshot.getValue(Dictionary.class);
                        }
                        firebaseCallback.onCallback(dictionary);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getWord(final String language_id, final FirebaseCallback<Word> firebaseCallback) {
        FirebaseDatabase.getInstance().getReference("words").orderByChild("text")
                .equalTo(getIntent().getStringExtra(DictionarySearchActivity.WORD_TEXT))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Word word = new Word();
                if(dataSnapshot.exists()) {
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        if(postSnapshot.child("language_id").getValue().toString().equals(language_id)) {
                            word = postSnapshot.getValue(Word.class);
                        }
                    }
                }
                firebaseCallback.onCallback(word);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMeaningItemList(@NonNull final FirebaseCallback<List<MeaningItem>> firebaseCallback) {
        databaseMeanings.child(getIntent().getStringExtra(DictionarySearchActivity.WORD_ID))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final long listSize = dataSnapshot.getChildrenCount();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                index++;
                                final Meaning meaning = postSnapshot.getValue(Meaning.class);
                                //TODO Continuar tendo como base o translation list do word activity
                                getDictionary(meaning.getDictionary_id(), new FirebaseCallback<Dictionary>() {
                                    @Override
                                    public void onCallback(final Dictionary dictionary) {
                                        getWord(dictionary.getFirstLanguage(), new FirebaseCallback<Word>() {
                                            @Override
                                            public void onCallback(Word word) {
                                                //TODO se der certo, implementar esse sistema no WordActivity tambem
                                                items.add(new MeaningItem(dictionary.getSecondLanguage(),
                                                        word.getText(), meaning.getText(), meaning.getId(),
                                                        word.getId(), dictionary.getId()));
                                                if(index>=listSize) {
                                                    firebaseCallback.onCallback(items);
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        } else {
                            Log.i("WordMeaningsActivity", "onDataChange: Meaning not found");
                        }
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
