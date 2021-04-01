package io.github.myin.phone.views.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerFragment;
import io.github.myin.phone.R;
import io.github.myin.phone.data.tool.HomeToolRepository;
import io.github.myin.phone.list.NoScrollLinearLayout;
import io.github.myin.phone.views.settings.toolbar.ManageToolsAdapter;

import javax.inject.Inject;

public class HomeBottom extends DaggerFragment {

    private ManageToolsAdapter toolsAdapter;

    @Inject
    HomeToolRepository toolRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolsAdapter = new ManageToolsAdapter(requireActivity().getPackageManager());
        toolsAdapter.setOnItemClick(tool -> startActivity(tool.getActivityIntent()));

        toolRepository.getHomeToolSorted().observe(this, toolsAdapter::submitList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_bottom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView appsView = view.findViewById(R.id.tools_list);
        appsView.setLayoutManager(new NoScrollLinearLayout(getActivity(), LinearLayoutManager.HORIZONTAL));
        appsView.setAdapter(toolsAdapter);
    }
}
