package com.example.hyungtaecfigur.dicttalk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hyungtaecfigur.dicttalk.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpActivity extends AppCompatActivity {
    private EditText email;
    private EditText name;
    private EditText password;
    private Button signUp;
    private String splash_background;
    private ImageView profile;
    private Uri imageUri;
    public static final int PICK_FROM_ALBUM = 10;

    StorageReference ref;

    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        profile = findViewById(R.id.signUpActivity_imagevie_profile);
        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        splash_background = mFirebaseRemoteConfig.getString(getString(R.string.re_color));

        email = findViewById(R.id.signUpActivity_editText_email);
        name = findViewById(R.id.signUpActivity_editText_name);
        password = findViewById(R.id.signUpActivity_editText_password);
        signUp = findViewById(R.id.signUpActivity_button_signUp);

        ref = FirebaseStorage.getInstance().getReference();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString() == null || email.getText().toString() == null || password.getText().toString() == null || imageUri == null) {
                    return;
                }

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    final String uid = task.getResult().getUser().getUid();

                                    FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            //String imageUrl = task.getResult().getDownloadUrl().toString(); //저장소에 사진uri넣기가 완료되면 넣은 사진을 다운 받을 수 있는 uri반환
                                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    imageUrl = uri.toString();
                                                }
                                            });
                                            UserModel userModel = new UserModel();
                                            userModel.userName = name.getText().toString();
                                            userModel.profileImageUrl = imageUrl;
                                            userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);

                                            finish();
                                        }
                                    });
                                }else{
                                    Toast.makeText(SignUpActivity.this, String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            profile.setImageURI(data.getData()); // 가운데 뷰를 바꿈
            imageUri = data.getData(); //이미지 경로 원본
        }
    }
}
