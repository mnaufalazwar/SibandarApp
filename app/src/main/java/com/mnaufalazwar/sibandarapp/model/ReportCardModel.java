package com.mnaufalazwar.sibandarapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ReportCardModel implements Parcelable {

    private String subject;
    private String nominal;
    private String paidId;
    private String time;
    private ArrayList<String> cards;

    public ReportCardModel() {
    }

    public ReportCardModel(String subject, String nominal, String paidId, String time, ArrayList<String> cards) {
        this.subject = subject;
        this.nominal = nominal;
        this.paidId = paidId;
        this.time = time;
        this.cards = cards;
    }

    protected ReportCardModel(Parcel in) {
        subject = in.readString();
        nominal = in.readString();
        paidId = in.readString();
        time = in.readString();
        cards = in.createStringArrayList();
    }

    public static final Creator<ReportCardModel> CREATOR = new Creator<ReportCardModel>() {
        @Override
        public ReportCardModel createFromParcel(Parcel in) {
            return new ReportCardModel(in);
        }

        @Override
        public ReportCardModel[] newArray(int size) {
            return new ReportCardModel[size];
        }
    };

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getPaidId() {
        return paidId;
    }

    public void setPaidId(String paidId) {
        this.paidId = paidId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<String> getCards() {
        return cards;
    }

    public void setCards(ArrayList<String> cards) {
        this.cards = cards;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeString(nominal);
        dest.writeString(paidId);
        dest.writeString(time);
        dest.writeStringList(cards);
    }
}
