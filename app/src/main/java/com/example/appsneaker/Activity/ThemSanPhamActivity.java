package com.example.appsneaker.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appsneaker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ThemSanPhamActivity extends AppCompatActivity {

    ImageView imgThem;
    EditText edtTen, edtGia, edtLoai, edtDanhGia, edtMota;
    Button btnThem;

    FirebaseFirestore db;
    FirebaseStorage storage;
    Uri imgSanPhamUri;
    ProgressDialog progressDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_san_pham);
        imgThem = findViewById(R.id.imgThem);
        edtTen = findViewById(R.id.edtTen);
        edtGia = findViewById(R.id.edtGia);
        edtLoai = findViewById(R.id.edtLoai);
        edtDanhGia = findViewById(R.id.edtDanhGia);
        edtMota = findViewById(R.id.edtMota);
        btnThem = findViewById(R.id.btnThem);
        toolbar = findViewById(R.id.toolBar);
        progressDialog = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        imgThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 5);
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }

    public void addData() {
        String name = edtTen.getText().toString();
        String priceText = edtGia.getText().toString();
        String type = edtLoai.getText().toString();
        String rate = edtDanhGia.getText().toString();
        String des = edtMota.getText().toString();

        if (name.isEmpty()){
            edtTen.setError("Vui lòng nhập tên");
            return;
        }
        if (priceText.isEmpty()){
            edtGia.setError("Vui lòng nhập giá sản phẩm");
            return;
        }
        if (type.isEmpty()){
            edtLoai.setError("Vui lòng nhập thưởng hiệu");
            return;
        }
        if (rate.isEmpty()){
            edtDanhGia.setError("Vui lòng nhập đánh giá");
            return;
        }
        if (des.isEmpty()){
            edtMota.setError("Vui lòng nhập mô tả");
            return;
        }

        int price = Integer.parseInt(priceText);


        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("name", name);
        productMap.put("price", price);
        productMap.put("type", type);
        productMap.put("rate", rate);
        productMap.put("des", des);


        if (imgSanPhamUri != null){
            progressDialog.show();
            StorageReference storageReference = storage.getReference().child("sanpham/"+ System.currentTimeMillis() + ".jpg");
            storageReference.putFile(imgSanPhamUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imgSaveUri = uri.toString();
                            productMap.put("imgurl", imgSaveUri);

                            db.collection("SanPham").add(productMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ThemSanPhamActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ThemSanPhamActivity.this, "Upload hình ảnh thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(ThemSanPhamActivity.this, "Vui lòng chọn hình ảnh", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgSanPhamUri = data.getData();
            imgThem.setImageURI(imgSanPhamUri);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(ThemSanPhamActivity.this, MainAdminActivity.class));
        return true;
    }
}