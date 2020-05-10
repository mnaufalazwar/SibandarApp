package com.mnaufalazwar.sibandarapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.adapter.OrderItemAdapter;
import com.mnaufalazwar.sibandarapp.common.NumberToRupiah;
import com.mnaufalazwar.sibandarapp.model.CustomerModel;
import com.mnaufalazwar.sibandarapp.model.DataTransactionModel;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;
import com.mnaufalazwar.sibandarapp.ui.dialog.AddCustomerDialog;
import com.mnaufalazwar.sibandarapp.ui.dialog.AddOrderItemDialog;
import com.mnaufalazwar.sibandarapp.ui.sell.SellViewModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddCustomerOrderActivity extends AppCompatActivity {

    public static final int RESULT_ADD_CUSTOMER_ORDER = 101;
    public static final int REQUEST_ADD_CUSTOMER_ORDER = 100;
    public static final String EXTRA_CUSTOMER = "extra_customer";
    public static final String EXTRA_ORDER_LIST = "extra_order_list";
    public static final String EXTRA_TRANSACTION_ID = "extra_transaction_id";

    private ProgressBar progressBar;

    RecyclerView recyclerView;
    OrderItemAdapter adapter;

    Button btnAddOrderItem, btnAddCustomerData, btnSaveOrder;
    TextView tvTotalPriceOrder, tvNoOrder;

    ArrayList<SingleOrderItemModel> singleOrderItemModels = new ArrayList<>();
    CustomerModel thisCustomerModel = new CustomerModel();

    public static String responseReturnPostSell = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_order);

//        if(getActionBar() != null){
//            getActionBar().hide();
//        }

        progressBar = findViewById(R.id.progressBar);

        btnAddOrderItem = findViewById(R.id.btn_add_order_item);
        btnAddCustomerData = findViewById(R.id.btn_add_customer);
        btnSaveOrder = findViewById(R.id.btn_save_order);
        tvTotalPriceOrder = findViewById(R.id.tv_total_price_transaction);
        tvNoOrder = findViewById(R.id.tvNoOrder);

        recyclerView = findViewById(R.id.rvOrderItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddCustomerOrderActivity.this));
        recyclerView.setHasFixedSize(true);

        adapter = new OrderItemAdapter(AddCustomerOrderActivity.this);
        adapter.setOnItemClickCallback(new OrderItemAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(SingleOrderItemModel data, int position) {

            }
        });

        adapter.setList(singleOrderItemModels);
        recyclerView.setAdapter(adapter);


        btnSaveOrder.setEnabled(false);
        btnAddOrderItem.setEnabled(false);

        btnAddOrderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddOrderItemDialog mAddOrderItemDialog = new AddOrderItemDialog();
                FragmentManager mFragmentManager = getSupportFragmentManager();
                mAddOrderItemDialog.show(mFragmentManager, AddOrderItemDialog.class.getSimpleName());
            }
        });

        btnAddCustomerData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddCustomerDialog mAddCustomerDialog = new AddCustomerDialog();
                FragmentManager mFragmentManager = getSupportFragmentManager();
                mAddCustomerDialog.show(mFragmentManager, AddCustomerDialog.class.getSimpleName());

                btnAddOrderItem.setEnabled(true);
            }
        });

        btnSaveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AddCustomerOrderActivity.this, "pesanan ditambahkan", Toast.LENGTH_SHORT).show();

                AddCustomerOrderViewModel addCustomerOrderViewModel = new ViewModelProvider(AddCustomerOrderActivity.this,
                        new ViewModelProvider.NewInstanceFactory())
                        .get(AddCustomerOrderViewModel.class);

                addCustomerOrderViewModel.setTransactionCode(thisCustomerModel, singleOrderItemModels);
                showLoading(true);
                addCustomerOrderViewModel.getTransactionCode().observe(AddCustomerOrderActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {

                        String transactionCode = s;

                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_TRANSACTION_ID, transactionCode);
                        intent.putExtra(EXTRA_CUSTOMER, thisCustomerModel);
                        intent.putParcelableArrayListExtra(EXTRA_ORDER_LIST, singleOrderItemModels);
                        setResult(RESULT_ADD_CUSTOMER_ORDER, intent);
                        finish();

                        showLoading(false);

                    }
                });
            }
        });

        if(singleOrderItemModels.size() != 0){
            tvNoOrder.setVisibility(View.GONE);
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public AddOrderItemDialog.OnAddOrderItemListener addOrderItemListener = new AddOrderItemDialog.OnAddOrderItemListener() {
        @Override
        public void onOptionChoosen(SingleOrderItemModel singleOrderItemModel) {

            singleOrderItemModels.add(singleOrderItemModel);
            adapter.setList(singleOrderItemModels);
            tvNoOrder.setVisibility(View.GONE);

            int total = 0 ;
            for(int i = 0 ; i < singleOrderItemModels.size() ; i ++){
                total += Integer.parseInt(singleOrderItemModels.get(i).getPriceKg().trim()) * Integer.parseInt(singleOrderItemModels.get(i).getAmountOrderKg().trim());
            }
            tvTotalPriceOrder.setText(NumberToRupiah.convertNumberToRupiah("" + total));

            if(singleOrderItemModels.size() > 0){
                btnSaveOrder.setEnabled(true);
            }
        }
    };

    public AddCustomerDialog.OnAddCustomerListener addCustomerListener
            = new AddCustomerDialog.OnAddCustomerListener() {
        @Override
        public void onOptionChoosen(CustomerModel customerModel) {
            Toast.makeText(AddCustomerOrderActivity.this, customerModel.getContactPerson() + " dari " + customerModel.getCompany() + " berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            thisCustomerModel = customerModel;
        }
    };
}
