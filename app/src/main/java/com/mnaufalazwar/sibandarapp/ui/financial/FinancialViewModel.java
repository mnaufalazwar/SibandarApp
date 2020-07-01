package com.mnaufalazwar.sibandarapp.ui.financial;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mnaufalazwar.sibandarapp.common.CommonEndpoint;
import com.mnaufalazwar.sibandarapp.model.DataTransactionModel;
import com.mnaufalazwar.sibandarapp.model.PaymentModel;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FinancialViewModel extends ViewModel {

    private MutableLiveData<ArrayList<PaymentModel>> mutableLiveData = new MutableLiveData<>();

    private MutableLiveData<ArrayList<PaymentModel>> mutableLiveDataPurchase = new MutableLiveData<>();


    //Sell

    void setDataPaymentModel() {

        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<PaymentModel> listItems = new ArrayList<>();

        String url = "http://" + CommonEndpoint.IP + ":" + CommonEndpoint.PORT + "/payments/getprepayments?username=testing01&cardType=1";
//        String url = "http://192.168.100.78:8080/payments/getprepayments?username=testing01&cardType=1";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try{

                    String result = new String(responseBody);
                    Log.d("RESPONSE GET PAYMENT", result);
                    JSONObject responseObject = new JSONObject(result);

                    JSONArray listCustDetail = responseObject.getJSONArray("listCustDetail");
                    Log.d("GET PAYMENT SIZE", "" + listCustDetail.length());
                    for(int i = 0 ; i < listCustDetail.length() ; i ++){

                        JSONObject customerTransasction = listCustDetail.getJSONObject(i);

                        PaymentModel model = new PaymentModel();
                        model.setSubject(customerTransasction.getString("subjectId"));

                        JSONArray cards = customerTransasction.getJSONArray("cards");
                        ArrayList<DataTransactionModel> dataTransactionModels = new ArrayList<>();
                        for(int j = 0 ; j < cards.length() ; j ++){

                            JSONObject card = cards.getJSONObject(j);

                            DataTransactionModel dataTransactionModel = new DataTransactionModel();
                            dataTransactionModel.setTransactionCode(card.getString("_id"));
                            dataTransactionModel.setTransactionStatus(card.getString("paymentCode"));
                            dataTransactionModel.setPaid(false);
                            dataTransactionModel.setSubject(card.getString("subjectId"));
                            dataTransactionModel.setPaymentDate(card.getString("expectedPaymentDate"));
                            dataTransactionModel.setPaymentType(card.getString("paymentType"));
                            dataTransactionModel.setExpectedDeliveryDate(card.getString("sentTime"));

                            JSONArray orders = card.getJSONArray("orders");
                            ArrayList<SingleOrderItemModel> singleOrderItemModels = new ArrayList<>();
                            for(int k = 0 ; k < orders.length() ; k ++){

                                JSONObject orderItem = orders.getJSONObject(k);

                                SingleOrderItemModel singleOrderItemModel = new SingleOrderItemModel();
                                singleOrderItemModel.setCommodity(orderItem.getString("komoditas"));
                                singleOrderItemModel.setPriceKg(orderItem.getString("harga"));
                                singleOrderItemModel.setAmountOrderKg(orderItem.getString("kuantitas"));
                                singleOrderItemModel.setReady(true);

                                singleOrderItemModels.add(singleOrderItemModel);
                            }
                            dataTransactionModel.setListOrder(singleOrderItemModels);
                            dataTransactionModels.add(dataTransactionModel);
                        }

                        model.setDataTransactionModels(dataTransactionModels);

                        listItems.add(model);
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

    public LiveData<ArrayList<PaymentModel>> getDataPaymentModel() {
        return mutableLiveData;
    }


    //Purchase

    void setDataPaymentModelPurchase() {

        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<PaymentModel> listItems = new ArrayList<>();

        String url = "http://" + CommonEndpoint.IP + ":" + CommonEndpoint.PORT + "/payments/getprepayments?username=testing01&cardType=2";
//        String url = "http://192.168.100.78:8080/payments/getprepayments?username=testing01&cardType=2";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try{

                    String result = new String(responseBody);
                    Log.d("RESPONSE GET PAYMENT", result);
                    JSONObject responseObject = new JSONObject(result);

                    JSONArray listCustDetail = responseObject.getJSONArray("listCustDetail");
                    Log.d("GET PAYMENT SIZE", "" + listCustDetail.length());
                    for(int i = 0 ; i < listCustDetail.length() ; i ++){

                        JSONObject customerTransasction = listCustDetail.getJSONObject(i);

                        PaymentModel model = new PaymentModel();
                        model.setSubject(customerTransasction.getString("subjectId"));

                        JSONArray cards = customerTransasction.getJSONArray("cards");
                        ArrayList<DataTransactionModel> dataTransactionModels = new ArrayList<>();
                        for(int j = 0 ; j < cards.length() ; j ++){

                            JSONObject card = cards.getJSONObject(j);

                            DataTransactionModel dataTransactionModel = new DataTransactionModel();
                            dataTransactionModel.setTransactionCode(card.getString("_id"));
                            dataTransactionModel.setTransactionStatus(card.getString("paymentCode"));
                            dataTransactionModel.setPaid(false);
                            dataTransactionModel.setSubject(card.getString("subjectId"));
                            dataTransactionModel.setPaymentDate(card.getString("expectedPaymentDate"));
                            dataTransactionModel.setPaymentType(card.getString("paymentType"));
                            dataTransactionModel.setExpectedDeliveryDate(card.getString("sentTime"));

                            JSONArray orders = card.getJSONArray("orders");
                            ArrayList<SingleOrderItemModel> singleOrderItemModels = new ArrayList<>();
                            for(int k = 0 ; k < orders.length() ; k ++){

                                JSONObject orderItem = orders.getJSONObject(k);

                                SingleOrderItemModel singleOrderItemModel = new SingleOrderItemModel();
                                singleOrderItemModel.setCommodity(orderItem.getString("komoditas"));
                                singleOrderItemModel.setPriceKg(orderItem.getString("harga"));
                                singleOrderItemModel.setAmountOrderKg(orderItem.getString("kuantitas"));
                                singleOrderItemModel.setReady(true);

                                singleOrderItemModels.add(singleOrderItemModel);
                            }
                            dataTransactionModel.setListOrder(singleOrderItemModels);
                            dataTransactionModels.add(dataTransactionModel);
                        }

                        model.setDataTransactionModels(dataTransactionModels);

                        listItems.add(model);
                    }

                    mutableLiveDataPurchase.postValue(listItems);
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

    public LiveData<ArrayList<PaymentModel>> getDataPaymentModelPurchase() {
        return mutableLiveDataPurchase;
    }
}