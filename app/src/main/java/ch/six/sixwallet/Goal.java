package ch.six.sixwallet;

import android.content.Context;
import android.content.SharedPreferences;

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
        android.content.SharedPreferences pref = ctx.getSharedPreferences("ch.six.sixwallet", Context.MODE_PRIVATE);
        this.name = pref.getString("name", null);
        this.phoneNumber = pref.getString("phoneNumber", null);
        this.api = pref.getString("api", null);

        if (name.equals("") || phoneNumber.equals("") || api.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public void save(Context ctx) {
        android.content.SharedPreferences pref = ctx.getSharedPreferences("ch.six.sixwallet", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("name", name);
        edit.putString("phoneNumber", phoneNumber);
        edit.putString("api", api);
        edit.commit();
    }
}
