package com.example.save4fun.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.save4fun.R;
import com.example.save4fun.adapter.ProductByListAdapter;
import com.example.save4fun.db.DBProductsHelper;
import com.example.save4fun.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ListDetailFragment extends Fragment {

    List<Product> products = new ArrayList<>();

    TextView textViewProductsByList, textViewTotalPrice;
    RecyclerView recyclerViewProductsInList;

    DBProductsHelper dbProductsHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_detail, container, false);

        textViewProductsByList = view.findViewById(R.id.textViewProductsByList);
        textViewTotalPrice = view.findViewById(R.id.textViewTotalPrice);

        recyclerViewProductsInList = view.findViewById(R.id.recyclerViewProductsInList);

        dbProductsHelper = new DBProductsHelper(getContext());

        Bundle listData = getArguments();
        if (listData != null) {
            int listId = listData.getInt("listId");
            String listName = listData.getString("listName");

            products = dbProductsHelper.getProductsByListId(listId);
            textViewProductsByList.setText("Products in " + listName);

            double total = calculateTotalPrice(products);
            DecimalFormat df = new DecimalFormat("$0.00");
            textViewTotalPrice.setText(df.format(total));
        }

        ProductByListAdapter productByListAdapter = new ProductByListAdapter(products);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerViewProductsInList.setAdapter(productByListAdapter);
        recyclerViewProductsInList.setLayoutManager(linearLayoutManager);

        return view;
    }

    private double calculateTotalPrice(List<Product> products) {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }
}