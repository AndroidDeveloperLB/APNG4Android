package com.github.penfeizhou.animation.demo;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.github.penfeizhou.animation.apng.APNGDrawable;
import com.github.penfeizhou.animation.awebpencoder.WebPEncoder;
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
public class EncoderTestActivity extends AppCompatActivity {
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
        final ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = 50;
        layoutParams.topMargin = 50;
        linearLayout.addView(imageView, layoutParams);

        new Thread(new Runnable() {
            @Override
            public void run() {

                final byte[] ret = WebPEncoder.fromDecoder(
                        APNGDrawable.fromAsset(EncoderTestActivity.this,
                                "test2.png").getFrameSeqDecoder()).build();
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(imageView)
                                .load(ret)
                                .into(imageView);
                    }
                });
            }
        }).start();
    }
}
