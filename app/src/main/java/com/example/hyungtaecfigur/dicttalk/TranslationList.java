package com.example.hyungtaecfigur.dicttalk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import de.hdodenhof.circleimageview.CircleImageView;


public class TranslationList extends ArrayAdapter<TranslationItem> {
    public static final String USER_ID = "com.example.hyungtaecfigur.dicttalk.userid";

    private Activity context;
    private List<TranslationItem> items;
    private TranslationItem translationItem;

    private Boolean liked;
    private Boolean disliked;

    private String like_id;
    private String dislike_id;

    private ImageButton buttonTranslationLike;
    private ImageButton buttonTranslationDislike;
    private ImageButton buttonAddToList;

    public TranslationList(Activity context, List<TranslationItem> items) {
        super(context, R.layout.translation_item_layout, items);
        this.context = context;
        this.items = items;
        FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View listViewItem = inflater.inflate(R.layout.translation_item_layout, null, true);

        initializeViewItems(listViewItem, position);

        setLikeAndDislikeButtons();

        return listViewItem;
    }

    public void initializeViewItems(View listViewItem, int position){
        final TextView textViewUsername = (TextView) listViewItem.findViewById(R.id.textViewUsername);
        final TextView textViewTranslationText = (TextView) listViewItem.findViewById(R.id.textViewTranslationText);
        final TextView textViewSource = (TextView) listViewItem.findViewById((R.id.textViewSource));
        final TextView textViewScore = (TextView) listViewItem.findViewById((R.id.textViewScore));
        final TextView textViewCommentsCount = (TextView) listViewItem.findViewById((R.id.textViewCommentsCount));
        final CircleImageView imageViewProfilePicture = (CircleImageView) listViewItem.findViewById(R.id.imageProfile);
        final TextView textViewTime = (TextView) listViewItem.findViewById((R.id.textViewTime));
        final TextView textViewReputation = (TextView) listViewItem.findViewById((R.id.textViewReputation));
        final TextView textViewSourceLabel = (TextView) listViewItem.findViewById((R.id.textViewSourceLabel));

        buttonTranslationLike = (ImageButton) listViewItem.findViewById(R.id.buttonTranslationLike);
        buttonTranslationDislike = (ImageButton) listViewItem.findViewById(R.id.buttonTranslationDislike);
        buttonAddToList = (ImageButton) listViewItem.findViewById(R.id.buttonAddToList);
        final ImageButton buttonComment = (ImageButton) listViewItem.findViewById(R.id.buttonComment);
        final ImageButton textViewTranslationFlag = (ImageButton) listViewItem.findViewById(R.id.textViewTranslationFlag);

        translationItem = items.get(position);

        textViewUsername.setText(translationItem.getNickname());
        Glide.with(getContext()).load(translationItem.getImage()).into(imageViewProfilePicture);

        textViewTranslationText.setText(translationItem.getTranslationText());
        textViewSource.setText(translationItem.getSource());
        textViewScore.setText(String.valueOf(translationItem.getLikes() - translationItem.getDislikes()));
        textViewCommentsCount.setText(String.valueOf(translationItem.getComments()));
        textViewReputation.setText(String.valueOf(translationItem.getReputation()));
        textViewTime.setText(new PrettyTime(Locale.getDefault())
                .format(new Date(translationItem.getTime())));

        if(textViewSource.getText().equals("")) textViewSourceLabel.setVisibility(View.INVISIBLE);

        textViewUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(USER_ID, translationItem.getUserId());
                context.finish();
                context.startActivity(intent);
            }
        });

        buttonAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO PENSAR EM COMO ADICIONAR LISTAS -> TEM QUE JA HAVER UMA LISTA PARA PODER ADICIONAR
                //TODO Tem que usar um dialog aqui

                //TODO PEGAR O TEXTO DA PALAVRA E TRADUÇÃO E BOTAR COMO EDITÁVEL
                //TODO SERIA BOM TER UM CONTADOR QUE REGISTRA EM QUANTAS LISTAS DE USUARIOS ESSA TRADUÇÃO ESTÁ, ENTAO FUTURAMENTE PODERIA TER UM RANKING



                //TODO Pegar nomes das listas e colocar em uma ArrayList OK
                //TODO Provavelmente vou precisar de uma ArrayList auxiliar para guardar os ID's das listas para quando precisar acessar referente ao nome OK
                //TODO Depois colocar a lista no spinner OK
                //TODO Depois programar o botao de adicionar lista -> falta adicionar um My-word
                final List<String> listNames = new ArrayList<>();
                final List<String> listIds = new ArrayList<>();
                FirebaseDatabase.getInstance().getReference("lists")
                        .child(translationItem.getUserId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        listNames.add(postSnapshot.child("name").getValue(String.class));
                                        listIds.add(postSnapshot.child("id").getValue(String.class));
                                    }
                                    //insert data into the spinner
                                    Spinner spinner = new Spinner(context);
                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                                            (context, android.R.layout.simple_spinner_item,
                                                    listNames);
                                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                            .simple_spinner_dropdown_item);
                                    spinner.setAdapter(spinnerArrayAdapter);
                                }
                                showAddToListDialog(listNames, listIds);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                //TODO Depois que tudo funcionar bem, colocar codigo semelhante no AddToListActivity
                //TODO Depois fazer as listas aparecerem no MyListsMainActivity
                //TODO Depois fazer as palavras aparecerem em outro activity depois de clicar na lista do MyListsMainActivity

                FirebaseDatabase.getInstance().getReference("lists").child(translationItem.getUserId());//todo .child(LIST_ID)
            }
        });
    }

    private void showAddToListDialog(final List<String> listNames, final List<String> listIds) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.add_to_list_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Spinner spinnerLists = (Spinner) dialogView.findViewById(R.id.spinnerLists);
        final Button buttonAdd = (Button) dialogView.findViewById(R.id.buttonAdd);
        final CheckBox checkBoxPrivate = (CheckBox) dialogView.findViewById(R.id.checkBoxPrivate);

        dialogBuilder.setTitle("Add into a list");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newListName = editTextName.getText().toString().trim();
                //TODO TESTAR SE TEM VALORES NO SPINNER
                //String selectedList = spinnerLists.getSelectedItem().toString();
                String listType;

                if(checkBoxPrivate.isChecked()){
                    listType = "private";
                } else {
                    listType = "public";
                }

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("lists")
                        .child(translationItem.getUserId());

                //if there is a name written in the editTextName, then prior that name to be the list name to put the word into
                if (!TextUtils.isEmpty(newListName)) {
                    String list_id = reference.push().getKey();
                    Lista list = new Lista(list_id, translationItem.getUserId(), newListName, listType) ;
                    reference.child(list_id).setValue(list);
                    b.dismiss();
                    //TODO CONTINUE
                    //TODO TEM QUE REGISTRAR UM MY_WORD AQUI
                    Toast.makeText(context, "Word registered to the list \"" + newListName + "\" successfully.", Toast.LENGTH_LONG).show();
                } else if (listNames.size() > 0) { //else if the user has lists already created, it takes the selected item as the list to put the word into
                    //TODO Pegar a lista e salvar a palavra MY_WORD AQUI
//                    String list_id = reference.push().getKey();
//                    Lista list = new Lista(list_id, translationItem.getUserId(), newListName, listType) ;
//                    reference.child(list_id).setValue(list);
//                    b.dismiss();
                }
            }
        });
        //                    b.dismiss();


    }

    public void setLikeAndDislikeButtons(){
        FirebaseDatabase.getInstance().getReference("likes/translations").child(translationItem.getTranslationId())
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            buttonTranslationLike.setImageResource(R.drawable.ic_up_vote);
                            liked = true;
                        } else {
                            buttonTranslationLike.setImageResource(R.drawable.ic_arrow_drop_up);
                            liked = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("dislikes/translations").child(translationItem.getTranslationId())
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            buttonTranslationDislike.setImageResource(R.drawable.ic_down_vote);
                            disliked = true;
                        } else {
                            buttonTranslationDislike.setImageResource(R.drawable.ic_arrow_drop_down);
                            disliked = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        buttonTranslationLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //testing if it is the same user
                if(!FirebaseAuth.getInstance().getCurrentUser().getUid()
                        .equals(translationItem.getUserId())) {
                    //testing if it is already liked
                    if (!liked) {
                        like();
                    } else {
                        cancelLike();
                    }
                } else {
                    Toast.makeText(context, "You cannot like your own translations", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonTranslationDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //testing if it is the same user
                if(!FirebaseAuth.getInstance().getCurrentUser().getUid()
                        .equals(translationItem.getUserId())) {
                    //testing if it is already liked
                    if (!disliked) {
                        dislike();
                    } else {
                        cancelDislike();
                    }
                } else {
                    Toast.makeText(context, "You cannot dislike your own translations", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void like(){
        //TODO if it is not liked
        if (disliked) {
            cancelDislike();
        }
        if(!liked) {
            //registering the like in the "likes/translations"
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("likes/translations").child(translationItem.getTranslationId())
                    .child(FirebaseAuth.getInstance().getUid());
            like_id = reference.push().getKey();
            Like like = new Like(like_id, "", translationItem.getUserId(),
                    translationItem.getTranslationId(), "");
            reference.child(like_id).setValue(like);
            //Increasing like counters for translation and user, and increasing reputation
            increaseTranslationLikes(new FirebaseCallback<Integer>() {
                @Override
                public void onCallback(Integer data) {
                    increaseUserLikes(new FirebaseCallback<Integer>() {
                        @Override
                        public void onCallback(Integer data) {
                            increaseUserReputation(new FirebaseCallback<Integer>() {
                                @Override
                                public void onCallback(Integer data) {
                                    //TODO CREATE NOTIFICATION ABOUT THE LIKE
                                    buttonTranslationLike.setImageResource(R.drawable.ic_up_vote);
                                    liked = true;
                                    //it refreshes the listView
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Translation liked successfully", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            });
        } else {
            Toast.makeText(context, "It is already liked", Toast.LENGTH_LONG).show();
        }
    }

    public void dislike(){
        //TODO if it is not disliked
        //TODO REQUEST A REASON FOR THE DISLIKE
            if (liked) {
                cancelLike();
            }
        if(!disliked) {
            //registering the dislike in the "dislikes"
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("dislikes/translations").child(translationItem.getTranslationId())
                    .child(FirebaseAuth.getInstance().getUid());
            dislike_id = reference.push().getKey();
            Dislike dislike = new Dislike(dislike_id, "", translationItem.getUserId(),
                    translationItem.getTranslationId(), "", "");
            reference.child(dislike_id).setValue(dislike);
            //Increasing dislike counters for translation and user, and increasing reputation
            increaseTranslationDislikes(new FirebaseCallback<Integer>() {
                @Override
                public void onCallback(Integer data) {
                    increaseUserDislikes(new FirebaseCallback<Integer>() {
                        @Override
                        public void onCallback(Integer data) {
                            increaseUserReputation(new FirebaseCallback<Integer>() {
                                @Override
                                public void onCallback(Integer data) {
                                    //TODO CREATE NOTIFICATION ABOUT THE DISLIKE
                                    buttonTranslationDislike.setImageResource(R.drawable.ic_down_vote);
                                    disliked = true;
                                    //it refreshes the listView
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Translation disliked successfully", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            });
        } else {
            Toast.makeText(context, "It is already disliked", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelLike(){
        //removing the user in the "likes/translations"
        FirebaseDatabase.getInstance().getReference("likes/translations").child(translationItem.getTranslationId())
                .child(FirebaseAuth.getInstance().getUid()).removeValue();
        //Decreasing like counters for translation and user, and increasing reputation
        decreaseTranslationLikes(new FirebaseCallback<Integer>() {
            @Override
            public void onCallback(Integer data) {
                decreaseUserLikes(new FirebaseCallback<Integer>() {
                    @Override
                    public void onCallback(Integer data) {
                        decreaseUserReputation(new FirebaseCallback<Integer>() {
                            @Override
                            public void onCallback(Integer data) {
                                buttonTranslationLike.setImageResource(R.drawable.ic_arrow_drop_up);
                                liked = false;
                                //it refreshes the listView
                                notifyDataSetChanged();
                                Toast.makeText(context, "Translation like cancelled successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
    }

    public void cancelDislike(){
        //removing the dislike in the "likes/translations"
        FirebaseDatabase.getInstance().getReference("likes/translations").child(translationItem.getTranslationId())
                .child(FirebaseAuth.getInstance().getUid()).removeValue();
        //Decreasing dislike counters for translation and user, and increasing reputation
        decreaseTranslationDislikes(new FirebaseCallback<Integer>() {
            @Override
            public void onCallback(Integer data) {
                decreaseUserDislikes(new FirebaseCallback<Integer>() {
                    @Override
                    public void onCallback(Integer data) {
                        decreaseUserReputation(new FirebaseCallback<Integer>() {
                            @Override
                            public void onCallback(Integer data) {
                                buttonTranslationDislike.setImageResource(R.drawable.ic_arrow_drop_down);
                                disliked = false;
                                //it refreshes the listView
                                notifyDataSetChanged();
                                Toast.makeText(context, "Translation like cancelled successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
    }

    public void increaseTranslationLikes(final FirebaseCallback<Integer> firebaseCallback){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("translations")
                .child(translationItem.getWordId())
                .child(translationItem.getLanguageId())
                .child(translationItem.getMeaningId())
                .child(translationItem.getTranslationId()).child("likes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int likes = dataSnapshot.getValue(Integer.class);
                likes++;
                ref.setValue(likes);
                firebaseCallback.onCallback(likes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void increaseTranslationDislikes(final FirebaseCallback<Integer> firebaseCallback){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("translations")
                .child(translationItem.getWordId())
                .child(translationItem.getLanguageId())
                .child(translationItem.getMeaningId())
                .child(translationItem.getTranslationId()).child("dislikes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int dislikes = dataSnapshot.getValue(Integer.class);
                dislikes++;
                ref.setValue(dislikes);
                firebaseCallback.onCallback(dislikes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void decreaseTranslationLikes(final FirebaseCallback<Integer> firebaseCallback){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("translations")
                .child(translationItem.getWordId())
                .child(translationItem.getLanguageId())
                .child(translationItem.getMeaningId())
                .child(translationItem.getTranslationId()).child("likes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int likes = dataSnapshot.getValue(Integer.class);
                likes--;
                ref.setValue(likes);
                firebaseCallback.onCallback(likes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void decreaseTranslationDislikes(final FirebaseCallback<Integer> firebaseCallback){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("translations")
                .child(translationItem.getWordId())
                .child(translationItem.getLanguageId())
                .child(translationItem.getMeaningId())
                .child(translationItem.getTranslationId()).child("dislikes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int dislikes = dataSnapshot.getValue(Integer.class);
                dislikes--;
                ref.setValue(dislikes);
                firebaseCallback.onCallback(dislikes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void increaseUserLikes(final FirebaseCallback<Integer> firebaseCallback){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                .child(translationItem.getUserId()).child("likes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int likes = dataSnapshot.getValue(Integer.class);
                likes++;
                ref.setValue(likes);
                firebaseCallback.onCallback(likes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void increaseUserDislikes(final FirebaseCallback<Integer> firebaseCallback){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                .child(translationItem.getUserId()).child("dislikes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int dislikes = dataSnapshot.getValue(Integer.class);
                dislikes++;
                ref.setValue(dislikes);
                firebaseCallback.onCallback(dislikes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void decreaseUserLikes(final FirebaseCallback<Integer> firebaseCallback){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                .child(translationItem.getUserId()).child("likes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int likes = dataSnapshot.getValue(Integer.class);
                likes--;
                ref.setValue(likes);
                firebaseCallback.onCallback(likes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void decreaseUserDislikes(final FirebaseCallback<Integer> firebaseCallback){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                .child(translationItem.getUserId()).child("dislikes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int dislikes = dataSnapshot.getValue(Integer.class);
                dislikes--;
                ref.setValue(dislikes);
                firebaseCallback.onCallback(dislikes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void increaseUserReputation(final FirebaseCallback<Integer> firebaseCallback){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                .child(translationItem.getUserId()).child("reputation");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int reputation = dataSnapshot.getValue(Integer.class);
                reputation++;
                ref.setValue(reputation);
                firebaseCallback.onCallback(reputation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void decreaseUserReputation(final FirebaseCallback<Integer> firebaseCallback){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                .child(translationItem.getUserId()).child("reputation");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int reputation = dataSnapshot.getValue(Integer.class);
                reputation--;
                ref.setValue(reputation);
                firebaseCallback.onCallback(reputation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface FirebaseCallback<T> {
        void onCallback(T data);// T means generic data type
    }
}
