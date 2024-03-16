package com.example.save4fun.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.save4fun.R;
import com.example.save4fun.adapter.BannerImageAdapter;
import com.example.save4fun.model.Banner;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {

    private ViewPager viewPagerBanner;
    CircleIndicator circleIndicatorBanner;
    private BannerImageAdapter bannerImageAdapter;

    List<Banner> banners;

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

        return view;
    }

    private void loadBanner() {
        banners = new ArrayList<>();
        banners.add(new Banner(R.drawable.bn_sales_1));
        banners.add(new Banner(R.drawable.bn_sales_2));
        banners.add(new Banner(R.drawable.bn_sales_3));
        banners.add(new Banner(R.drawable.bn_sales_4));
    }
}