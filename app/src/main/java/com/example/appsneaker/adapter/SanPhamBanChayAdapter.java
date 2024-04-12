package com.example.appsneaker.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appsneaker.Activity.SanPhamChiTietActivity;
import com.example.appsneaker.R;
import com.example.appsneaker.model.SanPham;
import com.example.appsneaker.model.SanPhamBanChay;

import java.util.ArrayList;

public class SanPhamBanChayAdapter extends RecyclerView.Adapter<SanPhamBanChayAdapter.ViewHolder> {

    ArrayList<SanPham> list;
    Context context;

    public SanPhamBanChayAdapter(ArrayList<SanPham> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_sanpham,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(list.get(position).getName());
        holder.tvRate.setText(list.get(position).getRate());
        holder.tvType.setText(list.get(position).getType());
        holder.tvPrice.setText("$"+ list.get(position).getPrice());
        Glide.with(context).load(list.get(position).getImgurl()).into(holder.imgBanChay);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, SanPhamChiTietActivity.class);
                intent.putExtra("chitiet", list.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(5, list.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPrice, tvRate, tvType;
        ImageView imgBanChay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvType = itemView.findViewById(R.id.tvType);
            imgBanChay = itemView.findViewById(R.id.imgSanPham);
        }
    }
}
