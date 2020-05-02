package io.github.myin.phone.views.settings;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.github.myin.phone.R;

import java.util.Arrays;
import java.util.List;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        List<Integer> textIds = Arrays.asList(R.id.about_source, R.id.about_img_credits);
        for (Integer textId : textIds) {
            TextView text = findViewById(textId);
            text.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

}
