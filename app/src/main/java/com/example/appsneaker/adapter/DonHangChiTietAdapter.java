package com.example.appsneaker.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appsneaker.R;
import com.example.appsneaker.model.DonHang;
import com.example.appsneaker.model.GioHang;

import java.util.ArrayList;

public class DonHangChiTietAdapter extends RecyclerView.Adapter<DonHangChiTietAdapter.ViewHolder> {

    Context context;
    ArrayList<GioHang> list;

    public DonHangChiTietAdapter(Context context, ArrayList<GioHang> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_xac_nhan_dat_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(list.get(position).getName());
        holder.tvPrice.setText("Giá: $" + list.get(position).getPrice());
        holder.tvQuantity.setText("SL: " + list.get(position).getQuantity());

        Glide.with(context).load(list.get(position).getImgurl()).into(holder.imgSanPham);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSanPham;
        TextView tvName, tvPrice, tvQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }

    public void setList(ArrayList<GioHang> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
