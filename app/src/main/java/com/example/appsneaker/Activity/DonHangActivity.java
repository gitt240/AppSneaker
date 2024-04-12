package com.example.appsneaker.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.appsneaker.R;
import com.example.appsneaker.adapter.DonHangAdapter;
import com.example.appsneaker.model.DonHang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DonHangActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth auth;
    RecyclerView rvOrder;
    Toolbar toolbar;
    ArrayList<DonHang> list;
    DonHangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang);
        toolbar = findViewById(R.id.toolBar);
        rvOrder = findViewById(R.id.rvOrder);
        list = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        rvOrder.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DonHangAdapter(this, list);
        rvOrder.setAdapter(adapter);

        getDataOrder();

    }

    public void getDataOrder(){
        db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("DonHang").orderBy("date", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                DonHang donHang = documentSnapshot.toObject(DonHang.class);
                                String id = documentSnapshot.getId();
                                donHang.setId(id);

                                list.add(donHang);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}