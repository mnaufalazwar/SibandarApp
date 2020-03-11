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

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private final ArrayList<CustomerModel> list = new ArrayList<>();
    private final Activity activity;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback{
        void onItemClicked(CustomerModel data, int position);
    }

    public CustomerAdapter (Activity activity){
        this.activity = activity;
    }

    public ArrayList<CustomerModel> getList(){
        return list;
    }

    public void setList(ArrayList<CustomerModel> list){

        if(list.size() > 0){
            this.list.clear();
        }

        this.list.addAll(list);

        notifyDataSetChanged();
    }

    public void addItem(CustomerModel customerModel) {
        this.list.add(customerModel);
        notifyItemInserted(list.size() - 1);
        notifyDataSetChanged();
    }

    public void updateItem(int position, CustomerModel customerModel) {
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
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {

        holder.tvCustomerName.setText(list.get(position).getContactPerson());
        holder.tvCustomerCompany.setText(list.get(position).getCompany());
        holder.tvCustomerAddres.setText(list.get(position).getDeliveryAddress());

        holder.itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                onItemClickCallback.onItemClicked(list.get(position), position);
//                Toast.makeText(activity, list.get(position).getContactPerson() + " clicked", Toast.LENGTH_SHORT).show();

            }
        }));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {

        final TextView tvCustomerName, tvCustomerAddres, tvCustomerCompany;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvCustomerAddres = itemView.findViewById(R.id.tv_customer_address);
            tvCustomerCompany = itemView.findViewById(R.id.tv_customer_company);

        }
    }
}
