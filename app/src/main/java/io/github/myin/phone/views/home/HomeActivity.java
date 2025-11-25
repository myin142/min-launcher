package io.github.myin.phone.views.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
import io.github.myin.phone.data.todo.TodoItem;
import io.github.myin.phone.data.todo.TodoItemDiffCallback;
import io.github.myin.phone.data.todo.TodoItemRepository;
import io.github.myin.phone.list.NoScrollLinearLayout;
import io.github.myin.phone.list.TextListAdapter;
import io.github.myin.phone.utils.FeaturePreference;
import io.github.myin.phone.views.SelectAppActivity;
import io.github.myin.phone.views.apps.AppsList;
import io.github.myin.phone.views.settings.Settings;
import io.github.myin.phone.views.todo.TodoActivity;

import javax.inject.Inject;

public class HomeActivity extends SelectAppActivity {

    public static final int REQ_APPS_CHANGED = 1;

    private static final int REQ_OPEN_APP = 3;

    private static final int APPS_SWIPE_DISTANCE = 200;
    private static final int SETTINGS_SWIPE_DISTANCE = 800;
    private static final int TODO_SWIPE_DISTANCE = 200;
    private float swipeStartY;
    private float swipeStartX;

    private TextListAdapter<HomeApp> appAdapter;
    private TextListAdapter<CalendarEvent> calendarAdapter;
    private TextListAdapter<TodoItem> todoAdapter;
    private RecyclerView calendarView;
    private RecyclerView todoView;

    private View eventList;
    private View todoList;
    private TextView bottomAction;

    private View homeTop;
    private View root;

    @Inject
    HomeAppRepository homeAppRepository;
    @Inject
    AppSettingRepository appSettingRepository;
    @Inject
    TodoItemRepository todoItemRepository;

    // TODO: don't understand inject anymore, fix this another time
    CalendarService calendarService = new CalendarService();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        root = findViewById(R.id.root);
        homeTop = findViewById(R.id.home_top);

        eventList = findViewById(R.id.event_list);
        todoList = findViewById(R.id.todo_list);
        bottomAction = findViewById(R.id.home_bottom_action);

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

        startIntro.setVisibility(homeAppRepository.countHomeApps() > 0 ? View.GONE : View.VISIBLE);
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

        todoAdapter = new TextListAdapter<>(new TodoItemDiffCallback(), getResources().getDimensionPixelSize(R.dimen.note_size), true);
        todoAdapter.setDisplayFunction(item -> item.title);
        todoView = findViewById(R.id.todo_list);
        todoView.setLayoutManager(new NoScrollLinearLayout(this));
        todoView.setAdapter(todoAdapter);

        TodoTouchHelper touchHelper = new TodoTouchHelper(todoAdapter, todoItemRepository);
        touchHelper.attachToRecyclerView(todoView);

        todoItemRepository.getAll().observe(this, list -> todoAdapter.submitList(list));
        showTodoList(FeaturePreference.isHomeShowTodo());

        FeaturePreference.addObserver(() -> {
            boolean showTodoButton = FeaturePreference.isFeatureEnabled(SharedConst.PREF_SHOW_TODO_BUTTON);
            bottomAction.setVisibility(showTodoButton ? View.VISIBLE : View.GONE);
        });
    }

    private void showTodoList(boolean show) {
        eventList.setVisibility(show ? View.GONE : View.VISIBLE);
        todoList.setVisibility(show ? View.VISIBLE : View.GONE);

        var txt = getString(show ? R.string.calendar : R.string.todo);
        if (!show) {
            txt += " (" + todoAdapter.getItemCount() + ")";
        }
        bottomAction.setText(txt);
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

        boolean showTodo = FeaturePreference.isFeatureEnabled(SharedConst.PREF_HOME_SHOW_TODO);
        showTodoList(showTodo);

        root.setLayoutDirection(FeaturePreference.getLayoutDirection().getValue());
        calendarView.setLayoutDirection(FeaturePreference.getLayoutDirection().flip().getValue());
        todoView.setLayoutDirection(FeaturePreference.getLayoutDirection().flip().getValue());
        loadCalendarEvents();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN -> {
                swipeStartY = event.getY();
                swipeStartX = event.getX();
            }
            case MotionEvent.ACTION_UP -> {
                final float deltaY = swipeStartY - event.getY();
                final float deltaX = swipeStartX - event.getX();
                if (deltaY > APPS_SWIPE_DISTANCE) {
                    openAppsList();
                } else if (-deltaY > SETTINGS_SWIPE_DISTANCE) {
                    openSettings();
                } else if (FeaturePreference.isHomeShowTodo()) {
                    final var isLeft = FeaturePreference.getLayoutDirection() == FeaturePreference.LayoutDirection.LEFT;
                    if (isLeft && deltaX > TODO_SWIPE_DISTANCE || !isLeft && -deltaX > TODO_SWIPE_DISTANCE) {
                        openTodo();
                    }
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

    private void openTodo() {
        Intent appsIntent = new Intent(this, TodoActivity.class);
        startActivityForResult(appsIntent, HomeActivity.REQ_APPS_CHANGED);
    }

    public void onHomeBottomActionClicked(View v) {
        boolean showToDo = eventList.getVisibility() == View.VISIBLE;
        showTodoList(showToDo);
        FeaturePreference.setHomeShowTodo(showToDo);
    }

}
