package io.github.myin.phone.views.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;
import io.github.myin.phone.R;
import io.github.myin.phone.data.BaseAppDiffCallback;
import io.github.myin.phone.data.tool.HomeTool;
import io.github.myin.phone.data.tool.HomeToolRepository;
import io.github.myin.phone.list.NoScrollLinearLayout;
import io.github.myin.phone.list.TextListAdapter;

import javax.inject.Inject;

public class HomeBottom extends Fragment {

    private TextListAdapter<HomeTool> toolAdapter;

    @Inject
    HomeToolRepository homeToolRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        toolAdapter = new TextListAdapter<>(new BaseAppDiffCallback<>());
        toolAdapter.setOnItemClickListener(homeTool -> {
            Intent appIntent = homeTool.getActivityIntent();
            startActivity(appIntent);
        });

        homeToolRepository.getHomeToolSorted().observe(this, appList -> {
            toolAdapter.submitList(appList);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_bottom, container, false);

        if (view != null) {
            RecyclerView appsView = view.findViewById(R.id.apps_list);
            appsView.setLayoutManager(new NoScrollLinearLayout(getContext(), LinearLayoutManager.HORIZONTAL));
            appsView.setAdapter(toolAdapter);
        }

        return view;
    }

}
