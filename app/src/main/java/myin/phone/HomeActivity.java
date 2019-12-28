package myin.phone;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import myin.phone.apps.AppsList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("Result: " + requestCode + ", " + resultCode);
        if (data != null) {
            System.out.println("Data: " + data.getStringExtra(AppsList.SELECTED_APP));

            Intent appIntent = getPackageManager().getLaunchIntentForPackage(data.getStringExtra(AppsList.SELECTED_APP));
            ResolveInfo info = getPackageManager().resolveActivity(appIntent, 0);
            System.out.println("Name: " + info.loadLabel(getPackageManager()));

            startActivity(appIntent);
        }
    }
}
