package com.mnaufalazwar.sibandarapp.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.activity.AddCustomerOrderActivity;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;

public class AddOrderItemDialog extends DialogFragment implements View.OnClickListener {

    Button btnAdd, btnClose;
    EditText etCommodity, etAmount, etPriceKg;
    OnAddOrderItemListener addOrderItemListener;

    public AddOrderItemDialog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_order_item_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAdd = view.findViewById(R.id.btn_add_customer);
        btnAdd.setOnClickListener(this);
        btnClose = view.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
        etCommodity = view.findViewById(R.id.etComodity);
        etAmount = view.findViewById(R.id.etAmount);
        etPriceKg = view.findViewById(R.id.etPriceKg);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = getActivity();
        if(activity instanceof AddCustomerOrderActivity){
            AddCustomerOrderActivity addCustomerOrderActivity = (AddCustomerOrderActivity) activity;
            this.addOrderItemListener = addCustomerOrderActivity.addOrderItemListener;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.addOrderItemListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_close:
                getDialog().cancel();
                break;
            case R.id.btn_add_customer:
                SingleOrderItemModel model = new SingleOrderItemModel();

                String commodity = etCommodity.getText().toString();
                String amount = etAmount.getText().toString().trim();
                String priceKg = etPriceKg.getText().toString();

                if(commodity.isEmpty()){
                    model.setCommodity("-");
                }
                else {
                    model.setCommodity(commodity);
                }

                if(amount.isEmpty()){
                    model.setAmountOrderKg("0");
                }
                else {
                    model.setAmountOrderKg(amount);
                }

                if(priceKg.isEmpty()){
                    model.setPriceKg("0");
                }
                else {
                    model.setPriceKg(priceKg);
                }

                if(addOrderItemListener != null){
                    addOrderItemListener.onOptionChoosen(model);
                }

                getDialog().dismiss();
                break;
        }

    }

    public interface OnAddOrderItemListener{
        void onOptionChoosen(SingleOrderItemModel singleOrderItemModel);
    }
}
