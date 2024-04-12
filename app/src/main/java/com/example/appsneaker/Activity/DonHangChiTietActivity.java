package com.example.appsneaker.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.appsneaker.R;
import com.example.appsneaker.adapter.DonHangChiTietAdapter;
import com.example.appsneaker.model.DonHang;
import com.example.appsneaker.model.GioHang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class DonHangChiTietActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;
    RecyclerView rvOrderDelaites;
    ArrayList<GioHang> list;
    DonHangChiTietAdapter adapter;
    Toolbar toolbar;
    TextView tvName, tvPhone, tvEmail, tvDate, tvTime, tvStatus, tvAddress, tvThanhTien, tvTongTien;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang_chi_tiet);
        toolbar = findViewById(R.id.toolBar);
        rvOrderDelaites = findViewById(R.id.rvOrderDetailes);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvDate = findViewById(R.id.tvDate);
//        tvTime = findViewById(R.id.tvTime);
        tvStatus = findViewById(R.id.tvStatus);
        tvAddress = findViewById(R.id.tvAddress);
        tvThanhTien = findViewById(R.id.tvThanhTien);
        tvTongTien = findViewById(R.id.tvTongCong);
        list = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        id = getIntent().getStringExtra("id");

        rvOrderDelaites.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DonHangChiTietAdapter(this, list);
        rvOrderDelaites.setNestedScrollingEnabled(false);
        rvOrderDelaites.setAdapter(adapter);

        getData();


    }

    public void getData() {
        db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("DonHang").document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            tvName.setText(documentSnapshot.getString("name"));
                            tvPhone.setText(documentSnapshot.getString("phone"));
                            tvEmail.setText(documentSnapshot.getString("email"));
                            tvAddress.setText(documentSnapshot.getString("address"));
                            tvStatus.setText(documentSnapshot.getString("status"));
//                            tvTime.setText(documentSnapshot.getString("time"));
                            tvDate.setText(documentSnapshot.getString("date"));
                            tvThanhTien.setText("$" + documentSnapshot.get("totalPrice"));
                            tvTongTien.setText("$" + documentSnapshot.get("totalPrice"));

                            ArrayList<HashMap<String, Object>> listCartItem = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("cartItem");

                            if (listCartItem != null) {
                                for (HashMap<String, Object> cartItem : listCartItem) {
                                    String name = (String) cartItem.get("name");
                                    int price = ((Long) cartItem.get("price")).intValue();
                                    String quantity = (String) cartItem.get("quantity");
                                    String imgurl = (String) cartItem.get("imgurl");

                                    GioHang gioHang = new GioHang(name, quantity, imgurl, price);
                                    list.add(gioHang);
                                }
                            }

                            // Hiển thị dữ liệu trong adapter
                            adapter.notifyDataSetChanged();
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