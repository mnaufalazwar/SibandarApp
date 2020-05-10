package com.mnaufalazwar.sibandarapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.common.NumberToRupiah;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;

import java.util.ArrayList;

public class TransactionPTSAdapter extends RecyclerView.Adapter<TransactionPTSAdapter.TransactionPTSViewHolder> {

    private final ArrayList<SingleOrderItemModel> list = new ArrayList<>();
    private final Activity activity;

    private OnItemCheckCallback onItemCheckCallback;

    public void setOnItemCeckCallback(OnItemCheckCallback onItemCheckCallback){
        this.onItemCheckCallback = onItemCheckCallback;
    }

    public interface OnItemCheckCallback{
        void onItemChecked(SingleOrderItemModel data, int position, boolean isChecked);
    }

    public TransactionPTSAdapter (Activity activity){
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
    public TransactionPTSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_pts, parent, false);
        return new TransactionPTSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionPTSViewHolder holder, final int position) {

        holder.tvOrderCommodity.setText(list.get(position).getCommodity());
        holder.tvOrderQuantity.setText("" + list.get(position).getAmountOrderKg());
        holder.tvOrderPrice.setText(NumberToRupiah.convertNumberToRupiah(list.get(position).getPriceKg()));
        int totalPriceSingleItem = Integer.parseInt(list.get(position).getPriceKg()) * Integer.parseInt(list.get(position).getAmountOrderKg());
        holder.tvOrderTotalPrice.setText(NumberToRupiah.convertNumberToRupiah("" + totalPriceSingleItem));

        if(list.get(position).isReady()){
            holder.cbOrderReady.setChecked(true);
        }else{
            holder.cbOrderReady.setChecked(false);
        }

        holder.cbOrderReady.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                list.get(position).setReady(isChecked);
                onItemCheckCallback.onItemChecked(list.get(position), position, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class TransactionPTSViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderCommodity, tvOrderQuantity, tvOrderPrice, tvOrderTotalPrice;
        CheckBox cbOrderReady;

        public TransactionPTSViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderCommodity = itemView.findViewById(R.id.tv_order_comodity);
            tvOrderQuantity = itemView.findViewById(R.id.tv_order_quantity);
            tvOrderPrice = itemView.findViewById(R.id.tv_order_price);
            tvOrderTotalPrice = itemView.findViewById(R.id.tv_order_total_price);
            cbOrderReady = itemView.findViewById(R.id.cb_order_ready);
        }
    }
}
