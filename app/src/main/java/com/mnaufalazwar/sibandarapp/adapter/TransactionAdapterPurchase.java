package com.mnaufalazwar.sibandarapp.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.common.CommonEndpoint;
import com.mnaufalazwar.sibandarapp.common.NumberToRupiah;
import com.mnaufalazwar.sibandarapp.custom.CustomOnItemClickListener;
import com.mnaufalazwar.sibandarapp.model.DataTransactionModel;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TransactionAdapterPurchase extends RecyclerView.Adapter<TransactionAdapterPurchase.ViewHolder> {

    private final ArrayList<DataTransactionModel> list = new ArrayList<>();
    private final Activity activity;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(TransactionAdapterPurchase.OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback{
        void onItemClicked(DataTransactionModel data, int position);
    }

    public TransactionAdapterPurchase (Activity activity){
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_purchase, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

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
        holder.tvTotalPrice.setText(NumberToRupiah.convertNumberToRupiah(list.get(position).getTotalTransactionNominal()));

//        String status = "";
//        if(list.get(position).getTransactionStatus().equals("1")){
//            status = "disiapkan";
//        }else if(list.get(position).getTransactionStatus().equals("2")){
//            status = "dikirim";
//        }else{
//            status = "diterima";
//        }
//        holder.btnTransactionStatus.setText(status);
//        holder.btnTransactionStatus.setEnabled(false);

        holder.btnTransactionStatus.setText("rincian");
        holder.btnTransactionStatus.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Toast.makeText(activity, list.get(position).getTransactionCode() + " clicked", Toast.LENGTH_SHORT).show();
                onItemClickCallback.onItemClicked(list.get(position), position);
            }
        }));

        boolean allReady = true;
        for(int i = 0 ; i < list.get(position).getListOrder().size() ; i ++){
            if(!list.get(position).getListOrder().get(i).isReady()){
                allReady = false;
            }
        }
        if(allReady){
            holder.btnSendTransaction.setEnabled(true);
            holder.btnTransactionDelivered.setEnabled(true);
        }else{
            holder.btnSendTransaction.setEnabled(false);
            holder.btnTransactionDelivered.setEnabled(false);
        }

        if(list.get(position).getTransactionStatus().equals("kirim")){
            holder.btnSendTransaction.setVisibility(View.GONE);
            holder.btnTransactionDelivered.setVisibility(View.VISIBLE);
        }

        holder.btnSendTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "kirim", Toast.LENGTH_SHORT).show();
                holder.btnSendTransaction.setVisibility(View.GONE);
                holder.btnTransactionDelivered.setVisibility(View.VISIBLE);

                transactionSend(list.get(position));
            }
        });

        holder.btnTransactionDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "sampai", Toast.LENGTH_SHORT).show();
//                holder.btnSendTransaction.setVisibility(View.VISIBLE);
//                holder.btnTransactionDelivered.setVisibility(View.GONE);

                transactionDelivered(list.get(position));
//                list.remove(position);
//                setList(list);
                removeItem(position);
            }
        });

