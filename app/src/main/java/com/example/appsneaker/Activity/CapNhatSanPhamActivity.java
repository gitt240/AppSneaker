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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appsneaker.R;
import com.example.appsneaker.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class CapNhatSanPhamActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseStorage storage;
     EditText edtTen, edtGia, edtLoai, edtDanhGia, edtMota;
     Button btnUpdate;
     ImageView imgSanPham;
     SanPham sanPham;
     String id;
     Uri imgUri;
     ProgressDialog progressDialog;
     Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_san_pham);
        imgSanPham = findViewById(R.id.imgSanPham);
        edtTen = findViewById(R.id.edtTen);
        edtGia = findViewById(R.id.edtGia);
        edtLoai = findViewById(R.id.edtLoai);
        edtDanhGia = findViewById(R.id.edtDanhGia);
        edtMota = findViewById(R.id.edtMota);
        btnUpdate = findViewById(R.id.btnUpdate);
        toolbar = findViewById(R.id.toolBar);
        progressDialog = new ProgressDialog(this);
        db =FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Object object = getIntent().getSerializableExtra("sanpham");
        id = getIntent().getStringExtra("id");

        if (object instanceof SanPham){
            sanPham = (SanPham) object;
        }
        if (sanPham!=null){
            Glide.with(this).load(sanPham.getImgurl()).into(imgSanPham);
            edtTen.setText(sanPham.getName());
            edtGia.setText(String.valueOf(sanPham.getPrice()));
            edtLoai.setText(sanPham.getType());
            edtDanhGia.setText(sanPham.getRate());
            edtMota.setText(sanPham.getDes());
        }

        imgSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 6);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSanPham();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 6 && resultCode == RESULT_OK && data!=null&&data.getData()!=null){
            imgUri = data.getData();
            imgSanPham.setImageURI(imgUri);
        }
    }

    public void updateSanPham(){
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

        HashMap<String, Object> productUpdate = new HashMap<>();
        productUpdate.put("name",name);
        productUpdate.put("price",price);
        productUpdate.put("type",type);
        productUpdate.put("rate",rate);
        productUpdate.put("des",des);

        if (imgUri!=null){
            progressDialog.show();
            StorageReference reference = storage.getReference().child("sanpham/" + System.currentTimeMillis() + ".jpg");
            reference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imgSaveUri = uri.toString();
                            productUpdate.put("imgurl", imgSaveUri);

                            db.collection("SanPham").document(id).update(productUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(CapNhatSanPhamActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();

                                        startActivity(new Intent(CapNhatSanPhamActivity.this, MainAdminActivity.class));
                                        finish();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }else {
            db.collection("SanPham").document(id).update(productUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(CapNhatSanPhamActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                        startActivity(new Intent(CapNhatSanPhamActivity.this, MainAdminActivity.class));
                        finish();
                    }
                }
            });
        }

//        db.collection("SanPham").document(id).update(productUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(CapNhatSanPhamActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_LONG).show();
//                    progressDialog.dismiss();
//
//                    startActivity(new Intent(CapNhatSanPhamActivity.this, MainAdminActivity.class));
//                    finish();
//                }
//            }
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}