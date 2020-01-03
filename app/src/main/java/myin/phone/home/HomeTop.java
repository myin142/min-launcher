package myin.phone.home;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import myin.phone.R;
import myin.phone.utils.Configuration;
import myin.phone.utils.Permission;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeTop extends Fragment {

    private DateFormat dateFormat;
    private DateFormat timeFormat;

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

        Locale locale = Configuration.getCurrentLocale(getContext());
        timeFormat = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, locale);
        dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, locale);

        updateCurrentTime();
        timeTickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateCurrentTime();
            }
        };
    }

    private void updateCurrentTime() {
        Date currentDate = new Date();
        dateView.setText(dateFormat.format(currentDate));
        timeView.setText(timeFormat.format(currentDate));
    }

    @Override
    public void onResume() {
        super.onResume();
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
        Permission.hasPermission(getActivity(), Manifest.permission.SET_ALARM, () -> {
            Intent alarm = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
            startActivity(alarm);
        });
    }
}
