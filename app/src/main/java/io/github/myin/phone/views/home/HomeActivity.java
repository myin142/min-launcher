package io.github.myin.phone.views.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.invoke.VarHandle;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;

import dagger.android.AndroidInjection;
import io.github.myin.phone.R;
import io.github.myin.phone.SharedConst;
import io.github.myin.phone.data.app.HomeApp;
import io.github.myin.phone.data.BaseAppDiffCallback;
import io.github.myin.phone.data.app.HomeAppRepository;
import io.github.myin.phone.data.calendar.CalendarDiffCallback;
import io.github.myin.phone.data.calendar.CalendarEvent;
import io.github.myin.phone.data.calendar.CalendarService;
import io.github.myin.phone.data.setting.AppSettingRepository;
import io.github.myin.phone.list.NoScrollLinearLayout;
import io.github.myin.phone.list.TextListAdapter;
import io.github.myin.phone.utils.FeaturePreference;
import io.github.myin.phone.views.SelectAppActivity;
import io.github.myin.phone.views.apps.AppsList;
import io.github.myin.phone.views.settings.Settings;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class HomeActivity extends SelectAppActivity {

    public static final int REQ_APPS_CHANGED = 1;

    private static final int REQ_OPEN_APP = 3;

    private static final int APPS_SWIPE_DISTANCE = 150;
    private static final int SETTINGS_SWIPE_DISTANCE = 800;
    private float swipeStartY;

    private TextListAdapter<HomeApp> appAdapter;
    private TextListAdapter<CalendarEvent> calendarAdapter;
    private RecyclerView calendarView;

    private View homeTop;
    private View root;

    @Inject
    HomeAppRepository homeAppRepository;
    @Inject
    AppSettingRepository appSettingRepository;

    // TODO: don't understand inject anymore, fix this another time
    CalendarService calendarService = new CalendarService();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        root = findViewById(R.id.root);
        homeTop = findViewById(R.id.home_top);

        appAdapter = new TextListAdapter<>(new BaseAppDiffCallback<>(), getResources().getDimensionPixelSize(R.dimen.title_size));
        appAdapter.setDisplayFunction(app -> {
            final var appSetting = appSettingRepository.getOneByPackageAndClass(app.packageName, app.className);
            return Optional.ofNullable(appSetting.getCustomName())
                    .filter(x -> !x.isBlank())
                    .orElse(app.toString());
        });
        appAdapter.setOnItemClickListener(homeApp -> {
            Intent appIntent = homeApp.getActivityIntent();
            startActivity(appIntent);
        });

        View startIntro = findViewById(R.id.start_intro);
        homeAppRepository.getHomeAppsSorted().observe(this, appList -> {
            startIntro.setVisibility(appList.isEmpty() ? View.VISIBLE : View.GONE);
            appAdapter.submitList(appList);
        });

        RecyclerView appsView = findViewById(R.id.apps_list);
        appsView.setLayoutManager(new NoScrollLinearLayout(this));
        appsView.setAdapter(appAdapter);

        calendarAdapter = new TextListAdapter<>(new CalendarDiffCallback(), getResources().getDimensionPixelSize(R.dimen.note_size), true);
        calendarAdapter.setDisplayFunction(ev -> {
            final var format = FeaturePreference.getDateTimeFormatter(getApplicationContext());
            return format.format(ev.getDate()) + "\n" + ev.getTitle();
        });
        calendarView = findViewById(R.id.event_list);
        calendarView.setLayoutManager(new NoScrollLinearLayout(this));
        calendarView.setAdapter(calendarAdapter);
        loadCalendarEvents();
    }

    private void loadCalendarEvents() {
        final var events = calendarService.readCalendarEvent(this, ZonedDateTime.now().minusDays(1), Duration.ofDays(30), 8);
        calendarAdapter.submitList(events);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();

        boolean showDate = FeaturePreference.isFeatureEnabled(SharedConst.PREF_SHOW_DATE_FEATURE);
        boolean showClock = FeaturePreference.isFeatureEnabled(SharedConst.PREF_SHOW_CLOCK_FEATURE);
        homeTop.setVisibility((!showDate && !showClock) ? View.GONE : View.VISIBLE);

        root.setLayoutDirection(FeaturePreference.getLayoutDirection().getValue());
        calendarView.setLayoutDirection(FeaturePreference.getLayoutDirection().flip().getValue());
        loadCalendarEvents();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN -> swipeStartY = event.getY();
            case MotionEvent.ACTION_UP -> {
                float deltaY = swipeStartY - event.getY();
                if (deltaY > APPS_SWIPE_DISTANCE) {
                    openAppsList();
                } else if (-deltaY > SETTINGS_SWIPE_DISTANCE) {
                    openSettings();
                }
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

    private void openSettings() {
        Intent appsIntent = new Intent(this, Settings.class);
        startActivityForResult(appsIntent, HomeActivity.REQ_APPS_CHANGED);
    }

}
