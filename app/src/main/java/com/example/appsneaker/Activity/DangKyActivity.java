package com.example.appsneaker.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appsneaker.R;
import com.example.appsneaker.model.NguoiDung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class DangKyActivity extends AppCompatActivity {

        TextView tvLogin;
        EditText edtEmail, edtPass, edtPassConfirm, edtPhone, edtName;
        Button btnRegister;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog;
        FirebaseDatabase db;
        FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        tvLogin = findViewById(R.id.tvLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        edtPassConfirm = findViewById(R.id.edtPassConfirm);
        edtPhone = findViewById(R.id.edtPhone);
        edtName = findViewById(R.id.edtName);
        btnRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
        db = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                String passConfirm = edtPassConfirm.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String name = edtName.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    edtEmail.setError("Vui lòng nhập Email");
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    edtPass.setError("Vui lòng nhập mật khẩu");
                    return;
                }
                if (pass.length()<6){
                    edtPass.setError("Mật khẩu phải có ít nhất 6 kí tự");
                    return;
                }
                if (!TextUtils.equals(pass,passConfirm)){
                    edtPassConfirm.setError("Mật khẩu không khớp");
                    return;
                }
                if (TextUtils.isEmpty(phone)){
                    edtPhone.setError("Vui lòng nhập số điện thoại");
                    return;
                }
                if (phone.length() < 10){
                    edtPhone.setError("Số điện thoại không hợp lệ");
                    return;
                }
                if (TextUtils.isEmpty(name)){
                    edtName.setError("Vui lòng nhập họ tên");
                    return;
                }




                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {




                                if (task.isSuccessful()){
                                    NguoiDung nguoiDung = new NguoiDung(email, pass, phone, name);
                                    String id = task.getResult().getUser().getUid();
//                                    db.getReference().child("NguoiDung").child(id).setValue(nguoiDung);
                                    firestore.collection("user").document(id).set(nguoiDung);

                                    Toast.makeText(DangKyActivity.this,"Đăng ký thành công", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(DangKyActivity.this,DangNhapActivity.class));
                                    finish();

                                    progressDialog.dismiss();
                                }else {
                                    Toast.makeText(DangKyActivity.this,"Đăng ký thất bại", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}