package com.mnaufalazwar.sibandarapp.service;

import android.util.Log;

import com.mnaufalazwar.sibandarapp.model.DataTransactionModel;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseService {

    // Construct JSON

    public String constructJsonUpdateSellCard(ArrayList<SingleOrderItemModel> listOrder, String cardNumber){

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

        return jsonString;
    }

    // Parse JSON

    public ArrayList<DataTransactionModel> parseResponseGetSellCardsToList(String responseBody){

        final ArrayList<DataTransactionModel> listItems = new ArrayList<>();

        try{

            JSONObject responseObject = new JSONObject(responseBody);
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

        }catch (Exception e){
            Log.d("Exceptoin huhu ", e.getMessage());
        }

        return listItems;
    }

}
