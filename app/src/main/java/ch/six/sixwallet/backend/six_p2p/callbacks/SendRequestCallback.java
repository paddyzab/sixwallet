package ch.six.sixwallet.backend.six_p2p.callbacks;


import android.util.Log;

import ch.six.sixwallet.Home;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SendRequestCallback implements Callback {
    @Override
    public void success(Object o, Response response) {
        Log.d(Home.class.getSimpleName(), response.toString());
    }

    @Override
    public void failure(RetrofitError error) {
        Log.d(Home.class.getSimpleName(), error.toString());
    }
}
