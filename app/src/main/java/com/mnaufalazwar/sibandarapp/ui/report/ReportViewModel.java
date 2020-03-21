package com.mnaufalazwar.sibandarapp.ui.report;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mnaufalazwar.sibandarapp.model.DataTransactionModel;
import com.mnaufalazwar.sibandarapp.model.PaymentModel;
import com.mnaufalazwar.sibandarapp.model.ReportCardModel;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ReportViewModel extends ViewModel {

    private MutableLiveData<String> mUtang = new MutableLiveData<>();
    private MutableLiveData<String> mPiutang = new MutableLiveData<>();
    private MutableLiveData<String> mKeluar = new MutableLiveData<>();
    private MutableLiveData<String> mMasuk = new MutableLiveData<>();
    private MutableLiveData<String> mSaldo = new MutableLiveData<>();
    private MutableLiveData<ArrayList<ReportCardModel>> mutableLiveData = new MutableLiveData<>();

    void setData() {

        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<ReportCardModel> listItems = new ArrayList<>();

        String url = "http://192.168.100.78:8080/payments/getfinance?username=testing01";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try{

                    String result = new String(responseBody);
                    Log.d("RESPONSE GET REPORT", result);
                    JSONObject responseObject = new JSONObject(result);
                    JSONObject finance = responseObject.getJSONObject("finance");

                    JSONArray cashflow = finance.getJSONArray("cashflow");
                    for(int i = 0 ; i < cashflow.length() ; i ++){

                        JSONObject cashflowObj = cashflow.getJSONObject(i);

                        ReportCardModel model = new ReportCardModel();
                        model.setSubject(cashflowObj.getString("subjectId"));
                        model.setNominal(cashflowObj.getString("paid"));
                        model.setPaidId(cashflowObj.getString("_id"));
                        model.setTime(cashflowObj.getString("createdAt"));

                        JSONArray cards = cashflowObj.getJSONArray("cards");
                        String[] arr = new String[cards.length()];

                        ArrayList<String> cardList = new ArrayList<>();
                        for(int j = 0 ; j < cards.length() ; j ++){
                            cardList.add(cards.getString(j));
                        }

                        model.setCards(cardList);

                        listItems.add(model);
                    }

                    mutableLiveData.postValue(listItems);

                    mUtang.postValue("" + finance.getInt("utang"));

                    mPiutang.postValue("" + finance.getInt("piutang"));

                    mKeluar.postValue("" + finance.getInt("totalKeluar"));

                    mMasuk.postValue("" + finance.getInt("totalMasuk"));

                    int masuk = finance.getInt("totalMasuk");
                    int keluar = finance.getInt("totalKeluar");
                    int saldo = masuk + keluar;
                    mSaldo.postValue("" + saldo);

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

    public LiveData<ArrayList<ReportCardModel>> getDataCashflow() {
        return mutableLiveData;
    }

    public LiveData<String> getDataUtang() {
        return mUtang;
    }

    public LiveData<String> getDataPiutang() {
        return mPiutang;
    }

    public LiveData<String> getDataKeluar() {
        return mKeluar;
    }

    public LiveData<String> getDataMasuk() {
        return mMasuk;
    }

    public LiveData<String> getDataSaldo() {
        return mSaldo;
    }
}