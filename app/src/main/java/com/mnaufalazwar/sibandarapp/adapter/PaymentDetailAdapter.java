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
import com.mnaufalazwar.sibandarapp.model.DataTransactionModel;
import com.mnaufalazwar.sibandarapp.model.PaymentModel;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;

import java.util.ArrayList;

public class PaymentDetailAdapter extends RecyclerView.Adapter<PaymentDetailAdapter.PaymentDetailViewHolder> {

    private final ArrayList<DataTransactionModel> list = new ArrayList<>();
    private final Activity activity;

    private OnItemCheckCallback onItemCheckCallback;

    public void setOnItemCeckCallback(OnItemCheckCallback onItemCheckCallback){
        this.onItemCheckCallback = onItemCheckCallback;
    }

    public interface OnItemCheckCallback{
        void onItemChecked(DataTransactionModel data, int position, boolean isChecked);
    }

    public PaymentDetailAdapter (Activity activity){
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

    public void addItem(DataTransactionModel customerModel) {
        this.list.add(customerModel);
        notifyItemInserted(list.size() - 1);
        notifyDataSetChanged();
    }

    public void updateItem(int position, DataTransactionModel customerModel) {
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
    public PaymentDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_detail, parent, false);
        return new PaymentDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentDetailViewHolder holder, final int position) {

        holder.tvTanggal.setText(list.get(position).getPaymentDate());

//        int totalPrice = 0;
//        for(int i = 0 ; i < list.get(position).getListOrder().size() ; i ++){
//
//            int priceSingeOrderItem = 0;
//            if(list.get(position).getTransactionStatus().equals("1")){
//                priceSingeOrderItem =
//                        Integer.parseInt(list.get(position).getListOrder().get(i).getPriceKg()) * Integer.parseInt(list.get(position).getListOrder().get(i).getAmountOrderKg());
//            }
//            else if(list.get(position).getTransactionStatus().equals("2")) {
//                priceSingeOrderItem =
//                        Integer.parseInt(list.get(position).getListOrder().get(i).getPriceKg()) * Integer.parseInt(list.get(position).getListOrder().get(i).getAmountSentKg());
//            }
//            else {
//                priceSingeOrderItem =
//                        Integer.parseInt(list.get(position).getListOrder().get(i).getPriceKg()) * Integer.parseInt(list.get(position).getListOrder().get(i).getAmountDeliverKg());
//            }
//
//            totalPrice += priceSingeOrderItem;
//        }

        holder.tvOrderNilai.setText("Rp." + list.get(position).getTotalTransactionNominal());

        holder.cbPaid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                list.get(position).setPaid(isChecked);
                onItemCheckCallback.onItemChecked(list.get(position), position, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaymentDetailViewHolder extends RecyclerView.ViewHolder {

        TextView tvTanggal, tvOrderNilai;
        CheckBox cbPaid;

        public PaymentDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvOrderNilai = itemView.findViewById(R.id.tv_order_nilai_transaction);
            cbPaid = itemView.findViewById(R.id.cb_paid);
        }
    }
}
