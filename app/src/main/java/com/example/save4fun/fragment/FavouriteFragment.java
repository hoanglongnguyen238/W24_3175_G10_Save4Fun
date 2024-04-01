package com.example.save4fun.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.save4fun.R;
import com.example.save4fun.adapter.FavouriteProductAdapter;
import com.example.save4fun.adapter.PopularProductAdapter;
import com.example.save4fun.db.DBProductsHelper;
import com.example.save4fun.model.MyList;
import com.example.save4fun.model.Product;
import com.example.save4fun.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    RecyclerView recyclerViewFavouriteProduct;
    SearchView searchViewFavouriteProduct;
    DBProductsHelper dbProductsHelper;
    List<Product> favouriteProducts;
    String username = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        searchViewFavouriteProduct = view.findViewById(R.id.searchViewFavouriteProduct);
        searchViewFavouriteProduct.clearFocus();

        dbProductsHelper = new DBProductsHelper(getContext());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constant.PREFERENCES_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(Constant.USERNAME, "");

        favouriteProducts = dbProductsHelper.getFavouriteProductsByUsername(username);

        FavouriteProductAdapter favouriteProductAdapter = new FavouriteProductAdapter(favouriteProducts);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        recyclerViewFavouriteProduct = view.findViewById(R.id.recyclerViewFavouriteProduct);
        recyclerViewFavouriteProduct.setAdapter(favouriteProductAdapter);
        recyclerViewFavouriteProduct.setLayoutManager(gridLayoutManager);

        favouriteProductAdapter.setOnRemoveFavouriteItemClickListener(new FavouriteProductAdapter.OnRemoveFavouriteItemClickListener() {
            @Override
            public void onRemoveFavouriteItemClick(int position) {
                int productId = favouriteProducts.get(position).getId();
                dbProductsHelper.addOrRemoveFavouriteProduct(username, productId, false);

                favouriteProducts = dbProductsHelper.getFavouriteProductsByUsername(username);
                favouriteProductAdapter.updateData(favouriteProducts);
            }
        });

        searchViewFavouriteProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Product> filteredFavouriteProducts = filterListOfFavouriteProducts(newText);
                favouriteProductAdapter.updateData(filteredFavouriteProducts);
                return false;
            }
        });

        return view;
    }

    private List<Product> filterListOfFavouriteProducts(String text) {
        favouriteProducts = dbProductsHelper.getFavouriteProductsByUsername(username);
        List<Product> filteredFavouriteProducts = new ArrayList<>();
        for (Product favouriteProduct : favouriteProducts) {
            if (favouriteProduct.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredFavouriteProducts.add(favouriteProduct);
            }
        }
        favouriteProducts = filteredFavouriteProducts;
        return filteredFavouriteProducts;
    }
}