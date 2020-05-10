package com.mnaufalazwar.sibandarapp.ui.sell;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.mnaufalazwar.sibandarapp.activity.TransactionPrepareToSendActivity;
import com.mnaufalazwar.sibandarapp.adapter.TransactionAdapter;
import com.mnaufalazwar.sibandarapp.model.CustomerModel;
import com.mnaufalazwar.sibandarapp.model.DataTransactionModel;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;

import java.util.ArrayList;

public class SellFragment extends Fragment {

    private ProgressBar progressBar;
    private Button btnOrderToday, btnOrderAll, btnAddOrder;
    private TextView tvNoOrder;
    private RecyclerView rvListOrder;
    private TransactionAdapter transactionAdapter;
    private ArrayList<DataTransactionModel> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sell, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnOrderToday = view.findViewById(R.id.btn_order_today);
        btnOrderAll = view.findViewById(R.id.btn_order_ongoing);
        tvNoOrder = view.findViewById(R.id.tvNoOrder);
        progressBar = view.findViewById(R.id.progressBar);

        btnAddOrder = view.findViewById(R.id.btn_add_order);
        btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddCustomerOrderActivity.class);
                startActivityForResult(intent, AddCustomerOrderActivity.REQUEST_ADD_CUSTOMER_ORDER);
            }
        });

        rvListOrder = view.findViewById(R.id.rvListOrder);
        rvListOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListOrder.setHasFixedSize(true);

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
        rvListOrder.setAdapter(transactionAdapter);

        SellViewModel sellViewModel = new ViewModelProvider(getActivity(),
                new ViewModelProvider.NewInstanceFactory())
                .get(SellViewModel.class);

        sellViewModel.setDataTransactionModel();
        showLoading(true);
        tvNoOrder.setVisibility(View.GONE);
        sellViewModel.getDataTransactionModel().observe(getActivity(), new Observer<ArrayList<DataTransactionModel>>() {
            @Override
            public void onChanged(ArrayList<DataTransactionModel> dataTransactionModels) {
                list = dataTransactionModels;
                transactionAdapter.setList(list);
                showLoading(false);
                if (list.size() != 0) {
                    tvNoOrder.setVisibility(View.GONE);
                }else {
                    tvNoOrder.setVisibility(View.VISIBLE);
                }
            }
        });

        if (list.size() != 0) {
            tvNoOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == AddCustomerOrderActivity.REQUEST_ADD_CUSTOMER_ORDER) {
                if (resultCode == AddCustomerOrderActivity.RESULT_ADD_CUSTOMER_ORDER) {

                    Toast.makeText(getContext(), "pesanan sampai di sell", Toast.LENGTH_SHORT).show();

                    CustomerModel customerModel = data.getParcelableExtra(AddCustomerOrderActivity.EXTRA_CUSTOMER);
                    ArrayList<SingleOrderItemModel> listOrder = data.getParcelableArrayListExtra(AddCustomerOrderActivity.EXTRA_ORDER_LIST);

                    DataTransactionModel dataTransactionModel = new DataTransactionModel();

                    dataTransactionModel.setSubject(customerModel.getCompany());
                    dataTransactionModel.setListOrder(listOrder);
//                    dataTransactionModel.setTransactionStatus("1");
                    dataTransactionModel.setTransactionStatus("order");
                    dataTransactionModel.setTransactionCode(data.getStringExtra(AddCustomerOrderActivity.EXTRA_TRANSACTION_ID));

                    list.add(0, dataTransactionModel);
                    transactionAdapter.setList(list);
                    tvNoOrder.setVisibility(View.GONE);
                }
            }

            if (requestCode == TransactionPrepareToSendActivity.REQUEST_TRANSACTION_PTS) {
                if (resultCode == TransactionPrepareToSendActivity.RESULT_TRANSACTION_PTS) {

                    DataTransactionModel dataTransactionModel = data.getParcelableExtra(TransactionPrepareToSendActivity.EXTRA_DATA_TRANSACTION_OUT);

                    int pos = data.getIntExtra(TransactionPrepareToSendActivity.EXTRA_DATA_TRANSACTION_POS, -1);
                    if (pos >= 0) {
                        transactionAdapter.updateItem(pos, dataTransactionModel);
                    }
                    tvNoOrder.setVisibility(View.GONE);
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