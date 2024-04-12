package com.example.appsneaker.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appsneaker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DoiPasswordActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText edtOldPass, edtNewPass, edtPassConfirm;
    FirebaseUser user;
    FirebaseAuth auth;
    Button btnChangePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_password);
        toolbar = findViewById(R.id.toolBar);
        edtOldPass = findViewById(R.id.edtOldPass);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtPassConfirm = findViewById(R.id.edtPassConfirm);
        btnChangePass = findViewById(R.id.btnChangePass);
        auth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void updatePassword(){
        String oldpass = edtOldPass.getText().toString();
        String newpass = edtNewPass.getText().toString();
        String passconfirm = edtPassConfirm.getText().toString();

        if (oldpass.isEmpty()){
            edtOldPass.setError("Nhập password hiện tại");
            return;
        }
        if (newpass.length() < 6){
            edtNewPass.setError("Mật khẩu phải có ít nhất 6 kí tự");
            return;
        }
        if (!passconfirm.equals(newpass)){
            edtPassConfirm.setError("Mật khẩu không khớp");
            return;
        }

        user = auth.getCurrentUser();
        user.updatePassword(newpass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    auth.signOut();
                    startActivity(new Intent(DoiPasswordActivity.this, DangNhapActivity.class));
                    finish();
                    Toast.makeText(DoiPasswordActivity.this,"Đổi mật khẩu thành công",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(DoiPasswordActivity.this,"Đổi mật khẩu không thành công",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}