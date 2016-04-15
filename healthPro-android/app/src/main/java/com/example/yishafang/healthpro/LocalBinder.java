package com.example.yishafang.healthpro;

import android.os.Binder;

import java.lang.ref.WeakReference;

/**
 * Created by yellowstar on 12/9/15.
 */
public class LocalBinder<S> extends Binder {
    private final WeakReference<S> mService;

    public LocalBinder(final S service) {
        mService = new WeakReference<>(service);
    }

    public S getService() {
        return mService.get();
    }
}
