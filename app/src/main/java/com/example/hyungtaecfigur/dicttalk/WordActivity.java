package com.example.hyungtaecfigur.dicttalk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WordActivity extends AppCompatActivity {

    private EditText editTextSearchWord;

    private Button buttonSearch;
    private Button buttonAddNew;

    private ImageButton buttonAddToList;
    private ImageButton buttonTranslations;
    private ImageButton buttonSentences;
    private ImageButton buttonSynonyms;
    private ImageButton buttonAntonyms;
    private ImageButton buttonHashtags;
    private ImageButton buttonComments;
    private ImageButton buttonReport;

    private TextView textViewWord;
    private TextView textViewWordDescription;

    private ListView listViewTranslations;

    private DatabaseReference databaseMeanings;
    private DatabaseReference databaseTranslations;

    private Meaning meaning;

    private List<TranslationItem> translationItemList;

    private long index; //auxiliary variable to get the translation items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        initialize();

        getMeaning(new FirebaseCallback<Meaning>() {
            @Override
            public void onCallback(Meaning data) {
                textViewWord.setText(getIntent().getStringExtra(WordMeaningsActivity.WORD_TEXT));
                textViewWordDescription.setText(meaning.getText());

                getTranslationItemList(new FirebaseCallback<List<TranslationItem>>() {
                    @Override
                    public void onCallback(List<TranslationItem> data) {
                        //TODO Change the adapter to TranslationItemList
                        //creating adapter
                        TranslationList translationAdapter = new TranslationList(WordActivity.this, translationItemList);
                        //attaching adapter to the listview
                        listViewTranslations.setAdapter(translationAdapter);
                    }
                });
            }
        });

        buttonAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO pegar o meaning_id, palavra(String) e tradução(String) e salvar numa lista
            }
        });
        //TODO passar para um método e fazer os esquemas
    }

    public void getMeaning(@NonNull final FirebaseCallback<Meaning> firebaseCallback){
        //attaching value event listener
        databaseMeanings.child(getIntent().getStringExtra(WordMeaningsActivity.WORD_ID))
                .orderByChild(getIntent().getStringExtra(WordMeaningsActivity.MEANING_ID))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                meaning = null;
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting meaning
                    meaning = postSnapshot.getValue(Meaning.class);
                }
                firebaseCallback.onCallback(meaning);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    public void getTranslationItemList(@NonNull final FirebaseCallback<List<TranslationItem>> firebaseCallback){
        //TODO Order by Likes
        getTranslationLanguage(new FirebaseCallback<String>() {
            @Override
            public void onCallback(final String translationLanguage) {
                Query translationQuery = databaseTranslations.child(getIntent().getStringExtra(WordMeaningsActivity.WORD_ID))
                        .child(translationLanguage).child(getIntent().getStringExtra(WordMeaningsActivity.MEANING_ID)).orderByChild("likes");
                translationQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //clearing the previous translationItem list
                        translationItemList.clear();
                        index = 0;
                        final long size = dataSnapshot.getChildrenCount();
                        //iterating through all the nodes
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //getting translations
                            final Translation translation = postSnapshot.getValue(Translation.class);
                            //TODO Through the translation, get the user and after create from both a TranslationItem
                            getUser(translation.getUser_id(), new FirebaseCallback<User>() {//Can't pass null for argument 'pathString' in child()
                                @Override
                                public void onCallback(User user) {
                                    TranslationItem translationItem = new TranslationItem(translation.getId(), translation.getText(),
                                            translation.getTime(), translation.getSource(), translation.getVerified(),
                                            translation.getLikes(), translation.getDislikes(), translation.getComments(),
                                            user.getId(), user.getNickname(), user.getReputation(), user.getImage(),
                                            getIntent().getStringExtra(WordMeaningsActivity.WORD_ID), meaning.getId(), translationLanguage,
                                            getIntent().getStringExtra(WordMeaningsActivity.WORD_TEXT));
                                    //adding translationItem to the list
                                    translationItemList.add(translationItem);
                                    if(++index>=size){
                                        firebaseCallback.onCallback(translationItemList);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void getTranslationLanguage(final FirebaseCallback<String> firebaseCallback){
        FirebaseDatabase.getInstance().getReference("dictionaries").child(getIntent().getStringExtra(WordMeaningsActivity.DICTIONARY_ID)).child("secondLanguage")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String translationLanguage = null;
                        if(dataSnapshot.exists()){
                            translationLanguage = dataSnapshot.getValue(String.class);
                        }
                        firebaseCallback.onCallback(translationLanguage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getUser(String userId, @NonNull final FirebaseCallback<User> firebaseCallback){
        FirebaseDatabase.getInstance().getReference("users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        firebaseCallback.onCallback(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void showUpdateDeleteDialog(final String artistId, String artistName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_to_list_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Spinner spinnerGenre = (Spinner) dialogView.findViewById(R.id.spinnerGenres);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteArtist);

        dialogBuilder.setTitle(artistName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


    }

//    public void pensarNumNome(DataSnapshot dataSnapshot, @NonNull final FirebaseCallback<List<TranslationItem>> firebaseCallback){
//        //iterating through all the nodes
//        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//            //getting artist
//            final Translation translation = postSnapshot.getValue(Translation.class);
//            getUser(translation.getUser_id(), new FirebaseCallback<User>() {
//                @Override
//                public void onCallback(User user) {
//                    TranslationItem translationItem = new TranslationItem(translation.getText(),
//                            translation.getTime(), translation.getSource(), translation.getVerified(),
//                            translation.getLikes(), translation.getDislikes(), translation.getComments(),
//                            user.getNickname(), user.getReputation(), user.getImage());
//                    //adding translationItem to the list
//                    translationItemList.add(translationItem);
//                }
//            });
//        }
//    }

    public void initializeToolbarAndBottomNavigation(){
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

    public void initialize(){
        //This is to avoid the keyboard showing automatically
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initializeToolbarAndBottomNavigation();
        editTextSearchWord = (EditText) findViewById(R.id.editTextSearchWord);

        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonAddNew = (Button) findViewById(R.id.buttonAddNew);

        buttonAddToList = (ImageButton) findViewById(R.id.buttonAddToList);
        buttonTranslations = (ImageButton) findViewById(R.id.buttonTranslations);
        buttonSentences = (ImageButton) findViewById(R.id.buttonSentences);
        buttonSynonyms = (ImageButton) findViewById(R.id.buttonSynonyms);
        buttonAntonyms = (ImageButton) findViewById(R.id.buttonAntonyms);
        buttonHashtags = (ImageButton) findViewById(R.id.buttonHashtags);
        buttonComments = (ImageButton) findViewById(R.id.buttonComments);
        buttonReport = (ImageButton) findViewById(R.id.buttonReport);

        textViewWord = (TextView) findViewById(R.id.textViewWord);
        textViewWordDescription = (TextView) findViewById(R.id.textViewWordDescription);

        listViewTranslations = (ListView) findViewById(R.id.listViewTranslations);

        translationItemList = new ArrayList<>();

        databaseMeanings = FirebaseDatabase.getInstance().getReference("meanings");
        databaseTranslations = FirebaseDatabase.getInstance().getReference("translations");
    }

    public interface FirebaseCallback<T> {
        void onCallback(T data);// T means generic data type
    }

}
