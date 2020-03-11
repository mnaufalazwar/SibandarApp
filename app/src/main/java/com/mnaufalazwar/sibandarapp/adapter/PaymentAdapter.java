package com.mnaufalazwar.sibandarapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.custom.CustomOnItemClickListener;
import com.mnaufalazwar.sibandarapp.model.CustomerModel;
import com.mnaufalazwar.sibandarapp.model.PaymentModel;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>{

    private final ArrayList<PaymentModel> list = new ArrayList<>();
    private final Activity activity;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback{
        void onItemClicked(PaymentModel data, int position);
    }

    public PaymentAdapter (Activity activity){
        this.activity = activity;
    }

    public ArrayList<PaymentModel> getList(){
        return list;
    }

    public void setList(ArrayList<PaymentModel> list){

        if(list.size() > 0){
            this.list.clear();
        }

        this.list.addAll(list);

        notifyDataSetChanged();
    }

    public void addItem(PaymentModel customerModel) {
        this.list.add(customerModel);
        notifyItemInserted(list.size() - 1);
        notifyDataSetChanged();
    }

    public void updateItem(int position, PaymentModel customerModel) {
        this.list.set(position, customerModel);
        notifyItemChanged(position, customerModel);
    }

    public void removeItem(int position) {
        this.list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {

        holder.tvCustomerCompany.setText(list.get(position).getSubject());
        holder.tvTotalTransaction.setText(list.get(position).getTotalTransaction());

        holder.itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                onItemClickCallback.onItemClicked(list.get(position), position);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {

        TextView tvCustomerCompany, tvTotalTransaction;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCustomerCompany = itemView.findViewById(R.id.tv_customer_company);
            tvTotalTransaction = itemView.findViewById(R.id.tv_order_total_transaction);
        }
    }
}
