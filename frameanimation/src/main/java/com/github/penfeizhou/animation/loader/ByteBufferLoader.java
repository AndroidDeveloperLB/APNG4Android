package com.github.penfeizhou.animation.loader;

import androidx.annotation.NonNull;

import com.github.penfeizhou.animation.io.ByteBufferReader;
import com.github.penfeizhou.animation.io.Reader;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @Description: ByteBufferLoader
 * @Author: pengfei.zhou
 * @CreateDate: 2019-05-15
 */
public abstract class ByteBufferLoader implements Loader {
    @NonNull
    public abstract ByteBuffer getByteBuffer();

    @NonNull
    @Override
    public Reader obtain() throws IOException {
        return new ByteBufferReader(getByteBuffer());
    }
}
