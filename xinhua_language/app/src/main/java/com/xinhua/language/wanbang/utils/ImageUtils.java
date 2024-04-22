package com.xinhua.language.wanbang.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ImageUtils {

    public static void loadWithBlur(Context context, int imageUrl, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();

        // 创建一个包含高斯模糊效果的转换器
        BlurTransformation blurTransformation = new BlurTransformation(25, 3);

        // 组合多个转换器，以应用多个效果
        MultiTransformation<Bitmap> transformations = new MultiTransformation<>(blurTransformation);

        // 使用Glide加载图片并应用转换器
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .apply(requestOptions)
                .transition(BitmapTransitionOptions.withCrossFade())
                .transform(transformations)
                .into(imageView);
    }
}

