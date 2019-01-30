package com.example.hyungtaecfigur.dicttalk;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.common.util.Strings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DictionaryList extends ArrayAdapter<Dictionary> {
    private Activity context;
    List<Dictionary> dictionaries;

    public DictionaryList(Activity context, List<Dictionary> dictionaries) {
        super(context, R.layout.dictionary_list_layout, dictionaries);
        this.context = context;
        this.dictionaries = dictionaries;

        //TODO Code in a way it translates back from the dictionary_id (english) to the device language
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.dictionary_list_layout, null, true);

        TextView textViewDictionary = (TextView) listViewItem.findViewById(R.id.textViewDictionary);

        Dictionary dictionary = dictionaries.get(position);
        textViewDictionary.setText(dictionary.getFirstLanguage() + " - " + dictionary.getSecondLanguage());
        return listViewItem;
    }

}
