package com.mnaufalazwar.sibandarapp.ui.financial;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.activity.PaymentDetailToPayActivity;
import com.mnaufalazwar.sibandarapp.adapter.PaymentAdapter;
import com.mnaufalazwar.sibandarapp.model.PaymentModel;

import java.util.ArrayList;

public class FinancialFragment extends Fragment {

    FinancialViewModel financialViewModel;

    private static String payType = "1";

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
        btnSell.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        btnPurchase.setBackgroundColor(getResources().getColor(R.color.colorBtnUnable));
        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSell.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                btnPurchase.setBackgroundColor(getResources().getColor(R.color.colorBtnUnable));
                payType = "1";
                showSellPayment();
            }
        });

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPurchase.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                btnSell.setBackgroundColor(getResources().getColor(R.color.colorBtnUnable));
                payType = "2";
                showPurchaseCard();
            }
        });

        //recyclerview
        rvPayment.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPayment.setHasFixedSize(true);

        paymentAdapter = new PaymentAdapter(getActivity());
        paymentAdapter.setOnItemClickCallback(new PaymentAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(PaymentModel data, int position) {

                Intent intent = new Intent(getActivity(), PaymentDetailToPayActivity.class);

                Log.d("DATA SEND EXTRA", "cards: " + data.getDataTransactionModels().size());

                intent.putExtra(PaymentDetailToPayActivity.EXTRA_DATA_PAYMENT_TYPE, payType);
                intent.putExtra(PaymentDetailToPayActivity.EXTRA_DATA_PAYMENT_IN, data);
                intent.putExtra(PaymentDetailToPayActivity.EXTRA_DATA_PAYMENT_IN_POS, position);
                startActivityForResult(intent, PaymentDetailToPayActivity.REQUEST_PAYMENT);

            }
        });

        rvPayment.setAdapter(paymentAdapter);

        //ViewModel
        financialViewModel = new ViewModelProvider(getActivity(),
                new ViewModelProvider.NewInstanceFactory())
                .get(FinancialViewModel.class);

        //show sell payment
        showSellPayment();

        //no data
        if (thisPaymentModels.size() != 0) {
            tvNoData.setVisibility(View.GONE);
        }
    }

    private void showSellPayment(){
        financialViewModel.setDataPaymentModel();
        showLoading(true);
        tvNoData.setVisibility(View.GONE);
        rvPayment.setVisibility(View.GONE);
        financialViewModel.getDataPaymentModel().observe(getActivity(), new Observer<ArrayList<PaymentModel>>() {
            @Override
            public void onChanged(ArrayList<PaymentModel> paymentModels) {
                thisPaymentModels = paymentModels;
                paymentAdapter.setList(thisPaymentModels);
                showLoading(false);
                rvPayment.setVisibility(View.VISIBLE);
                if (thisPaymentModels.size() != 0) {
                    tvNoData.setVisibility(View.GONE);
                }else {
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showPurchaseCard(){
        financialViewModel.setDataPaymentModelPurchase();
        showLoading(true);
        tvNoData.setVisibility(View.GONE);
        rvPayment.setVisibility(View.GONE);
        financialViewModel.getDataPaymentModelPurchase().observe(getActivity(), new Observer<ArrayList<PaymentModel>>() {
            @Override
            public void onChanged(ArrayList<PaymentModel> paymentModels) {
                thisPaymentModels = paymentModels;
                paymentAdapter.setList(thisPaymentModels);
                showLoading(false);
                rvPayment.setVisibility(View.VISIBLE);
                if (thisPaymentModels.size() != 0) {
                    tvNoData.setVisibility(View.GONE);
                }else {
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == PaymentDetailToPayActivity.REQUEST_PAYMENT) {
                if (resultCode == PaymentDetailToPayActivity.RESULT_PAYMENT) {

                    PaymentModel paymentModel = data.getParcelableExtra(PaymentDetailToPayActivity.EXTRA_DATA_PAYMENT_OUT);

                    int pos = data.getIntExtra(PaymentDetailToPayActivity.EXTRA_DATA_PAYMENT_POS, -1);
                    if (pos >= 0) {
                        paymentAdapter.updateItem(pos, paymentModel);
                    }
                    tvNoData.setVisibility(View.GONE);
                }
            }
        }

    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}