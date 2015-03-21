package ch.six.sixwallet;

import android.content.Context;
import android.content.SharedPreferences;

import ch.six.sixwallet.storage.SharedPreferencesKeyValueStorage;

/**
 * Created by jm on 20/03/15.
 */
public class Goal {

    private String name;
    private String phoneNumber;
    private String api;

    public Goal(String name, String phoneNumber, String api) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.api = api;

    }

    public Goal() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public boolean load(Context ctx) {
        android.content.SharedPreferences pref = ctx
                .getSharedPreferences("ch.six.sixwallet", Context.MODE_PRIVATE);
        this.name = pref.getString("name", SharedPreferencesKeyValueStorage.EMPTY_STRING);
        this.phoneNumber = pref
                .getString("phoneNumber", SharedPreferencesKeyValueStorage.EMPTY_STRING);
        this.api = pref.getString("api", SharedPreferencesKeyValueStorage.EMPTY_STRING);

        return (name.equals(SharedPreferencesKeyValueStorage.EMPTY_STRING) || phoneNumber
                .equals(SharedPreferencesKeyValueStorage.EMPTY_STRING) || api
                .equals(SharedPreferencesKeyValueStorage.EMPTY_STRING));
    }

    public void save(Context ctx) {
        android.content.SharedPreferences pref = ctx
                .getSharedPreferences("ch.six.sixwallet", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("name", name);
        edit.putString("phoneNumber", phoneNumber);
        edit.putString("api", api);
        edit.commit();
    }
}
