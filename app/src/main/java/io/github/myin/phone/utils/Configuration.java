package io.github.myin.phone.utils;

import android.content.Context;
import android.os.Build;

import java.util.Locale;

public class Configuration {

    public static Locale getCurrentLocale(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return ctx.getResources().getConfiguration().getLocales().get(0);
        } else {
            // noinspection deprecation
            return ctx.getResources().getConfiguration().locale;
        }
    }

}
