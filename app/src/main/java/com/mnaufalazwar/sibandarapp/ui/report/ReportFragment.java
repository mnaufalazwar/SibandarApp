package com.mnaufalazwar.sibandarapp.ui.report;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.activity.PaymentDetailToPayActivity;
import com.mnaufalazwar.sibandarapp.adapter.PaymentAdapter;
import com.mnaufalazwar.sibandarapp.adapter.ReportAdapter;
import com.mnaufalazwar.sibandarapp.common.NumberToRupiah;
import com.mnaufalazwar.sibandarapp.model.PaymentModel;
import com.mnaufalazwar.sibandarapp.model.ReportCardModel;
import com.mnaufalazwar.sibandarapp.ui.financial.FinancialViewModel;

import java.util.ArrayList;

public class ReportFragment extends Fragment {

    private ReportViewModel reportViewModel;

    private ProgressBar progressBar;
    TextView utang, piutang, saldo, uangMasuk, uangKeluar, tvNoData;
    RecyclerView rvListTransaction;
    private ReportAdapter adapter;
    private ArrayList<ReportCardModel> reportCardModels = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //inisiasi
        progressBar = view.findViewById(R.id.progressBar);
        utang = view.findViewById(R.id.utang);
        piutang = view.findViewById(R.id.piutang);
        saldo = view.findViewById(R.id.saldo);
        uangMasuk = view.findViewById(R.id.uangMasuk);
        uangKeluar = view.findViewById(R.id.uangKeluar);
        tvNoData = view.findViewById(R.id.tvNoOrder);

        //recyclerview
        rvListTransaction = view.findViewById(R.id.rvListTransaction);
        rvListTransaction.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListTransaction.setHasFixedSize(true);

        adapter = new ReportAdapter(getActivity());
        rvListTransaction.setAdapter(adapter);

        //ViewModel
        reportViewModel = new ViewModelProvider(getActivity(),
                new ViewModelProvider.NewInstanceFactory())
                .get(ReportViewModel.class);

        //show sell payment
        showData();

        //no data
        if (reportCardModels.size() != 0) {
            tvNoData.setVisibility(View.GONE);
        }
    }

    private void showData(){
        reportViewModel.setData();
        showLoading(true);
        tvNoData.setVisibility(View.GONE);
        reportViewModel.getDataCashflow().observe(getActivity(), new Observer<ArrayList<ReportCardModel>>() {
            @Override
            public void onChanged(ArrayList<ReportCardModel> paymentModels) {
                reportCardModels = paymentModels;
                adapter.setList(reportCardModels);
                showLoading(false);
                if (reportCardModels.size() != 0) {
                    tvNoData.setVisibility(View.GONE);
                }else {
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }
        });

        reportViewModel.getDataUtang().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                utang.setText(s);
                utang.setText(NumberToRupiah.convertNumberToRupiah(s));
            }
        });

        reportViewModel.getDataPiutang().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                piutang.setText(s);
                piutang.setText(NumberToRupiah.convertNumberToRupiah(s));
            }
        });

        reportViewModel.getDataKeluar().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                uangKeluar.setText(s);
                uangKeluar.setText(NumberToRupiah.convertNumberToRupiah(s));
            }
        });

        reportViewModel.getDataMasuk().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                uangMasuk.setText(s);
                uangMasuk.setText(NumberToRupiah.convertNumberToRupiah(s));
            }
        });

        reportViewModel.getDataSaldo().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                saldo.setText(s);
                saldo.setText(NumberToRupiah.convertNumberToRupiah(s));
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}