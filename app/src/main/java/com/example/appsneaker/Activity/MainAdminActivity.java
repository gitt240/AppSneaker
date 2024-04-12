package com.example.appsneaker.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.appsneaker.R;
import com.example.appsneaker.fragment.DonHangAdminFragment;
import com.example.appsneaker.fragment.KhacAdminFragment;
import com.example.appsneaker.fragment.SanPhamAdminFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainAdminActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView btmNavAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        toolbar = findViewById(R.id.toolBar);
        btmNavAdmin = findViewById(R.id.btmNavAdmin);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new SanPhamAdminFragment()).commit();

        btmNavAdmin.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId()==R.id.mSanPhamAM){
                    fragment = new SanPhamAdminFragment();
                }else if (item.getItemId()==R.id.mHoaDonAM){
                    fragment = new DonHangAdminFragment();
                }else if (item.getItemId()==R.id.mKhacAM){
                    fragment = new KhacAdminFragment();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit();

                toolbar.setTitle(item.getTitle());

                return true;
            }
        });
    }
}