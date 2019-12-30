package myin.phone.utils;

import android.content.Intent;

public class IntentBuilder {

    private Intent intent;

    public static IntentBuilder builder() {
        return new IntentBuilder();
    }

    public IntentBuilder() {
        intent = new Intent();
    }

    public IntentBuilder put(String key, String value) {
        intent.putExtra(key, value);
        return this;
    }

    public IntentBuilder put(String key, boolean value) {
        intent.putExtra(key, value);
        return this;
    }

    public Intent build() {
        return intent;
    }

}
