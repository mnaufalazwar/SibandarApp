package com.mnaufalazwar.sibandarapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PaymentModel implements Parcelable {

    private String subject;
    private String totalTransaction;
    private ArrayList<DataTransactionModel> dataTransactionModels;

    public PaymentModel() {
    }

    public PaymentModel(String subject, String totalTransaction, ArrayList<DataTransactionModel> dataTransactionModels) {
        this.subject = subject;
        this.totalTransaction = totalTransaction;
        this.dataTransactionModels = dataTransactionModels;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTotalTransaction() {
        return totalTransaction;
    }

    public void setTotalTransaction(String totalTransaction) {
        this.totalTransaction = totalTransaction;
    }

    public ArrayList<DataTransactionModel> getDataTransactionModels() {
        return dataTransactionModels;
    }

    public void setDataTransactionModels(ArrayList<DataTransactionModel> dataTransactionModels) {
        this.dataTransactionModels = dataTransactionModels;
    }

    protected PaymentModel(Parcel in) {
        subject = in.readString();
        totalTransaction = in.readString();
        dataTransactionModels = in.createTypedArrayList(DataTransactionModel.CREATOR);
    }

    public static final Creator<PaymentModel> CREATOR = new Creator<PaymentModel>() {
        @Override
        public PaymentModel createFromParcel(Parcel in) {
            return new PaymentModel(in);
        }

        @Override
        public PaymentModel[] newArray(int size) {
            return new PaymentModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeString(totalTransaction);
        dest.writeTypedList(dataTransactionModels);
    }
}
