package myin.phone.modules;

import android.app.Application;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import myin.phone.MainApplication;
import myin.phone.database.DatabaseModule;

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
}
