package com.example.appsneaker.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appsneaker.Activity.XacNhanDatHangActivity;
import com.example.appsneaker.R;
import com.example.appsneaker.adapter.GioHangAdapter;
import com.example.appsneaker.model.GioHang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GioHangragment extends Fragment {

    Button btnCart;
    RecyclerView rvCart;
    FirebaseFirestore db;
    FirebaseAuth userAuth;
    ArrayList<GioHang> list;
    GioHangAdapter adapter;
    ProgressBar progressBar;
    LinearLayout linearLayout, noCart;
    NestedScrollView scrollCart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gio_hang, container, false);
        btnCart = view.findViewById(R.id.btnCart);
        rvCart = view.findViewById(R.id.rvCart);
        progressBar = view.findViewById(R.id.progressbar);
        linearLayout = view.findViewById(R.id.linearLayoutCart);
        noCart = view.findViewById(R.id.noCart);
        scrollCart = view.findViewById(R.id.scrollCart);
        userAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        noCart.setVisibility(View.GONE);

        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new GioHangAdapter(getContext(), list);
        rvCart.setAdapter(adapter);

        db.collection("CurrentUser").document(userAuth.getCurrentUser().getUid())
                .collection("GioHang").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()){
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                GioHang gioHang = documentSnapshot.toObject(GioHang.class);

                                String documentid = documentSnapshot.getId();
                                gioHang.setId(documentid);

                                list.add(gioHang);
                                adapter.notifyDataSetChanged();

//                                loadFragment(new GioHangragment());

                                progressBar.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.GONE);
                            noCart.setVisibility(View.VISIBLE);
                        }
                    }
                });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addDataOrder();
                Intent intent = new Intent(getContext(), XacNhanDatHangActivity.class);
                intent.putExtra("cartAll", list);
                startActivity(intent);


//                deleteAllCartItems();
            }
        });


        return view;
    }


//    public void addDataOrder() {
//        Map<String, Object> orderData = new HashMap<>();
//        for (int i = 0; i < list.size(); i++) {
//            GioHang gioHang = list.get(i);
//            orderData.put("name" , gioHang.getName());
//            orderData.put("price" , gioHang.getPrice());
//            orderData.put("quantity" , gioHang.getQuantity());
//            orderData.put("imgurl" , gioHang.getImgurl());
//
//            db.collection("CurrentUser").document(userAuth.getCurrentUser().getUid())
//                    .collection("XacNhanDonHang").add(orderData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentReference> task) {
//
//                        }
//                    });
//
//        }
//    }

    // Phương thức để xoá tất cả sản phẩm trong giỏ hàng
//    private void deleteAllCartItems() {
//        db.collection("GioHang").document(userAuth.getCurrentUser().getUid())
//                .collection("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            // Duyệt qua danh sách sản phẩm và xoá từng sản phẩm
//                            for (DocumentSnapshot document : task.getResult()) {
//                                document.getReference().delete();
//                            }
//
//                            // Xoá tất cả mục trong danh sách và cập nhật giao diện
//                            list.clear();
//                            adapter.notifyDataSetChanged();
//
//                            // Hiển thị thông báo hoặc thực hiện các hành động khác sau khi xoá thành công
//                            // ...
//
//                            // Hiển thị hoặc ẩn các thành phần tùy thuộc vào tình trạng giỏ hàng sau khi xoá
//                            showHideCartElements();
//                        } else {
//                            Log.d("TAG", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }
//
//    // Phương thức để hiển thị hoặc ẩn các thành phần tùy thuộc vào tình trạng giỏ hàng
//    private void showHideCartElements() {
//        if (list.isEmpty()) {
//            // Nếu danh sách giỏ hàng rỗng, ẩn RecyclerView và hiển thị thông báo
//            linearLayout.setVisibility(View.GONE);
//            noCart.setVisibility(View.VISIBLE);
//        } else {
//            // Nếu danh sách giỏ hàng không rỗng, hiển thị RecyclerView và ẩn thông báo
//            linearLayout.setVisibility(View.VISIBLE);
//            noCart.setVisibility(View.GONE);
//        }
//    }
}