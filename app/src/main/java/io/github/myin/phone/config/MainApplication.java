package io.github.myin.phone.config;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import io.github.myin.phone.utils.FeaturePreference;

import javax.inject.Inject;

public class MainApplication extends Application implements HasAndroidInjector {

    @Inject
    public DispatchingAndroidInjector<Object> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Object> androidInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerInjectComponent.builder()
                .application(this)
                .build()
                .inject(this);

        registerPortraitOrientationCallback();
        FeaturePreference.init(this);
    }

    private void registerPortraitOrientationCallback() {
        registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallback(){
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });
    }
}
