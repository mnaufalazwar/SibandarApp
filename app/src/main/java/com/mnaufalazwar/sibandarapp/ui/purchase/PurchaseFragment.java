package com.mnaufalazwar.sibandarapp.ui.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.activity.AddCustomerOrderActivity;
import com.mnaufalazwar.sibandarapp.activity.AddPurchaseActivity;
import com.mnaufalazwar.sibandarapp.activity.TransactionPrepareToSendActivity;
import com.mnaufalazwar.sibandarapp.adapter.TransactionAdapter;
import com.mnaufalazwar.sibandarapp.model.CustomerModel;
import com.mnaufalazwar.sibandarapp.model.DataTransactionModel;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;
import com.mnaufalazwar.sibandarapp.ui.sell.SellViewModel;

import java.util.ArrayList;

public class PurchaseFragment extends Fragment {

    private ProgressBar progressBar;
    private Button btnRestockToday, btnRestockAll, btnAddRestock;
    private TextView tvNoRestock;
    private RecyclerView rvListRestock;
    private TransactionAdapter transactionAdapter;
    private ArrayList<DataTransactionModel> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_purchase, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnRestockToday = view.findViewById(R.id.btn_restock_ongoing);
        btnRestockAll = view.findViewById(R.id.btn_restock_ongoing);
        tvNoRestock = view.findViewById(R.id.tvNoRestock);
        progressBar = view.findViewById(R.id.progressBar);

        btnAddRestock = view.findViewById(R.id.btn_add_restock);
        btnAddRestock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddPurchaseActivity.class);
                startActivityForResult(intent, AddPurchaseActivity.REQUEST_ADD_PURCHASE);
            }
        });

        rvListRestock = view.findViewById(R.id.rvListRestock);
        rvListRestock.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListRestock.setHasFixedSize(true);

        transactionAdapter = new TransactionAdapter(getActivity());
        transactionAdapter.setOnItemClickCallback(new TransactionAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(DataTransactionModel data, int position) {

                Intent intent = new Intent(getActivity(), TransactionPrepareToSendActivity.class);
                intent.putExtra(TransactionPrepareToSendActivity.EXTRA_DATA_TRANSACTION_IN, data);
                intent.putExtra(TransactionPrepareToSendActivity.EXTRA_DATA_TRANSACTION_IN_POS, position);
                startActivityForResult(intent, TransactionPrepareToSendActivity.REQUEST_TRANSACTION_PTS);
            }
        });
        rvListRestock.setAdapter(transactionAdapter);

        PurchaseViewModel purchaseViewModel = new ViewModelProvider(getActivity(),
                new ViewModelProvider.NewInstanceFactory())
                .get(PurchaseViewModel.class);

        purchaseViewModel.setDataTransactionModel();
        showLoading(true);
        tvNoRestock.setVisibility(View.GONE);
        purchaseViewModel.getDataTransactionModel().observe(getActivity(), new Observer<ArrayList<DataTransactionModel>>() {
            @Override
            public void onChanged(ArrayList<DataTransactionModel> dataTransactionModels) {
                list = dataTransactionModels;
                transactionAdapter.setList(list);
                showLoading(false);
                if (list.size() != 0) {
                    tvNoRestock.setVisibility(View.GONE);
                }else {
                    tvNoRestock.setVisibility(View.VISIBLE);
                }
            }
        });

        if (list.size() != 0) {
            tvNoRestock.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == AddPurchaseActivity.REQUEST_ADD_PURCHASE) {
                if (resultCode == AddPurchaseActivity.RESULT_ADD_PURCHASE) {

                    Toast.makeText(getContext(), "pesanan dibuat", Toast.LENGTH_SHORT).show();

                    CustomerModel customerModel = data.getParcelableExtra(AddPurchaseActivity.EXTRA_FARMER);
                    ArrayList<SingleOrderItemModel> listOrder = data.getParcelableArrayListExtra(AddPurchaseActivity.EXTRA_RESTOCK_LIST);

                    DataTransactionModel dataTransactionModel = new DataTransactionModel();

                    dataTransactionModel.setSubject(customerModel.getCompany());
                    dataTransactionModel.setListOrder(listOrder);
                    dataTransactionModel.setTransactionStatus("1");
                    dataTransactionModel.setTransactionCode(data.getStringExtra(AddPurchaseActivity.EXTRA_TRANSACTION_ID));

                    list.add(0, dataTransactionModel);
                    transactionAdapter.setList(list);
                    tvNoRestock.setVisibility(View.GONE);
                }
            }

            if (requestCode == TransactionPrepareToSendActivity.REQUEST_TRANSACTION_PTS) {
                if (resultCode == TransactionPrepareToSendActivity.RESULT_TRANSACTION_PTS) {

                    DataTransactionModel dataTransactionModel = data.getParcelableExtra(TransactionPrepareToSendActivity.EXTRA_DATA_TRANSACTION_OUT);

                    int pos = data.getIntExtra(TransactionPrepareToSendActivity.EXTRA_DATA_TRANSACTION_POS, -1);
                    if (pos >= 0) {
                        transactionAdapter.updateItem(pos, dataTransactionModel);
                    }
                    tvNoRestock.setVisibility(View.GONE);
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