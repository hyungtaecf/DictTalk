package com.example.hyungtaecfigur.dicttalk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//TODO Implement extras from the DictionarySearchActivity
//TODO Fix: esta se confundindo em alguma parte referente a registrar o idioma da palavra
public class WordRegistrationActivity extends AppCompatActivity {
    private EditText editTextWordBody;
    private EditText editTextWordTranslation;
    private EditText editTextSource;
    private Spinner spinnerWordLanguage;
    private Spinner spinnerTranslationLanguage;
    private Button buttonAddWord;
    private CheckBox checkBoxConsiderCase;
    private CheckBox checkBoxInverseTranslation;

    //our database reference object
    private DatabaseReference words;
    private DatabaseReference meanings;
    private DatabaseReference translations;
    private DatabaseReference languages;
    private DatabaseReference languages_dictionaries;
    private DatabaseReference currentLanguage; //To compare with the selected language in the spinner

    private String wordLanguage; //word's language_id
    private String translationLanguage; //translation's language_id
    private String word_text;
    private String translation_text;
    private String meaning_text;
    private String source;
    private String currentFirebaseUserUId;
    private String wordLanguageNotTranslated; //auxiliary variables to store the string that appears in the spinner accordingly with the device language
    private String translationLanguageNotTranslated;
    private String word_id;
    private String meaning_id;

    private Boolean auxInverseTranslation; //auxiliary variable to dont loop the Inverse Translation Function (first loop = true, second loop = false)

