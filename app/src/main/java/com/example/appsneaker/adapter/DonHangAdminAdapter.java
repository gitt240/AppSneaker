package com.example.appsneaker.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appsneaker.R;
import com.example.appsneaker.model.DonHang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DonHangAdminAdapter extends RecyclerView.Adapter<DonHangAdminAdapter.ViewHolder> {

    ArrayList<DonHang> list;
    Context context;
    FirebaseFirestore db;



    public DonHangAdminAdapter(ArrayList<DonHang> list, Context context) {
        this.list = list;
        this.context = context;

        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_donhang_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvId.setText(list.get(position).getId());
        holder.tvEmail.setText(list.get(position).getEmail());
        holder.tvDate.setText(list.get(position).getDate());
        holder.tvStatus.setText(list.get(position).getStatus());

        String id = list.get(position).getId();

        holder.btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateConfirmOrder(id, holder);
            }
        });
        holder.btnPayed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePayed(id, holder);
            }
        });
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upDateCancel(id, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvEmail, tvDate, tvStatus;
        Button btnConfirmOrder, btnPayed, btnCancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvDate = itemView.findViewById(R.id.tvDate);

            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnConfirmOrder = itemView.findViewById(R.id.btnConfirmOrder);
            btnPayed = itemView.findViewById(R.id.btnPayed);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }

    public void updateConfirmOrder(String id,ViewHolder holder){
        String confirm = "Đã xác nhận";
        db.collectionGroup("DonHang").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot :task.getResult()){
                        if (documentSnapshot.getId().equals(id)){
                            documentSnapshot.getReference().update("status", confirm).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context,"Cập nhật trạng thái thành công", Toast.LENGTH_LONG).show();

                                    holder.btnConfirmOrder.setEnabled(false);
                                    holder.btnConfirmOrder.setAlpha(0.4f);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public void updatePayed(String id, ViewHolder holder){
        String payed = "Đã thanh toán";
        db.collectionGroup("DonHang").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        if (documentSnapshot.getId().equals(id)){
                            documentSnapshot.getReference().update("status", payed).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context,"Cập nhật trạng thái thành công", Toast.LENGTH_LONG).show();

                                    holder.btnPayed.setEnabled(false);
                                    holder.btnPayed.setAlpha(0.4f);

                                    holder.btnCancel.setEnabled(false);
                                    holder.btnCancel.setAlpha(0.4f);

                                    holder.btnConfirmOrder.setEnabled(false);
                                    holder.btnConfirmOrder.setAlpha(0.4f);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public void upDateCancel (String id, ViewHolder holder){
        String cancel = "Đơn hàng đã bị huỷ";
        db.collectionGroup("DonHang").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot :task.getResult()){
                        if (documentSnapshot.getId().equals(id)){
                            documentSnapshot.getReference().update("status",cancel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context,"Cập nhật trạng thái thành công", Toast.LENGTH_LONG).show();

                                    holder.btnCancel.setEnabled(false);
                                    holder.btnCancel.setAlpha(0.4f);

                                    holder.btnPayed.setEnabled(false);
                                    holder.btnPayed.setAlpha(0.4f);

                                    holder.btnConfirmOrder.setEnabled(false);
                                    holder.btnConfirmOrder.setAlpha(0.4f);
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}
