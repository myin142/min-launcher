package myin.phone.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import myin.phone.R;
import myin.phone.settings.Settings;

public class HomeBottom extends Fragment {

    private ImageView settingsIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_bottom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingsIcon = view.findViewById(R.id.settings);
        settingsIcon.setOnClickListener((View v) -> openSettings());
    }

    public void openSettings() {
        Intent appsIntent = new Intent(getContext(), Settings.class);
        startActivityForResult(appsIntent, 0);
    }

}
