package com.mnaufalazwar.sibandarapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomerModel implements Parcelable {

    private String company;
    private String contactPerson;
    private String phoneNumber;
    private String email;
    private String deliveryAddress;

    public CustomerModel() {
    }

    public CustomerModel(String company, String contactPerson, String phoneNumber, String email, String deliveryAddress) {
//        this.name = name;
        this.company = company;
        this.contactPerson = contactPerson;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.deliveryAddress = deliveryAddress;
    }

    protected CustomerModel(Parcel in) {
//        name = in.readString();
        company = in.readString();
        contactPerson = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        deliveryAddress = in.readString();
    }

    public static final Creator<CustomerModel> CREATOR = new Creator<CustomerModel>() {
        @Override
        public CustomerModel createFromParcel(Parcel in) {
            return new CustomerModel(in);
        }

        @Override
        public CustomerModel[] newArray(int size) {
            return new CustomerModel[size];
        }
    };

//    public String getName() {
//        return name;
//    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
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

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(name);
        dest.writeString(company);
        dest.writeString(contactPerson);
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeString(deliveryAddress);
    }
}
