package com.example.appsneaker.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appsneaker.R;
import com.example.appsneaker.adapter.PhanLoaiAdapter;
import com.example.appsneaker.adapter.SanPhamAdapter;
import com.example.appsneaker.model.Loai;
import com.example.appsneaker.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class SanPhamFragment extends Fragment {

    RecyclerView rvSanPham, rvLoai;
    ArrayList<SanPham> listSanPham;
    ArrayList<Loai> listLoai;
    SanPhamAdapter sanPhamAdapter;
    PhanLoaiAdapter phanLoaiAdapter;
    FirebaseFirestore db;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    NestedScrollView scrollSP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_san_pham, container, false);
        rvSanPham = view.findViewById(R.id.rvSanPham);
        rvLoai = view.findViewById(R.id.rvLoai);
        progressBar = view.findViewById(R.id.progressbar);
        linearLayout = view.findViewById(R.id.linearLayout);
        scrollSP = view.findViewById(R.id.scrollSP);
        db = FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);


//        Loại
        rvLoai.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        listLoai = new ArrayList<>();
        phanLoaiAdapter = new PhanLoaiAdapter(getContext(), listLoai, this::onItemClick);
        rvLoai.setAdapter(phanLoaiAdapter);

        db.collection("Loai").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentLoai : task.getResult()){
                        Loai loai = documentLoai.toObject(Loai.class);
                        listLoai.add(loai);
                        phanLoaiAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


//      Sản phẩm
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvSanPham.setLayoutManager(linearLayoutManager);
        rvSanPham.setNestedScrollingEnabled(false);
        listSanPham = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(getContext(), listSanPham);
        rvSanPham.setAdapter(sanPhamAdapter);

        db.collection("SanPham").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        SanPham sanPham = document.toObject(SanPham.class);
                        listSanPham.add(sanPham);
                        sanPhamAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                }else {
                    Toast.makeText(getContext(), "Lỗi: " +task.getException(),Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
    public void onItemClick(String type) {
        // Thực hiện các thao tác cần thiết dựa trên type, chẳng hạn như lọc danh sách sản phẩm theo type và cập nhật RecyclerView
        locSanPham(type);
    }

    private void locSanPham(String type) {

        ArrayList<SanPham> listLocSP = new ArrayList<>();
        for (SanPham sanPham : listSanPham) {
            if (sanPham.getType().equalsIgnoreCase(type)) {
                listLocSP.add(sanPham);
            }
        }
        sanPhamAdapter.setList(listLocSP);
        sanPhamAdapter.notifyDataSetChanged();
    }
}