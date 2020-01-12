package myin.phone.views.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;
import myin.phone.R;
import myin.phone.data.app.HomeApp;
import myin.phone.data.BaseAppDiffCallback;
import myin.phone.data.app.HomeAppRepository;
import myin.phone.list.NoScrollLinearLayout;
import myin.phone.list.TextListAdapter;

import javax.inject.Inject;

public class HomeActivity extends AppCompatActivity {

    public static final int REQ_APPS_CHANGED = 1;

    private TextListAdapter<HomeApp> appAdapter;

    @Inject
    HomeAppRepository homeAppRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        appAdapter = new TextListAdapter<>(new BaseAppDiffCallback(), getResources().getDimensionPixelSize(R.dimen.title_size));
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

}
