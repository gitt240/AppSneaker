package com.example.appsneaker.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.appsneaker.R;
import com.example.appsneaker.adapter.LoaiAdapter;
import com.example.appsneaker.adapter.SanPhamBanChayAdapter;
import com.example.appsneaker.model.Loai;
import com.example.appsneaker.model.NguoiDung;
import com.example.appsneaker.model.SanPham;
import com.example.appsneaker.model.SanPhamBanChay;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class TrangChuFragment extends Fragment {
    TextView tvName;
    ImageSlider imgSlideShow;
    String name, email, phoneNumber, id;
    ArrayList<Loai> listLoai;
    ArrayList<SanPham> listBanChay;
    LoaiAdapter loaiAdapter;
    SanPhamBanChayAdapter sanPhamBanChayAdapter;
    RecyclerView rvLoai, rvSPBanChay;
    NestedScrollView scrollHome;
    ProgressBar progressBar;
    FirebaseAuth userAuth;
    FirebaseFirestore db;
    FirebaseDatabase database;
    CircleImageView imgUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);
        tvName = view.findViewById(R.id.tvName);
        imgUser = view.findViewById(R.id.imgUser);
        imgSlideShow = view.findViewById(R.id.imgSlideShow);
        rvLoai = view.findViewById(R.id.rvLoai);
        rvSPBanChay = view.findViewById(R.id.rvSPBanChay);
        progressBar = view.findViewById(R.id.progressbar);
        scrollHome = view.findViewById(R.id.scrollHome);
        db = FirebaseFirestore.getInstance();
        userAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressBar.setVisibility(View.VISIBLE);
        scrollHome.setVisibility(View.GONE);

//        Lấy thông tin người dùng
        if (userAuth.getCurrentUser() != null) {
            id = userAuth.getCurrentUser().getUid();
        } else {
            Toast.makeText(getContext(), "Không tìm thấy người dùng", Toast.LENGTH_LONG).show();
        }

//        database.getReference().child("NguoiDung").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                NguoiDung nguoiDung =snapshot.getValue(NguoiDung.class);
//                if (nguoiDung != null){
//                    name = nguoiDung.getName();
//                    tvName.setText(name);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        db.collection("user").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    NguoiDung nguoiDung = value.toObject(NguoiDung.class);
                    if (nguoiDung != null) {
                        tvName.setText(nguoiDung.getName());
//                        if (nguoiDung.getImage() != null && getActivity() != null) {
//                            Glide.with(getActivity()).load(nguoiDung.getImage()).into(imgUser);
//                        }

                    }
                }

            }
        });

        ArrayList<SlideModel> listSlideShow = new ArrayList<>();

        db.collection("SlideShow").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        listSlideShow.add(new SlideModel(documentSnapshot.getString("url"), ScaleTypes.FIT));
                        imgSlideShow.setImageList(listSlideShow, ScaleTypes.FIT);

                        progressBar.setVisibility(View.GONE);
                        scrollHome.setVisibility(View.VISIBLE);

                    }
                }
            }
        });


        //        Loại
        rvLoai.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
//        rvLoai.setFocusable(false);
        rvLoai.setNestedScrollingEnabled(false);
        listLoai = new ArrayList<>();
        loaiAdapter = new LoaiAdapter(listLoai, getActivity());
        rvLoai.setAdapter(loaiAdapter);

        db.collection("Loai")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Loai loai = document.toObject(Loai.class);
                                listLoai.add(loai);
                                loaiAdapter.notifyDataSetChanged();


                            }
                        } else {
                            Toast.makeText(getActivity(), "Lỗi: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

//        Sản phẩm bán chạy
        rvSPBanChay.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rvSPBanChay.setFocusable(false);
        rvSPBanChay.setNestedScrollingEnabled(false);
        listBanChay = new ArrayList<>();
        sanPhamBanChayAdapter = new SanPhamBanChayAdapter(listBanChay, getActivity());
        rvSPBanChay.setAdapter(sanPhamBanChayAdapter);

        db.collection("SanPham")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SanPham sanPham = document.toObject(SanPham.class);
                                listBanChay.add(sanPham);
                                sanPhamBanChayAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getContext(), "Lỗi: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        return view;
    }


}