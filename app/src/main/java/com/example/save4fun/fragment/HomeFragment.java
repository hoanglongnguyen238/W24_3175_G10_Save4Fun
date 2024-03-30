package com.example.save4fun.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.save4fun.R;
import com.example.save4fun.adapter.BannerImageAdapter;
import com.example.save4fun.adapter.PopularProductAdapter;
import com.example.save4fun.db.DBProductsHelper;
import com.example.save4fun.model.Banner;
import com.example.save4fun.model.Product;
import com.example.save4fun.util.Constant;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {

    private ViewPager viewPagerBanner;
    RecyclerView recyclerViewPopularProduct;
    CircleIndicator circleIndicatorBanner;
    private BannerImageAdapter bannerImageAdapter;

    List<Banner> banners;
    
    List<Product> popularProducts;

    DBProductsHelper dbProductsHelper;

    String username = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPagerBanner = view.findViewById(R.id.viewPagerBanner);
        circleIndicatorBanner = view.findViewById(R.id.circleIndicatorBanner);

        loadBanner();
        bannerImageAdapter = new BannerImageAdapter(getContext(), banners);

        viewPagerBanner.setAdapter(bannerImageAdapter);

        circleIndicatorBanner.setViewPager(viewPagerBanner);
        bannerImageAdapter.registerDataSetObserver(circleIndicatorBanner.getDataSetObserver());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constant.PREFERENCES_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(Constant.USERNAME, "");

        dbProductsHelper = new DBProductsHelper(getContext());
        loadPopularProductFromDB(username);

        PopularProductAdapter popularProductAdapter = new PopularProductAdapter(popularProducts);
        
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        recyclerViewPopularProduct = view.findViewById(R.id.recyclerViewPopularProduct);
        recyclerViewPopularProduct.setAdapter(popularProductAdapter);
        recyclerViewPopularProduct.setLayoutManager(gridLayoutManager);

        popularProductAdapter.setOnAddOrRemoveFavouriteItemClickListener(new PopularProductAdapter.OnAddOrRemoveFavouriteItemClickListener() {
            @Override
            public void onAddOrRemoveFavouriteItemClick(int position) {
                if(popularProducts.get(position).getIsFavourite()) {
                    dbProductsHelper.addOrRemoveFavouriteProduct(username, popularProducts.get(position).getId(), false);
                } else {
                    dbProductsHelper.addOrRemoveFavouriteProduct(username, popularProducts.get(position).getId(), true);
                }

                popularProducts = dbProductsHelper.getPopularProducts(username);
                popularProductAdapter.updateData(popularProducts);
            }
        });

        return view;
    }

    private void loadPopularProductFromDB(String username) {
        popularProducts = dbProductsHelper.getPopularProducts(username);
    }

    private void loadBanner() {
        banners = new ArrayList<>();
        banners.add(new Banner(R.drawable.bn_sales_1));
        banners.add(new Banner(R.drawable.bn_sales_2));
        banners.add(new Banner(R.drawable.bn_sales_3));
        banners.add(new Banner(R.drawable.bn_sales_4));
    }
}