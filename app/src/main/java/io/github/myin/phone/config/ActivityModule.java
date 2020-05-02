package io.github.myin.phone.config;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.github.myin.phone.views.home.HomeActivity;
import io.github.myin.phone.views.home.HomeBottom;
import io.github.myin.phone.views.settings.apps.ManageAppsActivity;
import io.github.myin.phone.views.settings.toolbar.ManageToolsActivity;

@Module(includes = DatabaseModule.class)
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract HomeActivity homeActivity();

    @ContributesAndroidInjector
    abstract HomeBottom homeBottom();

    @ContributesAndroidInjector
    abstract ManageAppsActivity editAppsActivity();

    @ContributesAndroidInjector
    abstract ManageToolsActivity manageToolsActivity();

}
