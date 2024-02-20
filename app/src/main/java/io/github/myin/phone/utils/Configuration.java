package io.github.myin.phone.utils;

import android.content.Context;
import android.os.Build;

import java.util.Locale;

public class Configuration {

    public static Locale getCurrentLocale(Context ctx) {
        return ctx.getResources().getConfiguration().getLocales().get(0);
    }

}
