package com.example.appsneaker.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appsneaker.R;
import com.example.appsneaker.adapter.DonHangAdminAdapter;
import com.example.appsneaker.model.DonHang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DonHangAdminFragment extends Fragment {
    RecyclerView rvHoaDonAdmin;
    ArrayList<DonHang> list;
    FirebaseFirestore db;
    DonHangAdminAdapter adapter;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donhang_admin,container,false);
        rvHoaDonAdmin = view.findViewById(R.id.rvHoaDonAdmin);
        list = new ArrayList<>();
        progressBar = view.findViewById(R.id.progressbar);
        db = FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.VISIBLE);
        rvHoaDonAdmin.setVisibility(View.GONE);

        rvHoaDonAdmin.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DonHangAdminAdapter(list, getContext());
        rvHoaDonAdmin.setAdapter(adapter);
        getDataOrder();

        return view;
    }

    public void getDataOrder() {
        db.collectionGroup("DonHang").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot :task.getResult()){
                        DonHang donHang = documentSnapshot.toObject(DonHang.class);
                        String id = documentSnapshot.getId();
                        donHang.setId(id);
                        list.add(donHang);
                    }
                    adapter.notifyDataSetChanged();

                    progressBar.setVisibility(View.GONE);
                    rvHoaDonAdmin.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
