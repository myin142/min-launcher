package io.github.myin.phone.views.home;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.MotionEvent;
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
import io.github.myin.phone.utils.PreferenceSettings;
import io.github.myin.phone.views.SelectAppActivity;
import io.github.myin.phone.views.apps.AppsList;

import javax.inject.Inject;
import java.util.Set;

public class HomeActivity extends SelectAppActivity {

    public static final int REQ_APPS_CHANGED = 1;

    private static final int REQ_OPEN_APP = 3;

    private final int MIN_SWIPE_DISTANCE = 200;
    private float upSwipe, downSwipe;

    private TextListAdapter<HomeApp> appAdapter;
    private Set<String> features;

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

        homeAppRepository.getHomeAppsSorted().observe(this, appList -> {
            appAdapter.submitList(appList);
        });

        RecyclerView appsView = findViewById(R.id.apps_list);
        appsView.setLayoutManager(new NoScrollLinearLayout(this));
        appsView.setAdapter(appAdapter);
    }

    @Override
    protected void onResume() {
        features = PreferenceSettings.getFeatures(this);
        super.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (features.contains(SharedConst.PREF_OPEN_APP_FEATURE)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    upSwipe = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    downSwipe = event.getY();
                    float deltaY = upSwipe - downSwipe;
                    if (deltaY > MIN_SWIPE_DISTANCE) {
                        openAppsList();
                    }
                    break;
            }
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

}
