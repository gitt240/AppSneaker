package com.example.appsneaker.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appsneaker.R;
import com.example.appsneaker.model.Address;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    ArrayList<Address> list;
    Context context;
    SelectAddress selectAddress;
    RadioButton radioAddressSelect;

    public AddressAdapter(ArrayList<Address> list, Context context, SelectAddress selectAddress) {
        this.list = list;
        this.context = context;
        this.selectAddress = selectAddress;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvAddress.setText(list.get(position).getAddress());
        holder.radioAdrres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Address address: list){
                    address.setAddressSelected(false);
                }
                list.get(position).setAddressSelected(true);
                if (radioAddressSelect!=null){
                    radioAddressSelect.setChecked(false);
                }
                radioAddressSelect = (RadioButton) v;
                radioAddressSelect.setChecked(true);
                selectAddress.setAddress(list.get(position).getAddress());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress;
        RadioButton radioAdrres;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress=itemView.findViewById(R.id.tvAddress);
            radioAdrres=itemView.findViewById(R.id.radioAddress);
        }
    }

    public interface SelectAddress{
       void setAddress (String address);
    }
}
