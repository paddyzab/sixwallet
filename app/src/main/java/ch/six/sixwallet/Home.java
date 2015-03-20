package ch.six.sixwallet;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ch.six.sixwallet.backend.ApiProvider;
import ch.six.sixwallet.backend.six_p2p.SixApi;
import ch.six.sixwallet.backend.six_p2p.actions.UpdateBalanceAction;
import ch.six.sixwallet.backend.six_p2p.actions.UpdateTransactionsAction;
import ch.six.sixwallet.backend.six_p2p.callbacks.SendRequestCallback;
import ch.six.sixwallet.backend.six_p2p.models.RequestTransaction;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Home extends Activity {

    private final static String USER_TOKEN = "aplhdffRBBqjljPLAyMVcFBh9jTbh85f";

    private SixApi sixApi;
    private SendRequestCallback sendRequestCallback;

    @InjectView(R.id.textViewBalance)
    public TextView textViewBalance;

    @OnClick(R.id.buttonSendRequest)
    public void sendRequest() {
        sixApi.createPaymentRequest(USER_TOKEN,
                createRequestTransaction("100", "Give me reward please", "+41796845634"),
                sendRequestCallback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ApiProvider apiProvider = new ApiProvider();
        sixApi = apiProvider.getSixApi();

        ButterKnife.inject(this);

        final UpdateBalanceAction updateBalanceAction = new UpdateBalanceAction(textViewBalance);
        sendRequestCallback = new SendRequestCallback();
        final UpdateTransactionsAction updateTransactionsAction = new UpdateTransactionsAction();

        sixApi.getCurrentBalance(USER_TOKEN).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(updateBalanceAction);

        sixApi.getTransactions(USER_TOKEN).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(updateTransactionsAction);
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

    private RequestTransaction createRequestTransaction(final String amount, final String comment,
            final String number) {
        return new RequestTransaction()
                .setAmount(amount)
                .setComment(comment)
                .setPhoneNumber(number);
    }
}
