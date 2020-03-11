package com.mnaufalazwar.sibandarapp.ui.sell;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mnaufalazwar.sibandarapp.model.DataTransactionModel;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SellViewModel extends ViewModel {

    private MutableLiveData<ArrayList<DataTransactionModel>> mutableLiveData = new MutableLiveData<>();

    void setDataTransactionModel() {

        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<DataTransactionModel> listItems = new ArrayList<>();
        String url = "http://192.168.100.78:8080/daily/getsellcards?username=testing01&cardType=1";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{

                    String result = new String(responseBody);
                    Log.d("RESPONSE GET", result);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray desiredCard = responseObject.getJSONArray("desiredCard");
                    Log.d("desiredCard", ""+desiredCard.length());

                    for(int i = 0 ; i < desiredCard.length() ; i ++){
                        JSONObject card = desiredCard.getJSONObject(i);

                        JSONArray orders = card.getJSONArray("orders");
                        ArrayList<SingleOrderItemModel> singleOrderItemModels = new ArrayList<>();
                        for (int j = 0 ; j < orders.length() ; j ++){
                            JSONObject order = orders.getJSONObject(j);

                            SingleOrderItemModel singleOrderItemModel = new SingleOrderItemModel();
                            singleOrderItemModel.setCommodity(order.getString("komoditas"));
                            singleOrderItemModel.setPriceKg(order.getString("harga"));
                            singleOrderItemModel.setAmountOrderKg(order.getString("kuantitas"));
                            if(order.getString("orderStatus").trim().equals("siap")){
                                singleOrderItemModel.setReady(true);
                            }

                            singleOrderItemModels.add(singleOrderItemModel);
                        }

                        DataTransactionModel dataTransactionModel = new DataTransactionModel();
                        dataTransactionModel.setSubject(card.getString("subjectId"));
                        dataTransactionModel.setListOrder(singleOrderItemModels);
                        dataTransactionModel.setTransactionCode(card.getString("_id"));
                        dataTransactionModel.setTransactionStatus(card.getString("paymentCode"));

                        listItems.add(dataTransactionModel);
                    }

                    mutableLiveData.postValue(listItems);
                }catch (Exception e){
                    Log.d("Exceptoin huhu ", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });

    }

    LiveData<ArrayList<DataTransactionModel>> getDataTransactionModel() {
        return mutableLiveData;
    }
}