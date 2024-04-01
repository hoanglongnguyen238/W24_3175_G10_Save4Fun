package com.example.save4fun.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.save4fun.R;
import com.example.save4fun.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.ProductViewHolder> {

    List<Product> products = new ArrayList<>();
    PopularProductAdapter.OnAddOrRemoveFavouriteItemClickListener onAddOrRemoveFavouriteItemClickListener;

    public PopularProductAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_product_by_category, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(productView);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.textViewProductName.setText(products.get(position).getName());

        DecimalFormat df = new DecimalFormat("$0.00");
        holder.textViewProductPrice.setText(df.format(products.get(position).getPrice()));

        byte[] decodedString = Base64.decode(products.get(position).getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imageViewProduct.setImageBitmap(decodedByte);

        if(products.get(position).getIsFavourite()) {
            holder.imageViewFavouriteIcon.setImageResource(R.drawable.ic_favorited);
        } else {
            holder.imageViewFavouriteIcon.setImageResource(R.drawable.ic_favorite);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void updateData(List<Product> newProducts) {
        products.clear();
        products.addAll(newProducts);
        notifyDataSetChanged();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProductName, textViewProductPrice;
        ImageView imageViewFavouriteIcon, imageViewProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);

            imageViewFavouriteIcon = itemView.findViewById(R.id.imageViewFavourite);
            imageViewFavouriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onAddOrRemoveFavouriteItemClickListener != null) {
                        onAddOrRemoveFavouriteItemClickListener.onAddOrRemoveFavouriteItemClick(getAdapterPosition());
                    }
                }
            });

            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
        }
    }

    public PopularProductAdapter.OnAddOrRemoveFavouriteItemClickListener getOnAddOrRemoveFavouriteItemClickListener() {
        return onAddOrRemoveFavouriteItemClickListener;
    }

    public void setOnAddOrRemoveFavouriteItemClickListener(PopularProductAdapter.OnAddOrRemoveFavouriteItemClickListener onAddOrRemoveFavouriteItemClickListener) {
        this.onAddOrRemoveFavouriteItemClickListener = onAddOrRemoveFavouriteItemClickListener;
    }

    public interface OnAddOrRemoveFavouriteItemClickListener {
        void onAddOrRemoveFavouriteItemClick(int position);
    }
}