package com.github.penfeizhou.animation.avif;


import android.content.Context;

import androidx.annotation.NonNull;

import com.github.penfeizhou.animation.FrameAnimationDrawable;
import com.github.penfeizhou.animation.avif.decode.AVIFDecoder;
import com.github.penfeizhou.animation.decode.FrameSeqDecoder;
import com.github.penfeizhou.animation.loader.AssetStreamLoader;
import com.github.penfeizhou.animation.loader.FileLoader;
import com.github.penfeizhou.animation.loader.Loader;
import com.github.penfeizhou.animation.loader.ResourceStreamLoader;

/**
 * @Description: AVIFDrawable
 * @Author: pengfei.zhou
 * @CreateDate: 2019/3/27
 */
public class AVIFDrawable extends FrameAnimationDrawable<AVIFDecoder> {
    public AVIFDrawable(@NonNull Loader provider) {
        super(provider);
    }

    public AVIFDrawable(@NonNull AVIFDecoder decoder) {
        super(decoder);
    }

    @NonNull
    @Override
    protected AVIFDecoder createFrameSeqDecoder(Loader streamLoader, FrameSeqDecoder.RenderListener listener) {
        return new AVIFDecoder(streamLoader, listener);
    }


    @NonNull
    public static AVIFDrawable fromAsset(@NonNull Context context, String assetPath) {
        AssetStreamLoader assetStreamLoader = new AssetStreamLoader(context, assetPath);
        return new AVIFDrawable(assetStreamLoader);
    }

    @NonNull
    public static AVIFDrawable fromFile(@NonNull String filePath) {
        FileLoader fileLoader = new FileLoader(filePath);
        return new AVIFDrawable(fileLoader);
    }

    @NonNull
    public static AVIFDrawable fromResource(@NonNull Context context, int resId) {
        ResourceStreamLoader resourceStreamLoader = new ResourceStreamLoader(context, resId);
        return new AVIFDrawable(resourceStreamLoader);
    }
}
