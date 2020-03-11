package com.mnaufalazwar.sibandarapp.ui.financial;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.adapter.PaymentAdapter;
import com.mnaufalazwar.sibandarapp.adapter.TransactionAdapter;
import com.mnaufalazwar.sibandarapp.model.DataTransactionModel;
import com.mnaufalazwar.sibandarapp.model.PaymentModel;

import java.util.ArrayList;

public class FinancialFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView tvNoData;
    private Button btnSell, btnPurchase;
    private RecyclerView rvPayment;
    private PaymentAdapter paymentAdapter;
    private ArrayList<PaymentModel> thisPaymentModels = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_finance, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //inisiasi
        progressBar = view.findViewById(R.id.progressBar);
        btnSell = view.findViewById(R.id.btn_sell);
        btnPurchase = view.findViewById(R.id.btn_purchase);
        rvPayment = view.findViewById(R.id.rv_payment);
        tvNoData = view.findViewById(R.id.tvNoData);

        //action button
        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //recyclerview
        rvPayment.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPayment.setHasFixedSize(true);

        paymentAdapter = new PaymentAdapter(getActivity());
        paymentAdapter.setOnItemClickCallback(new PaymentAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(PaymentModel data, int position) {

            }
        });

        rvPayment.setAdapter(paymentAdapter);

        //ViewModel
        FinancialViewModel financialViewModel = new ViewModelProvider(getActivity(),
                new ViewModelProvider.NewInstanceFactory())
                .get(FinancialViewModel.class);

        financialViewModel.setDataPaymentModel();
        showLoading(true);
        tvNoData.setVisibility(View.GONE);
        financialViewModel.getDataPaymentModel().observe(getActivity(), new Observer<ArrayList<PaymentModel>>() {
            @Override
            public void onChanged(ArrayList<PaymentModel> paymentModels) {
                thisPaymentModels = paymentModels;
                paymentAdapter.setList(thisPaymentModels);
                showLoading(false);
                if (thisPaymentModels.size() != 0) {
                    tvNoData.setVisibility(View.GONE);
                }else {
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }
        });

        //no data
        if (thisPaymentModels.size() != 0) {
            tvNoData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}