package com.example.iteq;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context mContext;
    private List<Product> mProductList;
    private String name;
    private String post;

    public ProductAdapter(List<Product> productList, String name, String post) {
        mProductList = productList;
        this.name = name;
        this.post = post;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Product product = mProductList.get(position);
                Intent intent = new Intent(mContext, ProductActivity.class);
                intent.putExtra("number", product.getName());
                intent.putExtra("name", name);
                intent.putExtra("post", post);
//                mContext.startActivity(intent);
                //5.0界面切换动画
                mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((MainActivity) mContext).toBundle());
                //5.0共享元素切换动画
//                mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(((MainActivity) mContext), holder.productImage, "shared").toBundle());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Product product = mProductList.get(position);
        holder.productName.setText(product.getName());
        Glide.with(mContext).load(product.getImageId()).into(holder.productImage);

    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView productImage;
        TextView productName;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            productImage = itemView.findViewById(R.id.gd_image);
            productName = itemView.findViewById(R.id.gd_name);
        }
    }
}
