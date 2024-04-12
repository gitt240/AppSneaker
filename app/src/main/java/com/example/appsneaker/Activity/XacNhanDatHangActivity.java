package com.example.appsneaker.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appsneaker.R;
import com.example.appsneaker.adapter.XacNhanDonHangAdapter;
import com.example.appsneaker.model.GioHang;
import com.example.appsneaker.model.NguoiDung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class XacNhanDatHangActivity extends AppCompatActivity {

    TextView tvName, tvPhone, tvThanhTien, tvTongCong, tvAddress;
    Button btnOrder;
    LinearLayout linearAddress;
    FirebaseFirestore db;
    FirebaseAuth userAuth;
    Toolbar toolbar;
    RecyclerView rvConfirmCart;
    ArrayList<GioHang> list;
    XacNhanDonHangAdapter adapter;
    String id;
    GioHang gioHang = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_nhan_dat_hang);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvThanhTien = findViewById(R.id.tvThanhTien);
        tvTongCong = findViewById(R.id.tvTongCong);
        tvAddress = findViewById(R.id.tvAddress);
        btnOrder = findViewById(R.id.btnOrder);
        linearAddress = findViewById(R.id.linearAddress);
        toolbar = findViewById(R.id.toolBar);
        rvConfirmCart = findViewById(R.id.rvConfirmCart);
        userAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        id = userAuth.getCurrentUser().getUid();
        list = new ArrayList<>();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String address = getIntent().getStringExtra("address");
        tvAddress.setText(address);



        showData();
        Object object = getIntent().getSerializableExtra("cartAll");
        if (object != null && object instanceof ArrayList){
            list = (ArrayList<GioHang>) object;
            rvConfirmCart.setLayoutManager(new LinearLayoutManager(this));
            adapter = new XacNhanDonHangAdapter(this, list);
            rvConfirmCart.setAdapter(adapter);
            int totalPrice = adapter.calculateTotalPrice(list);
            tvTongCong.setText("$" + totalPrice);
            tvThanhTien.setText("$"+ totalPrice);
        }else {
            Toast.makeText(XacNhanDatHangActivity.this,"Sai",Toast.LENGTH_LONG).show();
        }


        linearAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XacNhanDatHangActivity.this, AddressActivity.class);
                intent.putExtra("cartAll", list);
                startActivity(intent);
            }
        });

//        db.collection("CurrentUser").document(userAuth.getCurrentUser().getUid())
//                .collection("DiaChi").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                    }
//                });


//        db.collection("CurrentUser").document(id).collection("XacNhanDonHang").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            list.clear();
//                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
//                                GioHang gioHang = documentSnapshot.toObject(GioHang.class);
//                                list.add(gioHang);
//                                adapter.notifyDataSetChanged();
//                            }
//                        }
//                    }
//                });


//        deleteAllItem();

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvAddress.length()<1){
                    Toast.makeText(XacNhanDatHangActivity.this, "Vui lòng chọn địa chỉ", Toast.LENGTH_LONG).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(XacNhanDatHangActivity.this);
                builder.setMessage("Bạn có chắc muốn đặt hàng?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addData();
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }



    public void showData() {
        String id = userAuth.getCurrentUser().getUid();

        db.collection("user").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                NguoiDung nguoiDung = value.toObject(NguoiDung.class);
                tvName.setText(nguoiDung.getName());
                tvPhone.setText(nguoiDung.getPhone());
            }
        });
    }

    public void addData(){
        String saveCurrentDate, saveCurrentTime;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        String address = tvAddress.getText().toString();
        String name = tvName.getText().toString();
        String phone = tvPhone.getText().toString();
        int totalPrice = adapter.calculateTotalPrice(list);
        String status = "Chờ xác nhận";
        String email = userAuth.getCurrentUser().getEmail();

        ArrayList<GioHang> cartItem = adapter.getCartItem();

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("name", name);
        orderMap.put("phone", phone);
        orderMap.put("address", address);
        orderMap.put("totalPrice", totalPrice);
        orderMap.put("status", status);
        orderMap.put("date", saveCurrentDate);
        orderMap.put("email", email);
        orderMap.put("cartItem", cartItem);

        db.collection("CurrentUser").document(id).collection("DonHang")
                .add(orderMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(XacNhanDatHangActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    }
                });

        deleteCart();
        startActivity(new Intent(XacNhanDatHangActivity.this, MainActivity.class));
    }


//    public void getData(){
//        db.collection("DonHang").document(id)
//                .collection("DonHangChiTiet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            list.clear();
//                            adapter.notifyDataSetChanged();
//                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
//                                DonHang donHang = documentSnapshot.toObject(DonHang.class);
//                                list.add(donHang);
//                                adapter.notifyDataSetChanged();
//                            }
//
//                        }
//                    }
//                });
//    }

    public void deleteCart(){
        db.collection("CurrentUser").document(userAuth.getCurrentUser().getUid()).collection("GioHang")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot :task.getResult()){
                                documentSnapshot.getReference().delete();
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