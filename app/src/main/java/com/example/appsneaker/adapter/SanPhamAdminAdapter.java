package com.example.appsneaker.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appsneaker.Activity.CapNhatSanPhamActivity;
import com.example.appsneaker.R;
import com.example.appsneaker.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SanPhamAdminAdapter extends RecyclerView.Adapter<SanPhamAdminAdapter.ViewHolder> {

    Context context;
    ArrayList<SanPham> list;
    ProgressDialog progressDialog;
    FirebaseFirestore db;

    public SanPhamAdminAdapter(Context context, ArrayList<SanPham> list) {
        this.context = context;
        this.list = list;

        progressDialog = new ProgressDialog(context);
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_sanpham_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImgurl()).into(holder.imgSanPham);
        holder.tvName.setText(list.get(position).getName());
        holder.tvRate.setText(list.get(position).getRate());
        holder.tvPrice.setText("$" + list.get(position).getPrice());
        holder.tvType.setText(list.get(position).getType());

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CapNhatSanPhamActivity.class);
                intent.putExtra("sanpham", list.get(position));
                intent.putExtra("id", list.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = list.get(position).getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có chắc muốn xoá sản phẩm này")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("SanPham").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            list.remove(list.get(position));
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Đã xoá sản phẩm", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(context, "Xoá sản phẩm thất bại", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvRate, tvPrice, tvType;
        ImageView imgSanPham, imgEdit, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvType = itemView.findViewById(R.id.tvType);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }

    public void setList(ArrayList<SanPham> newList) {
        this.list = newList;
    }
}
