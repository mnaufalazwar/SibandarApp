package com.mnaufalazwar.sibandarapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.adapter.OrderItemAdapter;
import com.mnaufalazwar.sibandarapp.adapter.TransactionPTSAdapter;
import com.mnaufalazwar.sibandarapp.model.CustomerModel;
import com.mnaufalazwar.sibandarapp.model.DataTransactionModel;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;
import com.mnaufalazwar.sibandarapp.ui.dialog.AddCustomerDialog;
import com.mnaufalazwar.sibandarapp.ui.dialog.AddOrderItemDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionPrepareToSendActivity extends AppCompatActivity {

    public static final int RESULT_TRANSACTION_PTS = 201;
    public static final int REQUEST_TRANSACTION_PTS = 200;
    public static final String EXTRA_DATA_TRANSACTION_IN = "EXTRA_DATA_TRANSACTION_IN";
    public static final String EXTRA_DATA_TRANSACTION_IN_POS = "EXTRA_DATA_TRANSACTION_IN_POS";
    public static final String EXTRA_DATA_TRANSACTION_OUT = "EXTRA_DATA_TRANSACTION_OUT";
    public static final String EXTRA_DATA_TRANSACTION_POS = "EXTRA_DATA_TRANSACTION_POS";

    RecyclerView recyclerView;
    TransactionPTSAdapter adapter;

    Button btnSaveOrder;
    TextView tvTotalPriceOrder, tvNoOrder;

    String cardNumber = "";
    ArrayList<SingleOrderItemModel> dataUpdate = new ArrayList<>();
    ArrayList<SingleOrderItemModel> singleOrderItemModels = new ArrayList<>();
    DataTransactionModel thisDataTransactionModel = new DataTransactionModel();
    int pos = -1;
    CustomerModel thisCustomerModel = new CustomerModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_prepare_to_send);

        btnSaveOrder = findViewById(R.id.btn_save_order);
        tvTotalPriceOrder = findViewById(R.id.tv_total_price_transaction);
        tvNoOrder = findViewById(R.id.tvNoOrder);

        if(getIntent().getParcelableExtra(EXTRA_DATA_TRANSACTION_IN) != null){
            thisDataTransactionModel = getIntent().getParcelableExtra(EXTRA_DATA_TRANSACTION_IN);
            singleOrderItemModels = thisDataTransactionModel.getListOrder();
            pos = getIntent().getIntExtra(EXTRA_DATA_TRANSACTION_IN_POS, -1);
            cardNumber = thisDataTransactionModel.getTransactionCode();
        }

        int total = 0 ;
        for(int i = 0 ; i < singleOrderItemModels.size() ; i ++){
            total += Integer.parseInt(singleOrderItemModels.get(i).getPriceKg().trim()) * Integer.parseInt(singleOrderItemModels.get(i).getAmountOrderKg().trim());
        }
        tvTotalPriceOrder.setText("Total transaksi : Rp" + total);

        recyclerView = findViewById(R.id.rvOrderItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(TransactionPrepareToSendActivity.this));
        recyclerView.setHasFixedSize(true);

        adapter = new TransactionPTSAdapter(TransactionPrepareToSendActivity.this);
        singleOrderItemModels = thisDataTransactionModel.getListOrder();
        adapter.setList(singleOrderItemModels);
        adapter.setOnItemCeckCallback(new TransactionPTSAdapter.OnItemCheckCallback() {
            @Override
            public void onItemChecked(SingleOrderItemModel data, int position, boolean isChecked) {
                dataUpdate.add(data);
                singleOrderItemModels.set(position, data);
                thisDataTransactionModel.setListOrder(singleOrderItemModels);
            }


        });

        recyclerView.setAdapter(adapter);

        btnSaveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                sendUpdatedData(dataUpdate);
                sendUpdatedData(singleOrderItemModels);

                Intent intent = new Intent();
                intent.putExtra(EXTRA_DATA_TRANSACTION_OUT, thisDataTransactionModel);
                intent.putExtra(EXTRA_DATA_TRANSACTION_POS, pos);
                setResult(RESULT_TRANSACTION_PTS, intent);
                finish();
            }
        });
    }

    private void sendUpdatedData(ArrayList<SingleOrderItemModel> listOrder){

        String orders = "[";
        for(int i = 0 ; i < listOrder.size() ; i ++){

            String process;
            if(listOrder.get(i).isReady()){
                process = "siap";
            }else {
                process = "belum";
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

        String jsonString = "{\"cardNumber\":\"" + cardNumber + "\"," +
                "\"status\":\"check\"," +
                "\"orders\":" + orders + "}";


        Log.d("JSON KIRIM UPDATE :", jsonString);

        try {

            final JSONObject jsonObject = new JSONObject(jsonString);

            Log.d("JSON KIRIM::", jsonObject.toString());

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
