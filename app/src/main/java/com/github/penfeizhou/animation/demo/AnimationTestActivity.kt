package com.github.penfeizhou.animation.demo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.penfeizhou.animation.apng.APNGDrawable;
import com.github.penfeizhou.animation.avif.AVIFDrawable;
import com.github.penfeizhou.animation.demo.databinding.ActivityApnglibBinding;
import com.github.penfeizhou.animation.gif.GifDrawable;
import com.github.penfeizhou.animation.webp.WebPDrawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


/**
 * @Description: 作用描述
 * @Author: pengfei.zhou
 * @CreateDate: 2019/3/29
 */
public class AnimationTestActivity extends AppCompatActivity {
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
        String[] files = getIntent().getStringArrayExtra("files");
        if (files != null) {
            for (String assetFile : files) {
                ImageView imageView = new ImageView(this);
                Drawable drawable = null;
                if (assetFile.endsWith("png")) {
                    drawable = APNGDrawable.fromAsset(this, assetFile);
                }
                if (assetFile.endsWith("webp")) {
                    drawable = WebPDrawable.fromAsset(this, assetFile);
                }
                if (assetFile.endsWith("gif")) {
                    drawable = GifDrawable.fromAsset(this, assetFile);
                }
                if (assetFile.endsWith("avif")) {
                    drawable = AVIFDrawable.fromAsset(this, assetFile);
                }
                imageView.setImageDrawable(drawable);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = 50;
                layoutParams.topMargin = 50;
                linearLayout.addView(imageView, layoutParams);
            }
        }
    }
}
