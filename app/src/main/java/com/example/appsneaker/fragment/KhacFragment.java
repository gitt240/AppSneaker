package com.example.appsneaker.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appsneaker.Activity.CapNhatThongTinActivity;
import com.example.appsneaker.Activity.DangNhapActivity;
import com.example.appsneaker.Activity.DoiPasswordActivity;
import com.example.appsneaker.Activity.DonHangActivity;
import com.example.appsneaker.Activity.LienHeActivity;
import com.example.appsneaker.R;
import com.example.appsneaker.model.NguoiDung;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import de.hdodenhof.circleimageview.CircleImageView;


public class KhacFragment extends Fragment {
    FirebaseAuth authUser;
    String id;
    FirebaseDatabase database;
    FirebaseFirestore db;

    TextView tvName, tvEmail;

    ProgressBar progressBar;

    ScrollView scrollOther;

    Button btnLogout, btnInforUser, btnChangePass,btnContact, btnOrderHistory;
    CircleImageView imgUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khac, container, false);
        // Inflate the layout for this fragment
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnInforUser =view.findViewById(R.id.btnInforUser);
        btnChangePass = view.findViewById(R.id.btnChangePass);
        btnContact = view.findViewById(R.id.btnContact);
        btnOrderHistory = view.findViewById(R.id.btnOrderHistory);
        imgUser = view.findViewById(R.id.imgUser);
        progressBar = view.findViewById(R.id.progressbar);
        scrollOther = view.findViewById(R.id.scrollOther);
        authUser = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        db =FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.VISIBLE);
        scrollOther.setVisibility(View.GONE);


//        Lấy thông tin user
        showData();


        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LienHeActivity.class));
            }
        });

//Đăng xuất
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authUser.signOut();
                startActivity(new Intent(getActivity(), DangNhapActivity.class));
                Toast.makeText(getContext(), "Đăng xuất thành công",Toast.LENGTH_LONG).show();
            }
        });

        btnInforUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), CapNhatThongTinActivity.class), 100);
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DoiPasswordActivity.class));
            }
        });

        btnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DonHangActivity.class));
            }
        });

        return view;
    }






    public void showData(){
        if (authUser.getCurrentUser() != null) {
            id = authUser.getCurrentUser().getUid();
        } else {
            Toast.makeText(getContext(), "Không tìm thấy người dùng", Toast.LENGTH_LONG).show();
        }


        db.collection("user").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value!=null && value.exists()){
                    NguoiDung nguoiDung = value.toObject(NguoiDung.class);
                    if (nguoiDung!=null){
                        tvName.setText(nguoiDung.getName());
                        tvEmail.setText(nguoiDung.getEmail());
//                    if (nguoiDung.getImage()!=null){
//                        if (getActivity() != null) {
//                            Glide.with(getActivity()).load(nguoiDung.getImage()).into(imgUser);
//                        }
//                    }

                        progressBar.setVisibility(View.GONE);
                        scrollOther.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == CapNhatThongTinActivity.RESULT_OK) {
                // Xử lý khi cập nhật thành công
                showData();
            }
        }
    }
}