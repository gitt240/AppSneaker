package com.example.appsneaker.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appsneaker.R;
import com.example.appsneaker.fragment.KhacFragment;
import com.example.appsneaker.model.NguoiDung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CapNhatThongTinActivity extends AppCompatActivity {


    EditText edtName, edtPhone, edtEmail;
    Button btnUpdate;
    String id;
    NguoiDung nguoiDung;
    Toolbar toolbar;
    FirebaseDatabase database;
    FirebaseAuth userAuth;
    FirebaseStorage storage;
    FirebaseFirestore db;
    CircleImageView imgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_thong_tin);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgUser = findViewById(R.id.imgUser);
        toolbar = findViewById(R.id.toolBar);
        database = FirebaseDatabase.getInstance();
        userAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        edtEmail.setFocusable(false);

        id = userAuth.getCurrentUser().getUid();


        db.collection("user").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value!= null && value.exists()){
                    NguoiDung nguoiDung = value.toObject(NguoiDung.class);
                    edtName.setText(nguoiDung.getName());
                    edtEmail.setText(nguoiDung.getEmail());
                    edtPhone.setText(nguoiDung.getPhone());
                }

//                if (!isFinishing()) {
//                    Glide.with(CapNhatThongTinActivity.this).load(nguoiDung.getImage()).into(imgUser);
//                }
            }
        });

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();
                if (phone.length() < 10) {
                    edtPhone.setError("Số điện thoại không hợp lệ");
                    return;
                }
                updateData(name, phone);
            }
        });


    }

    public void updateData(String name, String phone) {


        HashMap<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("phone", phone);

        db.collection("user").document(id).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    setResult(CapNhatThongTinActivity.RESULT_OK);
                    finish();
                    Toast.makeText(CapNhatThongTinActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CapNhatThongTinActivity.this, "Cập nhật không thành công", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 33 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri profileimg = data.getData();
            imgUser.setImageURI(profileimg);

            StorageReference reference = storage.getReference().child("user_image").child(userAuth.getCurrentUser().getUid());
            reference.putFile(profileimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CapNhatThongTinActivity.this, "Upload success", Toast.LENGTH_LONG).show();


                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("image", uri.toString());
                            db.collection("user").document(userAuth.getCurrentUser().getUid())
                                    .update(userMap);

                        }
                    });
                }
            });
        }
    }
}