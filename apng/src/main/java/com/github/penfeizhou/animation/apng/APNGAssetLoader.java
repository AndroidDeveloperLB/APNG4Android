package com.github.penfeizhou.animation.apng;


import android.content.Context;

import androidx.annotation.NonNull;

import com.github.penfeizhou.animation.loader.AssetStreamLoader;

/**
 * @Author: pengfei.zhou
 * @CreateDate: 2019/3/28
 * @see com.github.penfeizhou.animation.loader.AssetStreamLoader use this insted
 */
@Deprecated
public class APNGAssetLoader extends AssetStreamLoader {

    public APNGAssetLoader(@NonNull Context context, String assetName) {
        super(context, assetName);
    }
}