//        holder.itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
//            @Override
//            public void onItemClicked(View view, int position) {
//                Toast.makeText(activity, list.get(position).getTransactionCode() + " clicked", Toast.LENGTH_SHORT).show();
//                onItemClickCallback.onItemClicked(list.get(position), position);
//            }
//        }));

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSubject, tvTotalPrice;
        Button btnTransactionStatus, btnSendTransaction, btnTransactionDelivered;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSubject = itemView.findViewById(R.id.tv_subject);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
            btnSendTransaction = itemView.findViewById(R.id.btn_send_transaction);
            btnTransactionStatus = itemView.findViewById(R.id.btn_transaction_status);
            btnTransactionDelivered = itemView.findViewById(R.id.btn_transaction_delivered);

        }
    }

    private void transactionSend(DataTransactionModel dataTransactionModel){

        ArrayList<SingleOrderItemModel> listOrder = dataTransactionModel.getListOrder();

        String orders = "[";
        for(int i = 0 ; i < listOrder.size() ; i ++){

            String process;
            if(listOrder.get(i).isReady()){
                process = "siap";
            }else {
                process = "siap";
            }

            orders += "{";
            orders += ("\"komoditas\":" + "\"" + listOrder.get(i).getCommodity() + "\",");
            orders += ("\"harga\":" + "\"" + listOrder.get(i).getPriceKg() + "\",");
            orders += ("\"kuantitas\":" + "\"" + listOrder.get(i).getAmountOrderKg() + "\",");
            orders += ("\"orderStatus\":" + "\"" + process + "\"");
            if(i == (listOrder.size()-1)){
                orders += "}";
            }else {
                orders += "},";
            }
        }
        orders += "]";

        String jsonString = "{\"cardNumber\":\"" + dataTransactionModel.getTransactionCode() + "\"," +
                "\"status\":\"kirim\"," +
                "\"orders\":" + orders + "}";


        Log.d("JSON KIRIM CARD :", jsonString);

        try {

            final JSONObject jsonObject = new JSONObject(jsonString);

            Log.d("JSON KIRIM CARD::", jsonObject.toString());

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{

                        String endpoint = "http://" + CommonEndpoint.IP + ":" + CommonEndpoint.PORT + "/daily/updatesellcard";

//                        URL url = new URL("http://192.168.100.78:8080/daily/updatesellcard");

                        URL url = new URL(endpoint);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept","application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                        os.writeBytes(jsonObject.toString());

                        os.flush();
                        os.close();

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG" , conn.getResponseMessage());


                        BufferedReader br;

                        if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
                            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        } else {
                            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                        }

                        StringBuilder sb = new StringBuilder();
                        String output;
                        while ((output = br.readLine()) != null) {
                            sb.append(output);
                        }

                        String response = sb.toString();

                        Log.i("RRRRESPONSE UPDATE" , response);

                        conn.disconnect();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + jsonString + "\"");
        }

    }

    private void transactionDelivered(DataTransactionModel dataTransactionModel){

        ArrayList<SingleOrderItemModel> listOrder = dataTransactionModel.getListOrder();

        String orders = "[";
        for(int i = 0 ; i < listOrder.size() ; i ++){

            String process;
            if(listOrder.get(i).isReady()){
                process = "siap";
            }else {
                process = "siap";
            }

            orders += "{";
            orders += ("\"komoditas\":" + "\"" + listOrder.get(i).getCommodity() + "\",");
            orders += ("\"harga\":" + "\"" + listOrder.get(i).getPriceKg() + "\",");
            orders += ("\"kuantitas\":" + "\"" + listOrder.get(i).getAmountOrderKg() + "\",");
            orders += ("\"orderStatus\":" + "\"" + process + "\"");
            if(i == (listOrder.size()-1)){
                orders += "}";
            }else {
                orders += "},";
            }
        }
        orders += "]";

        String jsonString = "{\"cardNumber\":\"" + dataTransactionModel.getTransactionCode() + "\"," +
                "\"status\":\"sampai\"," +
                "\"orders\":" + orders + "}";


        Log.d("JSON SAMPAI :", jsonString);

        try {

            final JSONObject jsonObject = new JSONObject(jsonString);

            Log.d("JSON SAMPAI::", jsonObject.toString());

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        URL url = new URL("http://192.168.100.78:8080/daily/updatesellcard");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept","application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                        os.writeBytes(jsonObject.toString());

                        os.flush();
                        os.close();

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG" , conn.getResponseMessage());


                        BufferedReader br;

                        if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
                            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        } else {
                            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                        }

                        StringBuilder sb = new StringBuilder();
                        String output;
                        while ((output = br.readLine()) != null) {
                            sb.append(output);
                        }

                        String response = sb.toString();

                        Log.i("RRRRESPONSE UPDATE" , response);

                        conn.disconnect();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + jsonString + "\"");
        }

    }
}
