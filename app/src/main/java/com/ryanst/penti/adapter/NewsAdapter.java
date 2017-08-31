package com.ryanst.penti.adapter;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.ryanst.penti.BR;
import com.ryanst.penti.R;
import com.ryanst.penti.bean.News;
import com.ryanst.penti.ui.DetailNewsActivity;
import com.ryanst.penti.util.AndroidScreenUtil;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by zhengjuntong on 7/22/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Activity activity;
    private String type;
    private List<News> newsList;
    private final int TYPE_TEXT = 0;
    private final int TYPE_ONE_IMAGE = 1;
    private final int TYPE_THREE_IMAGES = 2;
    private int lastPosition = -1;

    public NewsAdapter(FragmentActivity activity, List<News> newsList, String type) {
        this.activity = activity;
        this.newsList = newsList;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        NewsItemHolder holder;
        ViewDataBinding binding;

        if (viewType == TYPE_ONE_IMAGE) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_news_one_image, parent, false);
        } else if (viewType == TYPE_THREE_IMAGES) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_news_three_images, parent, false);
        } else {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_news_text, parent, false);
        }

        holder = new NewsItemHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
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

        TextView title = (TextView) holder.itemView.findViewById(R.id.tv_title);
        title.setText(newsList.get(position).getTitle());

        View itemView = holder.itemView;

        RxView.clicks(itemView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dealItemOnClick(position);
                    }
                });

        int itemViewType = getItemViewType(position);
        News news = newsList.get(position);
        List<String> images = news.getImages();

        switch (itemViewType) {
            case TYPE_TEXT:
                break;
            case TYPE_ONE_IMAGE:
                if (images != null && !images.isEmpty()) {

                    ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_image);

                    ViewGroup.LayoutParams imageLayoutParam = imageView.getLayoutParams();
                    int screenWidth = AndroidScreenUtil.getScreenMetrics(activity).x;
                    float density = AndroidScreenUtil.getScreenDensity(activity);
                    int imageHeight = (int) ((double) (screenWidth - 32 * density) / 3);
                    imageLayoutParam.height = imageHeight;
                    imageView.setLayoutParams(imageLayoutParam);
                }
                break;
            case TYPE_THREE_IMAGES:
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
                }
                break;
            default:
                break;
        }

        ((NewsItemHolder) holder).getBinding().setVariable(BR.news, newsList.get(position));
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
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }

    private static class NewsItemHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;

        public NewsItemHolder(View itemView) {
            super(itemView);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
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
