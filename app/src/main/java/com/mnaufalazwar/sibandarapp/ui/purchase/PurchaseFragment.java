package com.mnaufalazwar.sibandarapp.ui.purchase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.mnaufalazwar.sibandarapp.R;

public class PurchaseFragment extends Fragment {

    private PurchaseViewModel purchaseViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        purchaseViewModel =
                ViewModelProviders.of(this).get(PurchaseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_purchase, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        purchaseViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}