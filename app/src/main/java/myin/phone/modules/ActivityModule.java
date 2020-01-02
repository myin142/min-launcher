package myin.phone.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import myin.phone.database.DatabaseModule;
import myin.phone.home.HomeActivity;
import myin.phone.settings.ManageAppsActivity;

@Module(includes = DatabaseModule.class)
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract HomeActivity homeActivity();

    @ContributesAndroidInjector
    abstract ManageAppsActivity editAppsActivity();

}
