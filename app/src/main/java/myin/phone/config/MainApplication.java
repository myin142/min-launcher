package myin.phone.config;

import android.app.Application;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import myin.phone.config.DaggerInjectComponent;

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
    }
}
