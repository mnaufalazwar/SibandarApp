package com.mnaufalazwar.sibandarapp.activity;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mnaufalazwar.sibandarapp.model.CustomerModel;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;

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
import java.util.Locale;

public class AddPurchaseViewModel extends ViewModel {

    private MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    void setTransactionCode(CustomerModel customerModel, ArrayList<SingleOrderItemModel> listOrder) {

        String subjectId = customerModel.getCompany();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        String sentTime = sdf.format(dt);

        String orders = "[";
        for(int i = 0 ; i < listOrder.size() ; i ++){
            orders += "{";
            orders += ("\"komoditas\":" + "\"" + listOrder.get(i).getCommodity() + "\",");
            orders += ("\"harga\":" + "\"" + listOrder.get(i).getPriceKg() + "\",");
            orders += ("\"kuantitas\":" + "\"" + listOrder.get(i).getAmountOrderKg() + "\"");
            if(i == (listOrder.size()-1)){
                orders += "}";
            }else {
                orders += "},";
            }
        }
        orders += "]";

        String jsonString = "{\"username\":\"testing01\"," +
                "\"cardType\":\"2\"," +
                "\"subjectId\":\"" + subjectId + "\"," +
                "\"sentTime\":\"" + sentTime + "\"," +
                "\"expectedPaymentDate\":\"" + sentTime + "\"," +
                "\"orders\":" + orders + "," +
                "\"paymentType\":\"kontan\"}";


        Log.d("JSON KIRIM :", jsonString);

        try {

            final JSONObject jsonObject = new JSONObject(jsonString);

            Log.d("JSON KIRIM::", jsonObject.toString());

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        URL url = new URL("http://192.168.100.78:8080/daily/postsellcard");
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

                        Log.i("RRRRESPONSEEEE" , response);

                        String transactionId = "tetot";
                        try{
                            JSONObject responseObject = new JSONObject(response);
                            JSONObject card = responseObject.getJSONObject("card");
                            transactionId = card.getString("_id");
                            Log.d("transactionId :::", transactionId);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        mutableLiveData.postValue(transactionId);

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

    LiveData<String> getTransactionCode() {
        return mutableLiveData;
    }
}
