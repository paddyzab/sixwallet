package ch.six.sixwallet.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesKeyValueStorage {

    public final static String RUN_KEEPER_TOKEN_KEY = "_rk_token";
    public final static String DISTANCE_STORAGE_KEY = "_distance_storage";
    public final static String KV_STORAGE = "_kv_storage";
    private final SharedPreferences mSharedPreferences;
    private final String EMPTY_STRING = "";

    public SharedPreferencesKeyValueStorage(final Context context, final String fileName) {
        mSharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public void delete() {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public final void storeString(final String key, final String value) {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public final void storeFloat(final String key, final float value) {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public final String getString(final String key) {
        return mSharedPreferences.getString(key, EMPTY_STRING);
    }

    public final float getFloat(final String key) {
        return mSharedPreferences.getFloat(key, 0.0f);
    }
}
