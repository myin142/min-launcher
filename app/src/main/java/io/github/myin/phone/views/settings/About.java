package io.github.myin.phone.views.settings;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.github.myin.phone.R;
import io.github.myin.phone.utils.FeaturePreference;

import java.util.Arrays;
import java.util.List;

public class About extends AppCompatActivity {

    private View root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(FeaturePreference.getThemeResourceId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        root = findViewById(R.id.root);

        TextView appInfo = findViewById(R.id.about_app);
        String info = getText(R.string.app_name).toString();
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            info += " - " + version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to get application version");
        }

        appInfo.setText(info);

        List<Integer> textIds = Arrays.asList(R.id.about_source, R.id.about_img_credits);
        for (Integer textId : textIds) {
            TextView text = findViewById(textId);
            text.setMovementMethod(LinkMovementMethod.getInstance());
            stripUnderlines(text);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
        root.setLayoutDirection(FeaturePreference.getLayoutDirection().getValue());
    }

    private void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    private static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }
        @Override public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

}
