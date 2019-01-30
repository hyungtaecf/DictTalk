package com.example.hyungtaecfigur.dicttalk;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;


public class MeaningList extends ArrayAdapter<MeaningItem> {
    private Activity context;
    private List<MeaningItem> items;

    public MeaningList(Activity context, List<MeaningItem> items) {
        super(context, R.layout.meaning_list_layout, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.meaning_list_layout, null, true);

        TextView textViewLanguage = (TextView) listViewItem.findViewById(R.id.textViewLanguage);
        TextView textViewWord = (TextView) listViewItem.findViewById(R.id.textViewWord);
        TextView textViewMeaning = (TextView) listViewItem.findViewById(R.id.textViewMeaning);

        MeaningItem item = items.get(position);
        textViewLanguage.setText(item.getLanguage());
        textViewWord.setText(item.getWord());
        textViewMeaning.setText(item.getMeaning());

        return listViewItem;
    }
}
