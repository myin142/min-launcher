package io.github.myin.phone.config;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.github.myin.phone.views.apps.AppsList;
import io.github.myin.phone.views.home.HomeActivity;
import io.github.myin.phone.views.home.HomeBottom;
import io.github.myin.phone.views.settings.apps.ManageAppsActivity;
import io.github.myin.phone.views.settings.toolbar.ManageToolsActivity;
import io.github.myin.phone.views.todo.TodoActivity;

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

    @ContributesAndroidInjector
    abstract AppsList appsList();

    @ContributesAndroidInjector
    abstract TodoActivity todoActivity();
}
