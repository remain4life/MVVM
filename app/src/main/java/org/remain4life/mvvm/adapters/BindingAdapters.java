package org.remain4life.mvvm.adapters;

import android.content.res.ColorStateList;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.ImageViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class BindingAdapters {
    private BindingAdapters() { }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        }
        else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("android:onClick")
    public static void setOnClickListener(View view, Runnable listener) {
        view.setOnClickListener(v -> listener.run());
    }

    @BindingAdapter("android:tint")
    public static void setTint(ImageView imageView, @ColorInt int color) {
        ImageViewCompat.setImageTintList(
                imageView,
                new ColorStateList(
                        new int[][]{
                                new int[]{}
                        },
                        new int[] {
                                color
                        }
                )
        );
    }

    @BindingAdapter("android:layout_width")
    public static void setWidth(View view, int width) {
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:layout_height")
    public static void setHeight(View view, int height) {
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }


    @BindingAdapter("imageUrl")
    public static void imageUrl(ImageView imageView, Uri uri) {
        Picasso.get()
                .load(uri)
                .into(imageView);
    }

    @BindingAdapter("onNavigationItemSelected")
    public static void setOnNavigationItemSelected(
            BottomNavigationView view, BottomNavigationView.OnNavigationItemSelectedListener listener) {
        view.setOnNavigationItemSelectedListener(listener);
    }

    @BindingAdapter("selectedItemPosition")
    public static void setSelectedItemPosition(
            BottomNavigationView view, int position) {
        view.setSelectedItemId(position);
    }
}

