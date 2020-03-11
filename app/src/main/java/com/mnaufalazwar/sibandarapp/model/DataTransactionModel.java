package com.mnaufalazwar.sibandarapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class DataTransactionModel implements Parcelable {

    private String subject;
    private String Transactiontype;
    private ArrayList<SingleOrderItemModel> listOrder;
    private String paymentType;
    private String expectedDeliveryDate;
    private String paymentDate;
    private String transactionCode;
    private String transactionStatus;
    private boolean paid;

    public DataTransactionModel() {
    }

    public DataTransactionModel(String subject, String transactiontype, ArrayList<SingleOrderItemModel> listOrder, String paymentType, String expectedDeliveryDate, String paymentDate, String transactionCode, String transactionStatus, boolean paid) {
        this.subject = subject;
        Transactiontype = transactiontype;
        this.listOrder = listOrder;
        this.paymentType = paymentType;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.paymentDate = paymentDate;
        this.transactionCode = transactionCode;
        this.transactionStatus = transactionStatus;
        this.paid = paid;
    }

    protected DataTransactionModel(Parcel in) {
        subject = in.readString();
        Transactiontype = in.readString();
        listOrder = in.createTypedArrayList(SingleOrderItemModel.CREATOR);
        paymentType = in.readString();
        expectedDeliveryDate = in.readString();
        paymentDate = in.readString();
        transactionCode = in.readString();
        transactionStatus = in.readString();
        paid = in.readByte() != 0;
    }

    public static final Creator<DataTransactionModel> CREATOR = new Creator<DataTransactionModel>() {
        @Override
        public DataTransactionModel createFromParcel(Parcel in) {
            return new DataTransactionModel(in);
        }

        @Override
        public DataTransactionModel[] newArray(int size) {
            return new DataTransactionModel[size];
        }
    };

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTransactiontype() {
        return Transactiontype;
    }

    public void setTransactiontype(String transactiontype) {
        Transactiontype = transactiontype;
    }

    public ArrayList<SingleOrderItemModel> getListOrder() {
        return listOrder;
    }

    public void setListOrder(ArrayList<SingleOrderItemModel> listOrder) {
        this.listOrder = listOrder;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeString(Transactiontype);
        dest.writeTypedList(listOrder);
        dest.writeString(paymentType);
        dest.writeString(expectedDeliveryDate);
        dest.writeString(paymentDate);
        dest.writeString(transactionCode);
        dest.writeString(transactionStatus);
        dest.writeByte((byte) (paid ? 1 : 0));
    }
}
