package com.ryanst.penti.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ryanst.penti.R;
import com.ryanst.penti.bean.News;
import com.ryanst.penti.ui.DetailNewsActivity;
import com.ryanst.penti.util.AndroidScreenUtil;

import java.util.List;

/**
 * Created by zhengjuntong on 7/22/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Activity activity;
    private List<News> newsList;
    private int TYPE_TEXT = 0;
    private int TYPE_ONE_IMAGE = 1;
    private int TYPE_THREE_IMAGES = 2;
    private int lastPosition = -1;

    public NewsAdapter(Activity activity, List<News> newsList) {
        this.activity = activity;
        this.newsList = newsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_TEXT) {
            return new TextViewHolder(activity.getLayoutInflater().inflate(R.layout.item_news_text, parent, false));
        } else if (viewType == TYPE_ONE_IMAGE) {
            return new OneImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_one_image, parent, false));
        } else if (viewType == TYPE_THREE_IMAGES) {
            return new ThreeImagesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_three_images, parent, false));
        }
        return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_text, parent, false));
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                    .anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        CardView cardView = (CardView) holder.itemView.getRootView().findViewById(R.id.cardView);
        cardView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        View itemView = holder.itemView;
        ((TextView) itemView.findViewById(R.id.tv_title)).setText(newsList.get(position).getTitle());
        ((TextView) itemView.findViewById(R.id.tv_date)).setText(newsList.get(position).getPosttime());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dealItemOnClick(position);
            }
        });

        if (holder instanceof OneImageViewHolder) {
            List<String> images = newsList.get(position).getImages();
            if (images != null && !images.isEmpty()) {

                ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_image);

                ViewGroup.LayoutParams imageLayoutParam = imageView.getLayoutParams();
                int screenWidth = AndroidScreenUtil.getScreenMetrics(activity).x;
                float density = AndroidScreenUtil.getScreenDensity(activity);
                int imageHeight = (int) ((double) (screenWidth - 32 * density) / 3);
                imageLayoutParam.height = imageHeight;
                imageView.setLayoutParams(imageLayoutParam);

                Glide.with(activity).load(images.get(0)).into(imageView);
            }
        } else if (holder instanceof ThreeImagesViewHolder) {
            List<String> images = newsList.get(position).getImages();
            if (images != null && !images.isEmpty() && images.size() == 3) {
                ImageView imageView1 = (ImageView) itemView.findViewById(R.id.iv_image_1);
                ImageView imageView2 = (ImageView) itemView.findViewById(R.id.iv_image_2);
                ImageView imageView3 = (ImageView) itemView.findViewById(R.id.iv_image_3);

                ViewGroup.LayoutParams imageLayoutParam1 = imageView1.getLayoutParams();
                ViewGroup.LayoutParams imageLayoutParam2 = imageView2.getLayoutParams();
                ViewGroup.LayoutParams imageLayoutParam3 = imageView3.getLayoutParams();

                int screenWidth = AndroidScreenUtil.getScreenMetrics(activity).x;
                float density = AndroidScreenUtil.getScreenDensity(activity);
                int imageHeight = (int) ((double) (screenWidth - 48 * density) / 3);
                imageLayoutParam1.height = imageHeight;
                imageLayoutParam2.height = imageHeight;
                imageLayoutParam3.height = imageHeight;

                imageView1.setLayoutParams(imageLayoutParam1);
                imageView2.setLayoutParams(imageLayoutParam2);
                imageView3.setLayoutParams(imageLayoutParam3);

                Glide.with(activity).load(images.get(0)).into(imageView1);
                Glide.with(activity).load(images.get(1)).into(imageView2);
                Glide.with(activity).load(images.get(2)).into(imageView3);
            }
        }

        CardView cardView = (CardView) holder.itemView.getRootView().findViewById(R.id.cardView);
        setAnimation(cardView, position);
    }

    @Override
    public int getItemCount() {
        if (newsList != null) {
            return newsList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        List<String> images = newsList.get(position).getImages();
        if (images == null || images.isEmpty()) {
            return TYPE_TEXT;
        } else if (images.size() == 1) {
            return TYPE_ONE_IMAGE;
        } else if (images.size() == 3) {
            return TYPE_THREE_IMAGES;
        }
        return TYPE_TEXT;
    }

    private void dealItemOnClick(int position) {
        String contentId = newsList.get(position).getContentID();
        Intent intent = new Intent(activity, DetailNewsActivity.class);
        intent.putExtra("contentId", contentId);
        activity.startActivity(intent);
    }

    private static class TextViewHolder extends RecyclerView.ViewHolder {

        public TextViewHolder(View itemView) {
            super(itemView);
        }

    }

    private static class OneImageViewHolder extends RecyclerView.ViewHolder {

        public OneImageViewHolder(View itemView) {
            super(itemView);
        }

    }

    private static class ThreeImagesViewHolder extends RecyclerView.ViewHolder {

        public ThreeImagesViewHolder(View itemView) {
            super(itemView);
        }

    }
}
