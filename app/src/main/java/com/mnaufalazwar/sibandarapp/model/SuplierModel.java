package com.mnaufalazwar.sibandarapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SuplierModel implements Parcelable {

    private String name;
    private String type;
    private String phoneNumber;
    private String email;
    private String address;

    public SuplierModel() {
    }

    public SuplierModel(String name, String type, String phoneNumber, String email, String address) {
        this.name = name;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    protected SuplierModel(Parcel in) {
        name = in.readString();
        type = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        address = in.readString();
    }

    public static final Creator<SuplierModel> CREATOR = new Creator<SuplierModel>() {
        @Override
        public SuplierModel createFromParcel(Parcel in) {
            return new SuplierModel(in);
        }

        @Override
        public SuplierModel[] newArray(int size) {
            return new SuplierModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeString(address);
    }
}
