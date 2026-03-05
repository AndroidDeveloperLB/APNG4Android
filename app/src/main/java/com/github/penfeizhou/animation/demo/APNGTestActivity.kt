package com.github.penfeizhou.animation.demo;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.github.penfeizhou.animation.demo.databinding.ActivityApnglibBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * @Description: 作用描述
 * @Author: pengfei.zhou
 * @CreateDate: 2019/3/29
 */
public class APNGTestActivity extends AppCompatActivity {
    private ActivityApnglibBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApnglibBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets navigationBarsWithIme = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout() | WindowInsetsCompat.Type.ime());
            binding.appBarLayout.setPadding(
                    navigationBarsWithIme.left,
                    navigationBarsWithIme.top,
                    navigationBarsWithIme.right,
                    0
            );
            binding.getRoot().setPadding(navigationBarsWithIme.left, 0, navigationBarsWithIme.right, 0);
            binding.scrollView.setPadding(
                    binding.scrollView.getPaddingLeft(),
                    binding.scrollView.getPaddingTop(),
                    binding.scrollView.getPaddingRight(),
                    navigationBarsWithIme.bottom
            );
            return insets;
        });

        LinearLayout linearLayout = binding.layout;
        String[] urls = new String[]{
                "file:///android_asset/test.avif",
                "file:///android_asset/wheel.avif",
                "file:///android_asset/world-cup.avif",
                "file:///android_asset/apng_detail_guide.png",
                "file:///android_asset/1.gif",
                "file:///android_asset/2.gif",
                "file:///android_asset/3.gif",
                "file:///android_asset/4.gif",
                "file:///android_asset/5.gif",
                "file:///android_asset/1.webp",
                "file:///android_asset/2.webp",
        };
        for (String url : urls) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.bottomMargin = 50;
            layoutParams.topMargin = 50;
            linearLayout.addView(imageView, layoutParams);
            Glide.with(imageView)
                    .load(url)
//                    .set(AnimationDecoderOption.NO_ANIMATION_BOUNDS_MEASURE, true)
                    .into(imageView);
        }
    }
}
