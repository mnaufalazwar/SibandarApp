package com.mnaufalazwar.sibandarapp.ui.sell;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.adapter.CustomerAdapter;
import com.mnaufalazwar.sibandarapp.model.CustomerModel;
import com.mnaufalazwar.sibandarapp.ui.dialog.AddCustomerDialog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewOrderFrag extends Fragment {

    Button btnAddCustomer;
    View noData;

    RecyclerView recyclerView;
    CustomerAdapter adapter;

    ArrayList<CustomerModel> customerList = new ArrayList<>();

    public AddNewOrderFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAddCustomer = view.findViewById(R.id.btnAddCustomer);

        recyclerView = view.findViewById(R.id.rvCustomer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CustomerAdapter(getActivity());
        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);

        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddCustomerDialog mAddCustomerDialog = new AddCustomerDialog();
                FragmentManager mFragmentManager = getChildFragmentManager();
                mAddCustomerDialog.show(mFragmentManager, AddCustomerDialog.class.getSimpleName());
            }
        });

        noData = view.findViewById(R.id.tvNoCustomer);
        if(customerList.size() != 0){
            noData.setVisibility(View.GONE);
        }
    }

    public AddCustomerDialog.OnAddCustomerListener addCustomerListener
            = new AddCustomerDialog.OnAddCustomerListener() {
        @Override
        public void onOptionChoosen(CustomerModel customerModel) {
            Toast.makeText(getActivity(), customerModel.getContactPerson() + " dari " + customerModel.getCompany() + " berhasil ditambahkan", Toast.LENGTH_SHORT).show();

//            if(customerList.size() == 0){
//                customerList.add(customerModel);
//                adapter.setList(customerList);
//                noData.setVisibility(View.GONE);
//            }else {
//                adapter.addItem(customerModel);
//                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
//                noData.setVisibility(View.GONE);
//            }

            customerList.add(customerModel);
            adapter.setList(customerList);
            noData.setVisibility(View.GONE);
        }
    };

}
