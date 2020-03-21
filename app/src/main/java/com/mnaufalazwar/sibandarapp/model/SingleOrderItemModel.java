package com.mnaufalazwar.sibandarapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SingleOrderItemModel implements Parcelable {

    private String commodity;
    private String priceKg;
    private String priceTotal;
    private String amountOrderKg;
    private String amountSentKg;
    private String amountDeliverKg;
    private boolean ready;

    public SingleOrderItemModel() {
    }

    public SingleOrderItemModel(String commodity, String priceKg, String priceTotal, String amountOrderKg, String amountSentKg, String amountDeliverKg, boolean ready) {
        this.commodity = commodity;
        this.priceKg = priceKg;
        this.priceTotal = priceTotal;
        this.amountOrderKg = amountOrderKg;
        this.amountSentKg = amountSentKg;
        this.amountDeliverKg = amountDeliverKg;
        this.ready = ready;
    }

    protected SingleOrderItemModel(Parcel in) {
        commodity = in.readString();
        priceKg = in.readString();
        priceTotal = in.readString();
        amountOrderKg = in.readString();
        amountSentKg = in.readString();
        amountDeliverKg = in.readString();
        ready = in.readByte() != 0;
    }

    public static final Creator<SingleOrderItemModel> CREATOR = new Creator<SingleOrderItemModel>() {
        @Override
        public SingleOrderItemModel createFromParcel(Parcel in) {
            return new SingleOrderItemModel(in);
        }

        @Override
        public SingleOrderItemModel[] newArray(int size) {
            return new SingleOrderItemModel[size];
        }
    };

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getPriceKg() {
        return priceKg;
    }

    public void setPriceKg(String priceKg) {
        this.priceKg = priceKg;
    }

    public String getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(String priceTotal) {
        this.priceTotal = priceTotal;
    }

    public String getAmountOrderKg() {
        return amountOrderKg;
    }

    public void setAmountOrderKg(String amountOrderKg) {
        this.amountOrderKg = amountOrderKg;
    }

    public String getAmountSentKg() {
        return amountSentKg;
    }

    public void setAmountSentKg(String amountSentKg) {
        this.amountSentKg = amountSentKg;
    }

    public String getAmountDeliverKg() {
        return amountDeliverKg;
    }

    public void setAmountDeliverKg(String amountDeliverKg) {
        this.amountDeliverKg = amountDeliverKg;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commodity);
        dest.writeString(priceKg);
        dest.writeString(priceTotal);
        dest.writeString(amountOrderKg);
        dest.writeString(amountSentKg);
        dest.writeString(amountDeliverKg);
        dest.writeByte((byte) (ready ? 1 : 0));
    }

    public String getTotalOrderPrice(){

        int totalPriceSingleItem = Integer.parseInt(this.getPriceKg()) * Integer.parseInt(this.getAmountOrderKg());
        return ("" + totalPriceSingleItem);

    }
}
