package com.example.hyungtaecfigur.dicttalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DictionarySearchActivity extends AppCompatActivity {
    public static final String WORD_ID = "com.example.hyungtaecfigur.dicttalk.wordid";
    public static final String DICTIONARY_ID = "com.example.hyungtaecfigur.dicttalk.dictionaryid";
    public static final String WORD_TEXT = "com.example.hyungtaecfigur.dicttalk.wordtext";

    //TODO QUANDO PESQUISAR, SE NÃO TIVER UMA PALAVRA EXATAMENTE COMO PESQUISOU, DIZER QUE AINDA NÃO EXISTE E FAZER APARECER EMBAIXO PALAVRAS QUE BATEM COM A PESQUISA

    //a list to store que query of users
//    private List<Word> wordList;

    private DatabaseReference databaseWords;

    private EditText editTextSearchWord;
    private Button buttonSearchWord;
    private Button buttonAddWord;
    private Button buttonRequests;
    private ListView listViewSearchWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_search);

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

        //getting the reference of users node
        databaseWords = FirebaseDatabase.getInstance().getReference("words");

        //getting views
        editTextSearchWord = (EditText) findViewById(R.id.editTextSearchWord);
        buttonSearchWord = (Button) findViewById(R.id.buttonSearchWord);
        buttonAddWord = (Button) findViewById(R.id.buttonAddWord);
        buttonRequests = (Button) findViewById(R.id.buttonRequests);
        listViewSearchWord = (ListView) findViewById(R.id.listViewSearchWord);

        buttonSearchWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO LOOK HOW NOT CONSIDER CASE OF THE TEXT IN THE DATABASE ON SEARCHING
                Query wordQuery = databaseWords.orderByChild("text").equalTo(editTextSearchWord.getText().toString().toLowerCase());

                //attaching value event listener
                wordQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Word word = null;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            word = postSnapshot.getValue(Word.class);
                        }
                        if (word != null) {
                            Intent intent = new Intent(getApplicationContext(), WordMeaningsActivity.class);
                            intent.putExtra(WORD_ID,word.getId());
                            //TODO intent.putExtra(PEGAR EXTRA DO ACTIVITY ANTERIOR);
                            intent.putExtra(WORD_TEXT,word.getText());
                            finish();
                            startActivity(intent);
                        }else{
                            Log.i("MyProfileActivity", "onDataChange: Word not found");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getMessage());
                    }
                });
            }
        });

        buttonAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO depois que tiver botoes para voltar botar esse finish
                //finish();
                Intent intent = new Intent(getApplicationContext(), WordRegistrationActivity.class);
                //todo intent.putExtra(WORD_ID,word.getId());
                //TODO intent.putExtra(PEGAR EXTRA DO ACTIVITY ANTERIOR);
                //TODO intent.putExtra(WORD_TEXT,word.getText());
                startActivity(intent);
            }
        });

        buttonRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO depois que tiver botoes para voltar botar esse finish
                //finish();
                Intent intent = new Intent(getApplicationContext(), RequestMainActivity.class);
                //TODO Colocar extras
                intent.putExtra("dictionary","eng_kor");
                startActivity(intent);
            }
        });
    }

}
