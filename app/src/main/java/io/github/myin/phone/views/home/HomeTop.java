package io.github.myin.phone.views.home;

import android.content.*;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.github.myin.phone.R;
import io.github.myin.phone.SharedConst;
import io.github.myin.phone.utils.FeaturePreference;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HomeTop extends Fragment {

    private DateTimeFormatter dateFormat;
    private DateTimeFormatter timeFormat;

    private TextView dateView;
    private TextView timeView;

    private BroadcastReceiver timeTickReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_top, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateView = view.findViewById(R.id.home_top_date);
        dateView.setOnClickListener((View v) -> openCalendar());

        timeView = view.findViewById(R.id.home_top_time);
        timeView.setOnClickListener((View v) -> openAlarm());

        FeaturePreference.addObserver(() -> timeFormat = FeaturePreference.getTimeFormatter(getContext()));
        dateFormat = FeaturePreference.getDateTimeFormatter(getContext());

        updateCurrentTime();
        timeTickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateCurrentTime();
            }
        };
    }

    private void updateVisibility() {
        setViewVisibility(dateView, SharedConst.PREF_SHOW_DATE_FEATURE);
        setViewVisibility(timeView, SharedConst.PREF_SHOW_CLOCK_FEATURE);
    }

    private void setViewVisibility(View view, String feature) {
        int visible = FeaturePreference.isFeatureEnabled(feature) ? View.VISIBLE : View.INVISIBLE;
        view.setVisibility(visible);
    }

    private void updateCurrentTime() {
        ZonedDateTime currentDate = ZonedDateTime.now();
        dateView.setText(dateFormat.format(currentDate));
        timeView.setText(timeFormat.format(currentDate));
    }

    @Override
    public void onResume() {
        super.onResume();
        updateVisibility();
        updateCurrentTime();
        getActivity().registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(timeTickReceiver);
    }

    private void openCalendar() {
        Intent calendar = new Intent(Intent.ACTION_MAIN);
        calendar.addCategory(Intent.CATEGORY_APP_CALENDAR);

        startActivity(calendar);
    }

    private void openAlarm() {
        Intent alarm = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        ComponentName componentName = alarm.resolveActivity(getActivity().getPackageManager());

        if (componentName != null) {
            alarm = getActivity().getPackageManager().getLaunchIntentForPackage(componentName.getPackageName());
        }

        startActivity(alarm);
    }
}
