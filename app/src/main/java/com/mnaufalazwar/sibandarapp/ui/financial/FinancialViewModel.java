package com.mnaufalazwar.sibandarapp.ui.financial;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mnaufalazwar.sibandarapp.model.DataTransactionModel;
import com.mnaufalazwar.sibandarapp.model.PaymentModel;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FinancialViewModel extends ViewModel {

    private MutableLiveData<ArrayList<PaymentModel>> mutableLiveData;

    void setDataPaymentModel() {

        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<PaymentModel> listItems = new ArrayList<>();
        String url = "";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try{

                    String result = new String(responseBody);
                    Log.d("RESPONSE GET", result);
                    JSONObject responseObject = new JSONObject(result);


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

    public LiveData<ArrayList<PaymentModel>> getDataPaymentModel() {
        return mutableLiveData;
    }
}