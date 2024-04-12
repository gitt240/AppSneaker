package com.example.appsneaker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appsneaker.Activity.DangNhapActivity;
import com.example.appsneaker.R;
import com.google.firebase.auth.FirebaseAuth;

public class KhacAdminFragment extends Fragment {

    Button btnLogout;
    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khac_admin,container,false);
        btnLogout = view.findViewById(R.id.btnLogout);
        auth = FirebaseAuth.getInstance();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getContext(), DangNhapActivity.class));
                getActivity().finish();
                Toast.makeText(getContext(), "Đăng xuất thành công",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
