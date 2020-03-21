package com.mnaufalazwar.sibandarapp.ui.dialog;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.activity.AddCustomerOrderActivity;
import com.mnaufalazwar.sibandarapp.activity.AddPurchaseActivity;
import com.mnaufalazwar.sibandarapp.model.CustomerModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFarmerDialog extends DialogFragment implements View.OnClickListener {

    Button btnAdd, btnClose;
    EditText etCompany, etName, etPhone, etEmail, etAddress;
    OnAddFarmerListener addFarmerListener;

    public AddFarmerDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_farmer_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAdd = view.findViewById(R.id.btn_add_customer);
        btnAdd.setOnClickListener(this);
        btnClose = view.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
        etCompany = view.findViewById(R.id.etCompany);
        etName = view.findViewById(R.id.etPerson);
        etPhone = view.findViewById(R.id.etPhone);
        etEmail = view.findViewById(R.id.etEmail);
        etAddress = view.findViewById(R.id.etAddress);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Activity activity = getActivity();
        if(activity instanceof AddPurchaseActivity){
            AddPurchaseActivity addPurchaseActivity = (AddPurchaseActivity) activity;
            this.addFarmerListener = addPurchaseActivity.addFarmerListener;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        this.addFarmerListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_close:
                getDialog().cancel();
                break;
            case R.id.btn_add_customer:
                CustomerModel model = new CustomerModel();

                String company = etCompany.getText().toString();
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String email = etEmail.getText().toString();
                String address = etAddress.getText().toString();

                if(company.isEmpty()){
                    model.setCompany("-");
                }
                else {
                    model.setCompany(company);
                }

                if(name.isEmpty()){
                    model.setContactPerson("-");
                }
                else {
                    model.setContactPerson(name);
                }

                if(phone.isEmpty()){
                    model.setPhoneNumber("-");
                }
                else {
                    model.setPhoneNumber(phone);
                }

                if(email.isEmpty()){
                    model.setEmail("-");
                }
                else {
                    model.setEmail(email);
                }

                if(address.isEmpty()){
                    model.setDeliveryAddress("-");
                }
                else {
                    model.setDeliveryAddress(address);
                }

                if(addFarmerListener != null){
                    addFarmerListener.onOptionChoosen(model);
                }

                getDialog().dismiss();
                break;
        }
    }

    public interface OnAddFarmerListener{
        void onOptionChoosen(CustomerModel customerModel);
    }
}
