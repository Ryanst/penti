package com.ryanst.penti.util;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by kevin on 16/3/11.
 */
public class ImageUtils {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    @BindingAdapter({"imageUrl", "error"})
    public static void loadImage(ImageView imageView, String url, Drawable error) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(imageView.getContext()).load(url).placeholder(error).into(imageView);
    }

    @BindingAdapter({"imageResId", "error"})
    public static void loadImage(ImageView imageView, int resId, Drawable error) {
        if (resId == 0) {
            return;
        }
        Glide.with(imageView.getContext()).load(resId).placeholder(error).into(imageView);
    }

    public static void loadImage(Activity activity, ImageView imageView, String url) {

        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed()) {
                return;
            }
        }

        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    public static void loadImage(Activity activity, ImageView imageView, int resId) {
        if (resId == 0) {
            return;
        }

        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed()) {
                return;
            }
        }

        Glide.with(imageView.getContext()).load(resId).into(imageView);
    }
}
