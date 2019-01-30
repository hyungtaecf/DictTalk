package com.example.hyungtaecfigur.dicttalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;

//TODO Quando eu fizer o quiz, usar referência do dicionário aparecendo como um "dialog", assim como no "updateDialog" do projeto Firebase4

public class MyListsMainActivity extends AppCompatActivity {

    private ListView search_lists;
    private ArrayAdapter<String> adapter;

    private Button buttonAddList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lists_main);

        search_lists = (ListView) findViewById(R.id.search_lists);

        buttonAddList = (Button) findViewById(R.id.buttonAddList);

        ArrayList<String> arrayLists = new ArrayList<>();
        arrayLists.addAll(Arrays.asList(getResources().getStringArray(R.array.my_lists)));

        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arrayLists
        );

        search_lists.setAdapter(adapter);

        buttonAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),SearchListsActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_lists);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