    private List<Word> sameWords;
    private List<Translation> sameTranslations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_registration);

        initialize();

    }

    private void addWord() {
        //TODO se a palavra já existir = like automático

        //TODO FIX: Nao registra a palavra com escrita igual em outra lingua

        //auxiliary variables
        wordLanguageNotTranslated = spinnerWordLanguage.getSelectedItem().toString();
        translationLanguageNotTranslated = spinnerTranslationLanguage.getSelectedItem().toString();
        auxInverseTranslation = true;

        //getting the values to save
        if (checkBoxConsiderCase.isChecked()) {
            word_text = editTextWordBody.getText().toString();
            translation_text = editTextWordTranslation.getText().toString();
        } else {
            word_text = editTextWordBody.getText().toString().toLowerCase();
            translation_text = editTextWordTranslation.getText().toString().toLowerCase();
        }
        source = editTextSource.getText().toString();
        currentFirebaseUserUId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //checking if the value is provided
        if (!TextUtils.isEmpty(word_text) || TextUtils.isEmpty(translation_text)) {
            wordLanguage = wordLanguageNotTranslated;
            translationLanguage = translationLanguageNotTranslated;
            //testing if the languages were selected
            if ((!wordLanguage.equals(getResources().getString(R.string.spinner_language_notSelected)))
                    && (!translationLanguage.equals(getResources().getString(R.string.spinner_language_notSelected)))) {
                //testing if the language of the word and translation are the same
                if (!wordLanguage.equals(translationLanguage)) {
                    registerWordIfNew();
                } else {
                    //if the value is not given displaying a toast
                    Toast.makeText(getApplicationContext(), "The language of the word and it's translation must be different", Toast.LENGTH_LONG).show();
                }
            } else {
                //if the value is not given displaying a toast
                Toast.makeText(getApplicationContext(), "Please select a language", Toast.LENGTH_LONG).show();
            }

        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a word and a translation", Toast.LENGTH_LONG).show();
        }
    }

    public void registerWordIfNew(){
        //Getting translations of spinners
        getWordLanguage(new FirebaseCallback<String>() {
            @Override
            public void onCallback(String data) {
                getTranslationLanguage(new FirebaseCallback<String>() {
                    @Override
                    public void onCallback(String data) {
                        //testing if the word is already registered
                        getWord(new FirebaseCallback<List<Word>>() {
                            @Override
                            public void onCallback(final List<Word> wordList) {
                                //wordLanguage.equals(wordList.get(0).getLanguage_id()
                                if (wordList.isEmpty()) {
                                    //getting a unique id using push().getKey() method
                                    word_id = words.push().getKey();
                                    meaning_id = meanings.push().getKey();
                                    proceedRegistration();
                                } else {
                                    //auxiliary variable that indicates if there is a registered word from the same language
                                    Boolean sameLanguage = false;
                                    int i;
                                    for(i =0; i<wordList.size(); i++){
                                        if(wordLanguage.equals(wordList.get(i).getLanguage_id())) {
                                            sameLanguage = true;
                                            //while breaking, it keeps registered the current index
                                            break;
                                        }
                                    }
                                    if(sameLanguage) {
                                        //testing if the same translation is already registered for the word
                                        final int finalI = i;
                                        getTranslation(wordList.get(i).getId(), translationLanguage, new FirebaseCallback<List<Translation>>() {
                                            @Override
                                            public void onCallback(List<Translation> translationList) {
                                                if (translationList.isEmpty()) {
                                                    word_id = wordList.get(finalI).getId();
                                                    proceedRegistration();
                                                } else {
                                                    //if the word is already registered
                                                    Toast.makeText(getApplicationContext(), "The translation" + translation_text
                                                            + "is already registered for the word " + word_text, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }else{
                                        //even if there is a word with the same text, if there is not registered in the same language, then treat as a new word
                                        //getting a unique id using push().getKey() method
                                        word_id = words.push().getKey();
                                        meaning_id = meanings.push().getKey();
                                        proceedRegistration();
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public void proceedRegistration(){
        //getting a unique id using push().getKey() method
        final String translation_id = translations.push().getKey();

        //getting dictionary_id based in the selected languages
        getDictionaryId(new FirebaseCallback<String>() {
            @Override
            public void onCallback(String dictionary_id) {
                meaning_text = dictionary_id + " " + getResources().getString(R.string.default_meaning);

                //getting language_id's
                String wordLanguageId = languages.child(wordLanguage).getKey();
                String translationLanguageId = languages.child(translationLanguage).getKey();

                //getting time
                long time = System.currentTimeMillis();

                //creating the objects
                Word word = new Word(word_id, currentFirebaseUserUId, wordLanguageId, word_text, time);
                Meaning meaning = new Meaning(meaning_id, word_id, dictionary_id, currentFirebaseUserUId, meaning_text, time);
                Translation translation = new Translation(translation_id, meaning_id, translationLanguageId, currentFirebaseUserUId, translation_text, time, source);

                //Saving the Word
                words.child(word_id).setValue(word);

                //Saving the Meaning
                meanings.child(word_id).child(meaning_id).setValue(meaning);

                //Saving the Translation
                translations.child(word_id).child(translationLanguageId).child(meaning_id).child(translation_id).setValue(translation);

                if (checkBoxInverseTranslation.isChecked() && auxInverseTranslation) {
                    addInverseTranslation();
                }

                //displaying a success toast
                Toast.makeText(getApplicationContext(), "Your contribution was added to the dictionary successfully", Toast.LENGTH_LONG).show();

                //resetting views
                editTextWordBody.setText("");
                editTextWordTranslation.setText("");
                editTextSource.setText("");
                checkBoxConsiderCase.setChecked(false);
            }
        });

    }

    public void addInverseTranslation() {
        //inverting the languages;
        String aux = wordLanguage;
        wordLanguage = translationLanguage;
        translationLanguage = aux;
        aux = word_text;
        word_text = translation_text;
        translation_text = aux;
        aux = wordLanguageNotTranslated;
        wordLanguageNotTranslated = translationLanguageNotTranslated;
        translationLanguageNotTranslated = aux;

        //breaking the loop of inverse translations
        auxInverseTranslation = false;

        registerWordIfNew();
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

    public void initialize() {
        initializeToolbarAndBottomNavigation();
        //getting the database references
        words = FirebaseDatabase.getInstance().getReference("words");
        translations = FirebaseDatabase.getInstance().getReference("translations");
        meanings = FirebaseDatabase.getInstance().getReference("meanings");
        languages = FirebaseDatabase.getInstance().getReference("languages");
        languages_dictionaries = FirebaseDatabase.getInstance().getReference("languages_dictionaries");
        currentLanguage = FirebaseDatabase.getInstance().getReference("string_translations")
                .child("languages").child(Locale.getDefault().getDisplayLanguage());

        //getting views
        editTextWordBody = (EditText) findViewById(R.id.editTextWordBody);
        editTextWordTranslation = (EditText) findViewById(R.id.editTextWordTranslation);
        editTextSource = (EditText) findViewById(R.id.editTextSource);
        spinnerWordLanguage = (Spinner) findViewById(R.id.spinnerWordLanguage);
        spinnerTranslationLanguage = (Spinner) findViewById(R.id.spinnerTranslationLanguage);
        buttonAddWord = (Button) findViewById(R.id.buttonAddWord);
        checkBoxConsiderCase = (CheckBox) findViewById(R.id.checkBoxConsiderCase);
        checkBoxInverseTranslation = (CheckBox) findViewById(R.id.checkBoxInverseTranslation);

        sameWords = new ArrayList<>();
        sameTranslations = new ArrayList<>();

        auxInverseTranslation = false;

        //adding an onclicklistener to button
        buttonAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addWord()
                //the method is defined below
                //this method is actually performing the write operation
                addWord();
            }
        });
    }

    public void getWord(final FirebaseCallback<List<Word>> firebaseCallback) {
        words.orderByChild("text").equalTo(word_text).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //clearing preview list
                sameWords.clear();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        sameWords.add(postSnapshot.getValue(Word.class));
                    }
                }
                firebaseCallback.onCallback(sameWords);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getTranslation(final String wordId, final String language_id, final FirebaseCallback<List<Translation>> firebaseCallback) {
        //TODO Verificar a origem desse language id, nao pode ser o da palavra, mas sim o da traducao
        //clearing preview list
        sameTranslations.clear();
        translations.child(wordId).child(language_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot meaningId : dataSnapshot.getChildren()) {
                                meaning_id = meaningId.getKey();
                                translations.child(wordId).child(language_id).child(meaning_id)
                                        .orderByChild("text").equalTo(translation_text)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                        sameTranslations.add(postSnapshot.getValue(Translation.class));
                                                    }
                                                }
                                                firebaseCallback.onCallback(sameTranslations);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }
                        }else{
                            //Adding a default meaning for this language
                            meaning_id = meanings.push().getKey();
                            //There is no meanings registered for that word, returning a empty list of translations
                            firebaseCallback.onCallback(sameTranslations);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getWordLanguage(@NonNull final FirebaseCallback<String> firebaseCallback) {
        currentLanguage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot language : dataSnapshot.getChildren()) {
                        if (language.getKey().equals(wordLanguageNotTranslated)) {
                            wordLanguage = language.getValue(String.class);
                            break;
                        }
                    }
                    if (wordLanguage == null) {
                        Log.i("WordRegistration", "onDataChange (wordLanguage): No Language like "
                                + wordLanguageNotTranslated);
                    }
                    firebaseCallback.onCallback(wordLanguage);
                }
                Log.i("WordRegistration", "onDataChange (wordLanguage): " + wordLanguageNotTranslated);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    public void getTranslationLanguage(@NonNull final FirebaseCallback<String> firebaseCallback) {
        currentLanguage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot language : dataSnapshot.getChildren()) {
                        if (language.getKey().equals(translationLanguageNotTranslated)) {
                            translationLanguage = language.getValue(String.class);
                            break;
                        }
                    }
                    if (translationLanguage == null) {
                        Log.i("WordRegistration", "onDataChange (translationLanguage): No Language like "
                                + translationLanguageNotTranslated);
                    }
                    firebaseCallback.onCallback(translationLanguage);
                }
                Log.i("WordRegistration", "onDataChange (translationLanguage): " + translationLanguageNotTranslated);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    public void getDictionaryId(@NonNull final FirebaseCallback<String> firebaseCallback) {
        languages_dictionaries.child(wordLanguage).child(translationLanguage)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String dictionary_id = null;
                        if (dataSnapshot.exists()) {
                            dictionary_id = dataSnapshot.getValue(String.class);
                        } else {
                            System.out.println("Data snapshot doesn't exist");
                        }
                        Log.i("WordRegistration", "onDataChange (dictionary_id): " + dictionary_id);
                        firebaseCallback.onCallback(dictionary_id);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getMessage());
                    }

                });
    }

    public interface FirebaseCallback<T> {
        void onCallback(T data);// T means generic data type
    }
}
