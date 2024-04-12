package com.example.appsneaker.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.appsneaker.R;
import com.example.appsneaker.adapter.SanPhamAdapter;
import com.example.appsneaker.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SPTheoLoai extends AppCompatActivity {

    SanPhamAdapter sanPhamAdapter;
    ArrayList<SanPham> list;
    FirebaseFirestore db;
    Toolbar toolbar;
    RecyclerView rvSPTheoLoai;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sptheo_loai);
        rvSPTheoLoai=findViewById(R.id.rvSPTheoLoai);
        progressBar =findViewById(R.id.progressbar);
        toolbar=findViewById(R.id.toolBar);
        db = FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.VISIBLE);
        rvSPTheoLoai.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        String type = getIntent().getStringExtra("type");

        rvSPTheoLoai.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(this, list);
        rvSPTheoLoai.setAdapter(sanPhamAdapter);

//        NIke
        if (type!= null && type.equalsIgnoreCase("nike")){
            db.collection("SanPham").whereEqualTo("type", "nike").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        SanPham sanPham = documentSnapshot.toObject(SanPham.class);
                        list.add(sanPham);
                        sanPhamAdapter.notifyDataSetChanged();

                        toolbar.setTitle("Nike");
                        progressBar.setVisibility(View.GONE);
                        rvSPTheoLoai.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

//        Puma
        if (type!= null && type.equalsIgnoreCase("puma")){
            db.collection("SanPham").whereEqualTo("type", "puma").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        SanPham sanPham = documentSnapshot.toObject(SanPham.class);
                        list.add(sanPham);
                        sanPhamAdapter.notifyDataSetChanged();

                        toolbar.setTitle("Puma");
                        progressBar.setVisibility(View.GONE);
                        rvSPTheoLoai.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

//        Adidas
        if (type!= null && type.equalsIgnoreCase("adidas")){
            db.collection("SanPham").whereEqualTo("type", "adidas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        SanPham sanPham = documentSnapshot.toObject(SanPham.class);
                        list.add(sanPham);
                        sanPhamAdapter.notifyDataSetChanged();

                        toolbar.setTitle("Adidas");
                        progressBar.setVisibility(View.GONE);
                        rvSPTheoLoai.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

//        Reebok
        if (type!= null && type.equalsIgnoreCase("reebok")){
            db.collection("SanPham").whereEqualTo("type", "reebok").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        SanPham sanPham = documentSnapshot.toObject(SanPham.class);
                        list.add(sanPham);
                        sanPhamAdapter.notifyDataSetChanged();

                        toolbar.setTitle("Reebok");
                        progressBar.setVisibility(View.GONE);
                        rvSPTheoLoai.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}