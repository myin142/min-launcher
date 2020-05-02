package io.github.myin.phone.views.home;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;
import io.github.myin.phone.R;
import io.github.myin.phone.SharedConst;
import io.github.myin.phone.data.app.HomeApp;
import io.github.myin.phone.data.BaseAppDiffCallback;
import io.github.myin.phone.data.app.HomeAppRepository;
import io.github.myin.phone.list.NoScrollLinearLayout;
import io.github.myin.phone.list.TextListAdapter;
import io.github.myin.phone.utils.FeaturePreference;
import io.github.myin.phone.views.SelectAppActivity;
import io.github.myin.phone.views.apps.AppsList;
import io.github.myin.phone.views.settings.Settings;

import javax.inject.Inject;

public class HomeActivity extends SelectAppActivity {

    public static final int REQ_APPS_CHANGED = 1;

    private static final int REQ_OPEN_APP = 3;

    private final int MIN_SWIPE_DISTANCE = 150;
    private float upSwipe, downSwipe;

    private TextListAdapter<HomeApp> appAdapter;
    private View homeTop;

    @Inject
    HomeAppRepository homeAppRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        appAdapter = new TextListAdapter<>(new BaseAppDiffCallback<>(), getResources().getDimensionPixelSize(R.dimen.title_size));
        appAdapter.setOnItemClickListener(homeApp -> {
            Intent appIntent = homeApp.getActivityIntent();
            startActivity(appIntent);
        });

        homeTop = findViewById(R.id.home_top);

        homeAppRepository.getHomeAppsSorted().observe(this, appList -> {
            appAdapter.submitList(appList);
        });

        RecyclerView appsView = findViewById(R.id.apps_list);
        appsView.setLayoutManager(new NoScrollLinearLayout(this));
        appsView.setAdapter(appAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean showDate = FeaturePreference.isFeatureEnabled(SharedConst.PREF_SHOW_DATE_FEATURE);
        boolean showClock = FeaturePreference.isFeatureEnabled(SharedConst.PREF_SHOW_CLOCK_FEATURE);
        homeTop.setVisibility((!showDate && !showClock) ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                upSwipe = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                downSwipe = event.getY();
                float deltaY = upSwipe - downSwipe;
                if (deltaY > MIN_SWIPE_DISTANCE) {
                    if (FeaturePreference.isFeatureEnabled(SharedConst.PREF_OPEN_APP_FEATURE)) {
                        openAppsList();
                    }
                } else if (-deltaY > MIN_SWIPE_DISTANCE) {
                    openSettings();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onAppSelected(int requestCode, ResolveInfo info) {
        if (requestCode == REQ_OPEN_APP) {
            HomeApp app = resolveToHomeApp(info);
            startActivity(app.getActivityIntent());
        }
    }

    private void openAppsList() {
        Intent appsListIntent = new Intent(this, AppsList.class);
        startActivityForResult(appsListIntent, REQ_OPEN_APP);
    }

    private void openSettings() {
        Intent appsIntent = new Intent(this, Settings.class);
        startActivityForResult(appsIntent, HomeActivity.REQ_APPS_CHANGED);
    }

}
