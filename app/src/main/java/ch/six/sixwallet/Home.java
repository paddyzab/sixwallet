package ch.six.sixwallet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.six.sixwallet.backend.ApiProvider;
import ch.six.sixwallet.backend.SixApi;
import ch.six.sixwallet.backend.models.Balance;
import ch.six.sixwallet.backend.models.RequestTransaction;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class Home extends Activity {


    @OnClick(R.id.buttonSendRequest)
    public void sendRequest() {
        sixApi.createPaymentRequest("aplhdffRBBqjljPLAyMVcFBh9jTbh85f", createRequestTransaction(),
                new Callback<Object>() {
                    @Override
                    public void success(Object o, Response response) {
                        Log.d(Home.class.getSimpleName(), response.toString());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(Home.class.getSimpleName(), error.toString());
                    }
                });
    }

    private RequestTransaction createRequestTransaction() {
        final RequestTransaction requestTransaction = new RequestTransaction();
        requestTransaction.setAmount("100");
        requestTransaction.setComment("Give me moneyz");
        requestTransaction.setPhoneNumber("+41796845634");
        return requestTransaction;
    }

    private SixApi sixApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ApiProvider apiProvider = new ApiProvider();
        sixApi = apiProvider.getSixApi();

        ButterKnife.inject(this);

        // TODO remove this hardcoded!
        sixApi.getCurrentBalance("aplhdffRBBqjljPLAyMVcFBh9jTbh85f").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Balance>() {
                                                                         @Override
                                                                         public void call(
                                                                                 Balance balance) {
                                                                             Log.d(Home.class
                                                                                             .getSimpleName(),
                                                                                     "Value passed: "
                                                                                             + String
                                                                                             .valueOf(
                                                                                                     balance.getBalance()));
                                                                         }
                                                                     },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.d(Home.class.getSimpleName(),
                                "Yikes! we got an error: " + throwable.toString());
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
