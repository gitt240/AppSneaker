package com.example.appsneaker.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appsneaker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DangNhapActivity extends AppCompatActivity {

    TextView tvRegister;
    EditText edtEmail, edtPass;
    Button btnLogin;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
            tvRegister = findViewById(R.id.tvRegister);
            edtEmail = findViewById(R.id.edtEmail);
            edtPass = findViewById(R.id.edtPass);
            btnLogin = findViewById(R.id.btnLogin);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = edtEmail.getText().toString().trim();
                    String pass = edtPass.getText().toString().trim();

                    if (TextUtils.isEmpty(email)){
                        Toast.makeText(DangNhapActivity.this,"Vui lòng nhập email", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(pass)){
                        Toast.makeText(DangNhapActivity.this,"Vui lòng nhập mật khẩu", Toast.LENGTH_LONG).show();
                        return;
                    }

                    firebaseAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(DangNhapActivity.this,"Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                        if (firebaseAuth.getCurrentUser().getEmail().equals("admin@gmail.com")){
                                            startActivity(new Intent(DangNhapActivity.this, MainAdminActivity.class));
                                            finish();
                                        }
                                        else {
                                            startActivity(new Intent(DangNhapActivity.this, MainActivity.class));
                                            finish();
                                        }

                                    }else {
                                        Toast.makeText(DangNhapActivity.this,"Email hoặc mật khẩu sai", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            });

            tvRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DangNhapActivity.this, DangKyActivity.class));
                }
            });

    }
}