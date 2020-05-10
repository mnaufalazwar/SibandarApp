package com.mnaufalazwar.sibandarapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.adapter.OrderItemAdapter;
import com.mnaufalazwar.sibandarapp.common.NumberToRupiah;
import com.mnaufalazwar.sibandarapp.model.CustomerModel;
import com.mnaufalazwar.sibandarapp.model.SingleOrderItemModel;
import com.mnaufalazwar.sibandarapp.ui.dialog.AddCustomerDialog;
import com.mnaufalazwar.sibandarapp.ui.dialog.AddFarmerDialog;
import com.mnaufalazwar.sibandarapp.ui.dialog.AddOrderItemDialog;
import com.mnaufalazwar.sibandarapp.ui.dialog.AddPurchaseItemDialog;

import java.util.ArrayList;

public class AddPurchaseActivity extends AppCompatActivity {

    public static final int RESULT_ADD_PURCHASE  = 401;
    public static final int REQUEST_ADD_PURCHASE = 400;
    public static final String EXTRA_FARMER = "extra_farmer";
    public static final String EXTRA_RESTOCK_LIST = "extra_restock_list";
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
        setContentView(R.layout.activity_add_purchase);

        progressBar = findViewById(R.id.progressBar);

        btnAddOrderItem = findViewById(R.id.btn_add_order_item);
        btnAddCustomerData = findViewById(R.id.btn_add_customer);
        btnSaveOrder = findViewById(R.id.btn_save_order);
        tvTotalPriceOrder = findViewById(R.id.tv_total_price_transaction);
        tvNoOrder = findViewById(R.id.tvNoOrder);

        recyclerView = findViewById(R.id.rvOrderItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddPurchaseActivity.this));
        recyclerView.setHasFixedSize(true);

        adapter = new OrderItemAdapter(AddPurchaseActivity.this);
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

                AddPurchaseItemDialog mAddPurchaseItemDialog = new AddPurchaseItemDialog();
                FragmentManager mFragmentManager = getSupportFragmentManager();
                mAddPurchaseItemDialog.show(mFragmentManager, AddPurchaseItemDialog.class.getSimpleName());
            }
        });

        btnAddCustomerData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddFarmerDialog mAddFarmerDialog = new AddFarmerDialog();
                FragmentManager mFragmentManager = getSupportFragmentManager();
                mAddFarmerDialog.show(mFragmentManager, AddFarmerDialog.class.getSimpleName());

                btnAddOrderItem.setEnabled(true);
            }
        });

        btnSaveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AddPurchaseActivity.this, "restock ditambahkan", Toast.LENGTH_SHORT).show();

                AddPurchaseViewModel addPurchaseViewModel = new ViewModelProvider(AddPurchaseActivity.this,
                        new ViewModelProvider.NewInstanceFactory())
                        .get(AddPurchaseViewModel.class);

                addPurchaseViewModel.setTransactionCode(thisCustomerModel, singleOrderItemModels);
                showLoading(true);
                addPurchaseViewModel.getTransactionCode().observe(AddPurchaseActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {

                        String transactionCode = s;

                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_TRANSACTION_ID, transactionCode);
                        intent.putExtra(EXTRA_FARMER, thisCustomerModel);
                        intent.putParcelableArrayListExtra(EXTRA_RESTOCK_LIST, singleOrderItemModels);
                        setResult(RESULT_ADD_PURCHASE, intent);
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

    public AddPurchaseItemDialog.OnAddPurchaseItemListener addPurchaseItemListener = new AddPurchaseItemDialog.OnAddPurchaseItemListener() {
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

    public AddFarmerDialog.OnAddFarmerListener addFarmerListener
            = new AddFarmerDialog.OnAddFarmerListener() {
        @Override
        public void onOptionChoosen(CustomerModel customerModel) {
            Toast.makeText(AddPurchaseActivity.this, customerModel.getContactPerson() + " dari " + customerModel.getCompany() + " berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            thisCustomerModel = customerModel;
        }
    };
}
