package com.mnaufalazwar.sibandarapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.mnaufalazwar.sibandarapp.R;

public class HomeFragment extends Fragment {

    private View root;
    private HomeViewModel homeViewModel;
    private CardView cardHome, cardSell, cardPurchase, cardFinance, cardReport;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cardSell = root.findViewById(R.id.card_sell);
        cardSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections navDirections = HomeFragmentDirections
                        .actionNavHomeToNavSell();
                Navigation.findNavController(v).navigate(navDirections);

                Navigation.findNavController(v).addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                    @Override
                    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                    }
                });
            }
        });

        cardPurchase = root.findViewById(R.id.card_purchase);
        cardPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections navDirections = HomeFragmentDirections
                        .actionNavHomeToNavPurchase();
                Navigation.findNavController(v).navigate(navDirections);

                Navigation.findNavController(v).addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                    @Override
                    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                    }
                });
            }
        });

        cardFinance = root.findViewById(R.id.card_finance);
        cardFinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections navDirections = HomeFragmentDirections
                        .actionNavHomeToNavFinance();
                Navigation.findNavController(v).navigate(navDirections);

                Navigation.findNavController(v).addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                    @Override
                    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                    }
                });
            }
        });

        cardReport = root.findViewById(R.id.card_report);
        cardReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections navDirections = HomeFragmentDirections
                        .actionNavHomeToNavReport();
                Navigation.findNavController(v).navigate(navDirections);

                Navigation.findNavController(v).addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                    @Override
                    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                    }
                });
            }
        });
    }
}