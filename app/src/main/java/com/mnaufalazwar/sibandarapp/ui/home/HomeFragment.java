package com.mnaufalazwar.sibandarapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mnaufalazwar.sibandarapp.R;

public class HomeFragment extends Fragment {

    private View root;
    private HomeViewModel homeViewModel;
    private CardView cardHome, cardSell, cardPurchase, cardFinance, cardReport;
    private ImageView sell, purchase, payment, report;

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

        sell = root.findViewById(R.id.img_item_photo_sell);
        purchase = root.findViewById(R.id.img_item_photo_purchase);
        payment = root.findViewById(R.id.img_item_photo_payment);
        report = root.findViewById(R.id.img_item_photo_report);

        Glide.with(getActivity())
                .load(R.drawable.ic_send_white_24dp)
                .apply(new RequestOptions().override(55,55))
                .into(sell);

        Glide.with(getActivity())
                .load(R.drawable.baseline_shopping_cart_white_48dp)
                .apply(new RequestOptions().override(55,55))
                .into(purchase);

        Glide.with(getActivity())
                .load(R.drawable.baseline_attach_money_white_48dp)
                .apply(new RequestOptions().override(55,55))
                .into(payment);

        Glide.with(getActivity())
                .load(R.drawable.ic_book_white_24dp)
                .apply(new RequestOptions().override(55,55))
                .into(report);

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