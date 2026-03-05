package com.github.penfeizhou.animation.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.github.penfeizhou.animation.demo.databinding.ActivityApngListTestBinding;

public class APNGRecyclerViewTestActivity extends AppCompatActivity {
    private ActivityApngListTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApngListTestBinding.inflate(getLayoutInflater());
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
            binding.rv.setPadding(
                    binding.rv.getPaddingLeft(),
                    binding.rv.getPaddingTop(),
                    binding.rv.getPaddingRight(),
                    navigationBarsWithIme.bottom
            );
            return insets;
        });

        binding.rv.setLayoutManager(new GridLayoutManager(this, 3));
        binding.rv.setAdapter(new TestAdapter(this));
    }
}
