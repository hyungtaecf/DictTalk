package com.example.hyungtaecfigur.dicttalk;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserList extends ArrayAdapter<User> {
    private Activity context;
    List<User> users;

    public UserList(Activity context, List<User> users) {
        super(context, R.layout.user_list_layout, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.user_list_layout, null, true);

        TextView textViewUsername = (TextView) listViewItem.findViewById(R.id.textViewUsername);
        TextView textViewBio = (TextView) listViewItem.findViewById(R.id.textViewBio);
        CircleImageView imageViewProfilePicture = (CircleImageView) listViewItem.findViewById(R.id.imageViewProfilePicture);

        User user = users.get(position);
        textViewUsername.setText(user.getNickname());
        textViewBio.setText(user.getBio());
        Glide.with(getContext()).load(user.getImage()).into(imageViewProfilePicture);
        return listViewItem;
    }
}
