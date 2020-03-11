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
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;

import java.util.ArrayList;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>{

    private final ArrayList<SingleOrderItemModel> list = new ArrayList<>();
    private final Activity activity;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback{
        void onItemClicked(SingleOrderItemModel data, int position);
    }

    public OrderItemAdapter (Activity activity){
        this.activity = activity;
    }

    public ArrayList<SingleOrderItemModel> getList(){
        return this.list;
    }

    public void setList(ArrayList<SingleOrderItemModel> list){

        if(list.size() > 0){
            this.list.clear();
        }

        this.list.addAll(list);

        notifyDataSetChanged();
    }

    public void addItem(SingleOrderItemModel singleOrderItemModel) {
        this.list.add(singleOrderItemModel);
        notifyItemInserted(list.size() - 1);
        notifyDataSetChanged();
    }

    public void updateItem(int position, SingleOrderItemModel singleOrderItemModel) {
        this.list.set(position, singleOrderItemModel);
        notifyItemChanged(position, singleOrderItemModel);
    }

    public void removeItem(int position) {
        this.list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer_order, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.OrderItemViewHolder holder, int position) {

        holder.tvOrderCommodity.setText(list.get(position).getCommodity());
        holder.tvOrderQuantity.setText("" + list.get(position).getAmountOrderKg());
        holder.tvOrderPrice.setText(list.get(position).getPriceKg());
        int totalPriceSingleItem = Integer.parseInt(list.get(position).getPriceKg()) * Integer.parseInt(list.get(position).getAmountOrderKg());
        holder.tvOrderTotalPrice.setText("" + totalPriceSingleItem);

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
        return this.list.size();
    }

    public class OrderItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderCommodity, tvOrderQuantity, tvOrderPrice, tvOrderTotalPrice;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderCommodity = itemView.findViewById(R.id.tv_order_comodity);
            tvOrderQuantity = itemView.findViewById(R.id.tv_order_quantity);
            tvOrderPrice = itemView.findViewById(R.id.tv_order_price);
            tvOrderTotalPrice = itemView.findViewById(R.id.tv_order_total_price);
        }
    }
}
