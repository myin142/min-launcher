package io.github.myin.phone.config;

import android.app.Application;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import io.github.myin.phone.views.home.HomeBottom;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        DatabaseModule.class,
        ActivityModule.class
})
public interface InjectComponent extends AndroidInjector<MainApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        InjectComponent build();
    }

    @Override
    void inject(MainApplication instance);
    void inject(HomeBottom homeBottom);
}
