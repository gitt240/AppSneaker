package com.example.appsneaker.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.appsneaker.R;
import com.example.appsneaker.adapter.AddressAdapter;
import com.example.appsneaker.model.Address;
import com.example.appsneaker.model.GioHang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectAddress{

    EditText edtAddress;
    Button btnAddAddress, btnConfirmAddress, btnSelectAddress;
    LinearLayout linearAddress;
    Toolbar toolbar;
    FirebaseFirestore db;
    FirebaseAuth userAuth;
    ArrayList<GioHang> list;
    ArrayList<Address> listAddress;
    AddressAdapter adapter;
    RecyclerView rvAddress;
    View line;
    ProgressBar progressBar;
    ScrollView scrollAddrees;
    String addressSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        edtAddress = findViewById(R.id.edtAddress);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        btnConfirmAddress = findViewById(R.id.btnConfirmAddress);
        btnSelectAddress = findViewById(R.id.btnSelectAddress);
        linearAddress = findViewById(R.id.linearAddress);
        rvAddress = findViewById(R.id.rvAddress);
        progressBar = findViewById(R.id.progressbar);
        line = findViewById(R.id.line);
        scrollAddrees = findViewById(R.id.scrollAddress);
        toolbar = findViewById(R.id.toolBar);
        db = FirebaseFirestore.getInstance();
        userAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        linearAddress.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        scrollAddrees.setVisibility(View.GONE);





        list = new ArrayList<>();
        Object object = getIntent().getSerializableExtra("cartAll");
        if (object instanceof ArrayList){
            list = (ArrayList<GioHang>) object;
        }

        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddAddress.setVisibility(View.GONE);
                linearAddress.setVisibility(View.VISIBLE);
            }
        });

        btnConfirmAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = edtAddress.getText().toString();
                HashMap<String, Object> addressMap = new HashMap<>();
                addressMap.put("address", address);

                db.collection("CurrentUser").document(userAuth.getCurrentUser().getUid())
                        .collection("DiaChi").add(addressMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(AddressActivity.this, "Thêm địa chỉ thành công",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                Intent intent = new Intent(AddressActivity.this, XacNhanDatHangActivity.class);
                intent.putExtra("address", address);
                intent.putExtra("cartAll", list);
                startActivity(intent);
                finish();
            }
        });

        btnSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this, XacNhanDatHangActivity.class);
                intent.putExtra("address", addressSelected);
                intent.putExtra("cartAll", list);
                startActivity(intent);
                finish();
            }
        });



        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        listAddress = new ArrayList<>();
        adapter = new AddressAdapter(listAddress, this, this);
        rvAddress.setAdapter(adapter);

        getAddress();
    }

    public void getAddress(){
        db.collection("CurrentUser").document(userAuth.getCurrentUser().getUid())
                .collection("DiaChi").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                Address address = documentSnapshot.toObject(Address.class);
                                listAddress.add(address);
                                adapter.notifyDataSetChanged();
                            }
                            if (!listAddress.isEmpty()) {
                                btnSelectAddress.setVisibility(View.VISIBLE);
                                line.setVisibility(View.VISIBLE);
                            }else {
                                btnSelectAddress.setVisibility(View.GONE);
                                line.setVisibility(View.GONE);
                            }

                            progressBar.setVisibility(View.GONE);
                            scrollAddrees.setVisibility(View.VISIBLE);
                        }

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void setAddress(String address) {
        addressSelected = address;
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        sendIntentData();
//    }

//    private void sendIntentData() {
//        String address = edtAddress.getText().toString();
//        Intent intent = new Intent(AddressActivity.this, XacNhanDatHangActivity.class);
//        intent.putExtra("address", address);
//        intent.putExtra("cartAll", list);
//        startActivity(intent);
//    }
}