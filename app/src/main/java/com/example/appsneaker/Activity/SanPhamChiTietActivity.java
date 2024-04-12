package com.example.appsneaker.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appsneaker.R;
import com.example.appsneaker.model.SanPham;
import com.example.appsneaker.model.SanPhamBanChay;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SanPhamChiTietActivity extends AppCompatActivity {

    ImageView imgSanPham, imgBack, imgTang, imgGiam;
    TextView tvName, tvRate, tvPrice, tvDes, tvQuantity;
    Button btnAddCart;
    SanPham sanPham = null;
    SanPhamBanChay sanPhamBanChay;
    FirebaseFirestore db;
    FirebaseAuth userAuth;
    int totalQuantity = 1;
    int totalPrice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham_chi_tiet);
        tvName = findViewById(R.id.tvName);
        tvRate = findViewById(R.id.tvRate);
        tvPrice = findViewById(R.id.tvPrice);
        tvDes = findViewById(R.id.tvDes);
        imgSanPham = findViewById(R.id.imgSanPham);
        imgBack = findViewById(R.id.imgBack);
        btnAddCart = findViewById(R.id.btnAddCart);
        tvQuantity = findViewById(R.id.tvQuantity);
        imgTang = findViewById(R.id.imgTang);
        imgGiam = findViewById(R.id.imgGiam);
        db = FirebaseFirestore.getInstance();
        userAuth = FirebaseAuth.getInstance();

        Object object = getIntent().getSerializableExtra("chitiet");
        if (object instanceof SanPham) {
            sanPham = (SanPham) object;
        }

        if (sanPham != null) {
            Glide.with(this).load(sanPham.getImgurl()).into(imgSanPham);
            tvName.setText(sanPham.getName());
            tvRate.setText(sanPham.getRate());
            tvPrice.setText("$" + sanPham.getPrice() * totalQuantity);
            tvDes.setText(sanPham.getDes());
            totalPrice = sanPham.getPrice() * totalQuantity;
        }

//        Object objectBC = getIntent().getSerializableExtra("chitietBC");
//        if (objectBC instanceof SanPhamBanChay) {
//            sanPhamBanChay = (SanPhamBanChay) objectBC;
//        }
//
//        if (sanPhamBanChay != null) {
//            Glide.with(this).load(sanPhamBanChay.getImgurl()).into(imgSanPham);
//            tvName.setText(sanPhamBanChay.getName());
//            tvRate.setText(sanPhamBanChay.getRate());
//            tvPrice.setText("$" + sanPhamBanChay.getPrice() * totalQuantity);
//            tvDes.setText(sanPhamBanChay.getDes());
//        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity > 1) {
                    totalQuantity--;
                    tvQuantity.setText(String.valueOf(totalQuantity));
                    tvPrice.setText("$" + sanPham.getPrice() * totalQuantity);
                    totalPrice = sanPham.getPrice() * totalQuantity;
                }
            }
        });

        imgTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity < 20) {
                    totalQuantity++;
                    tvQuantity.setText(String.valueOf(totalQuantity));
                    tvPrice.setText("$" + sanPham.getPrice() * totalQuantity);
                    totalPrice = sanPham.getPrice() * totalQuantity;
                }
            }
        });

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });


    }

    private void addToCart() {
        HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("name", sanPham.getName());
        cartMap.put("price", totalPrice);
        cartMap.put("quantity", tvQuantity.getText().toString());
        cartMap.put("imgurl", sanPham.getImgurl());

        db.collection("CurrentUser").document(userAuth.getCurrentUser().getUid())
                .collection("GioHang").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(SanPhamChiTietActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_LONG).show();
                    }
                });
    }
}