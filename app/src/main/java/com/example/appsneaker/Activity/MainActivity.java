package com.example.appsneaker.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.appsneaker.R;
import com.example.appsneaker.adapter.LoaiAdapter;
import com.example.appsneaker.adapter.SanPhamAdapter;
import com.example.appsneaker.fragment.GioHangragment;
import com.example.appsneaker.fragment.KhacFragment;
import com.example.appsneaker.fragment.SanPhamFragment;
import com.example.appsneaker.fragment.TrangChuFragment;
import com.example.appsneaker.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvLoai;
    LoaiAdapter loaiAdapter;

    CircleImageView imgUser;
    TextView tvName;
    Toolbar toolbar;
    FirebaseUser user;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    RecyclerView rvSearch;
    ArrayList<SanPham> list;
    SanPhamAdapter sanPhamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView btmNav =(BottomNavigationView) findViewById(R.id.btmNav);
        toolbar = findViewById(R.id.toolBar);
        rvSearch = findViewById(R.id.rvSearch);
        list = new ArrayList<>();
        sanPhamAdapter =new SanPhamAdapter(this,list);
        db = FirebaseFirestore.getInstance();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new TrangChuFragment()).commit();

        btmNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId()==R.id.mTrangChu){
                    fragment = new TrangChuFragment();
                }
                else if (item.getItemId()==R.id.mSanPham){
                    fragment=new SanPhamFragment();
                }
                else if (item.getItemId()==R.id.mGioHang){
                    fragment=new GioHangragment();
                }
                else if (item.getItemId()==R.id.mKhac){
                    fragment=new KhacFragment();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentLayout, fragment)
                        .commit();

                toolbar.setTitle(item.getTitle());
                return true;
            }
        });

//        rvLoai = findViewById(R.id.rvLoai);
//        imgUser = findViewById(R.id.imgUser);
//        tvName = findViewById(R.id.tvName);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
//        rvLoai.setLayoutManager(linearLayoutManager);
//
//        FirebaseRecyclerOptions<Loai> options =
//                new FirebaseRecyclerOptions.Builder<Loai>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("loai"), Loai.class)
//                        .build();
//
//        loaiAdapter = new LoaiAdapter(options);
//        rvLoai.setAdapter(loaiAdapter);
        rvSearch.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rvSearch.setAdapter(sanPhamAdapter);
        rvSearch.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.mSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSP(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               searchSP(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    public void searchSP(String name){
        if (name.isEmpty()) {
            // Nếu chuỗi tìm kiếm rỗng, không thực hiện truy vấn Firestore và thoát khỏi hàm
            list.clear();
            sanPhamAdapter.notifyDataSetChanged();;
            return;
        }
        db.collection("SanPham").whereGreaterThanOrEqualTo("name", name)
                .whereLessThanOrEqualTo("name", name +"z").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult()!=null){
                    list.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        SanPham sanPham = documentSnapshot.toObject(SanPham.class);
                        list.add(sanPham);
                    }

                    sanPhamAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    //    public void showUserInfor(){
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user == null){
//            return;
//        }
//
//        String name = user.getDisplayName();
//        String email = user.getEmail();
//        Uri img = user.getPhotoUrl();
//
//        if (name ==null){
//            tvName.setVisibility(View.GONE);
//        }else {
//            tvName.setVisibility(View.VISIBLE);
//            tvName.setText(name);
//        }
//
//        Glide.with(this).load(img).error(R.drawable.avatar_default).into(imgUser);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        loaiAdapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        loaiAdapter.stopListening();
//    }
}