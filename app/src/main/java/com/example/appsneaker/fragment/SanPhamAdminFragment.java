package com.example.appsneaker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appsneaker.Activity.ThemSanPhamActivity;
import com.example.appsneaker.R;
import com.example.appsneaker.adapter.LoaiAdapter;
import com.example.appsneaker.adapter.PhanLoaiAdapter;
import com.example.appsneaker.adapter.SanPhamAdminAdapter;
import com.example.appsneaker.model.Loai;
import com.example.appsneaker.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SanPhamAdminFragment extends Fragment {

    SanPhamAdminAdapter sanPhamAdminAdapter;
    ArrayList<SanPham> listSanPham;
    ArrayList<Loai> listLoai;
    PhanLoaiAdapter phanLoaiAdapter;
    FirebaseFirestore db;
    ProgressBar progressBar;
    NestedScrollView scrollSP;
    RecyclerView rvLoai, rvSanPhamAdmin;
    FloatingActionButton floatAdd;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sanpham_admin, container,false);
        rvLoai = view.findViewById(R.id.rvLoai);
        rvSanPhamAdmin = view.findViewById(R.id.rvSanPhamAdmin);
        scrollSP = view.findViewById(R.id.scrollSP);
        progressBar = view.findViewById(R.id.progressbar);
        floatAdd = view.findViewById(R.id.floatAdd);
        db = FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.VISIBLE);
        scrollSP.setVisibility(View.GONE);

        rvLoai.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        listLoai = new ArrayList<>();
        phanLoaiAdapter = new PhanLoaiAdapter(getContext(), listLoai, this::onItemClick);
        rvLoai.setAdapter(phanLoaiAdapter);

        getDataLoai();

        rvSanPhamAdmin.setLayoutManager(new LinearLayoutManager(getContext()));
        listSanPham = new ArrayList<>();
        sanPhamAdminAdapter = new SanPhamAdminAdapter(getContext(), listSanPham);
        rvSanPhamAdmin.setAdapter(sanPhamAdminAdapter);
        getDataSanPham();

        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ThemSanPhamActivity.class));
            }
        });

        return view;
    }

    public void getDataLoai(){
        db.collection("Loai").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Loai loai = documentSnapshot.toObject(Loai.class);
                        listLoai.add(loai);
                    }
                    phanLoaiAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void getDataSanPham(){
        db.collection("SanPham").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        SanPham sanPham = documentSnapshot.toObject(SanPham.class);
                        String id = documentSnapshot.getId();
                        sanPham.setId(id);

                        listSanPham.add(sanPham);
                        sanPhamAdminAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        scrollSP.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void onItemClick(String type){
        locSanPham(type);
    }
    public void locSanPham(String type){
        ArrayList<SanPham> listLocSP = new ArrayList<>();
        for (SanPham sanPham : listSanPham){
            if (sanPham.getType().equalsIgnoreCase(type)){
                listLocSP.add(sanPham);
            }
        }
        sanPhamAdminAdapter.setList(listLocSP);
        sanPhamAdminAdapter.notifyDataSetChanged();
    }
}
