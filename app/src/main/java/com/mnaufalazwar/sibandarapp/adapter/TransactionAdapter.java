package com.mnaufalazwar.sibandarapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.custom.CustomOnItemClickListener;
import com.mnaufalazwar.sibandarapp.model.CustomerModel;
import com.mnaufalazwar.sibandarapp.model.DataTransactionModel;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final ArrayList<DataTransactionModel> list = new ArrayList<>();
    private final Activity activity;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(TransactionAdapter.OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback{
        void onItemClicked(DataTransactionModel data, int position);
    }

    public TransactionAdapter (Activity activity){
        this.activity = activity;
    }

    public ArrayList<DataTransactionModel> getList(){
        return list;
    }

    public void setList(ArrayList<DataTransactionModel> list){

        if(list.size() > 0){
            this.list.clear();
        }

        this.list.addAll(list);

        notifyDataSetChanged();
    }

    public void addItem(DataTransactionModel dataTransactionModel) {
        this.list.add(dataTransactionModel);
        notifyItemInserted(list.size() - 1);
        notifyDataSetChanged();
    }

    public void updateItem(int position, DataTransactionModel dataTransactionModel) {
        this.list.set(position, dataTransactionModel);
        notifyItemChanged(position, dataTransactionModel);
    }

    public void removeItem(int position) {
        this.list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    @NonNull
    @Override
    public TransactionAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TransactionAdapter.TransactionViewHolder holder, int position) {

        holder.tvSubject.setText(list.get(position).getSubject());

        int totalPrice = 0;
        for(int i = 0 ; i < list.get(position).getListOrder().size() ; i ++){

            int priceSingeOrderItem = 0;
            if(list.get(position).getTransactionStatus().equals("order")){
                priceSingeOrderItem =
                        Integer.parseInt(list.get(position).getListOrder().get(i).getPriceKg()) * Integer.parseInt(list.get(position).getListOrder().get(i).getAmountOrderKg());
            }
            else if(list.get(position).getTransactionStatus().equals("siap")) {
//                priceSingeOrderItem =
//                        Integer.parseInt(list.get(position).getListOrder().get(i).getPriceKg()) * Integer.parseInt(list.get(position).getListOrder().get(i).getAmountSentKg());

                priceSingeOrderItem =
                        Integer.parseInt(list.get(position).getListOrder().get(i).getPriceKg()) * Integer.parseInt(list.get(position).getListOrder().get(i).getAmountOrderKg());
            }
            else {
//                priceSingeOrderItem =
//                        Integer.parseInt(list.get(position).getListOrder().get(i).getPriceKg()) * Integer.parseInt(list.get(position).getListOrder().get(i).getAmountDeliverKg());

                priceSingeOrderItem =
                        Integer.parseInt(list.get(position).getListOrder().get(i).getPriceKg()) * Integer.parseInt(list.get(position).getListOrder().get(i).getAmountOrderKg());
            }

            totalPrice += priceSingeOrderItem;
        }
        holder.tvTotalPrice.setText("" + totalPrice);

        String status = "";
        if(list.get(position).getTransactionStatus().equals("1")){
            status = "disiapkan";
        }else if(list.get(position).getTransactionStatus().equals("2")){
            status = "dikirim";
        }else{
            status = "diterima";
        }
        holder.btnTransactionStatus.setText(status);
        holder.btnTransactionStatus.setEnabled(false);

        boolean allReady = true;
        for(int i = 0 ; i < list.get(position).getListOrder().size() ; i ++){
            if(!list.get(position).getListOrder().get(i).isReady()){
                allReady = false;
            }
        }
        if(allReady){
            holder.btnSendTransaction.setEnabled(true);
        }else{
            holder.btnSendTransaction.setEnabled(false);
        }

        holder.btnSendTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "kirim", Toast.LENGTH_SHORT).show();
                holder.btnSendTransaction.setVisibility(View.GONE);
                holder.btnTransactionDelivered.setVisibility(View.VISIBLE);
            }
        });

        holder.btnTransactionDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "sampai", Toast.LENGTH_SHORT).show();
                holder.btnSendTransaction.setVisibility(View.VISIBLE);
                holder.btnTransactionDelivered.setVisibility(View.GONE);
            }
        });

        holder.itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Toast.makeText(activity, list.get(position).getTransactionCode() + " clicked", Toast.LENGTH_SHORT).show();
                onItemClickCallback.onItemClicked(list.get(position), position);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView tvSubject, tvTotalPrice;
        Button btnTransactionStatus, btnSendTransaction, btnTransactionDelivered;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSubject = itemView.findViewById(R.id.tv_subject);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
            btnSendTransaction = itemView.findViewById(R.id.btn_send_transaction);
            btnTransactionStatus = itemView.findViewById(R.id.btn_transaction_status);
            btnTransactionDelivered = itemView.findViewById(R.id.btn_transaction_delivered);
        }
    }
}
