package ch.six.sixwallet;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.jackson.JacksonFactory;

import com.wuman.android.auth.AuthorizationFlow;
import com.wuman.android.auth.AuthorizationUIController;
import com.wuman.android.auth.DialogFragmentController;
import com.wuman.android.auth.OAuthManager;
import com.wuman.android.auth.oauth2.store.SharedPreferencesCredentialStore;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ch.six.sixwallet.backend.ApiProvider;
import ch.six.sixwallet.backend.runkeeper.RunKeeperApi;
import ch.six.sixwallet.backend.runkeeper.actions.UpdateFitnessActivityPageAction;
import ch.six.sixwallet.backend.runkeeper.callbacks.RunKeeperOauthCallback;
import ch.six.sixwallet.backend.six_p2p.SixApi;
import ch.six.sixwallet.backend.six_p2p.actions.AllActivitiesAction;
import ch.six.sixwallet.backend.six_p2p.actions.UpdateBalanceAction;
import ch.six.sixwallet.backend.six_p2p.actions.UpdateTransactionsAction;
import ch.six.sixwallet.backend.six_p2p.callbacks.SendRequestCallback;
import ch.six.sixwallet.backend.six_p2p.models.RequestTransaction;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Home extends Activity {

    public final static String USER_TOKEN = "aplhdffRBBqjljPLAyMVcFBh9jTbh85f";
    public final static String CURRENT_USER = "_currentUser";

    private SixApi sixApi;
    private RunKeeperApi runKeeperApi;
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

        ButterKnife.inject(this);
        createApis();

        final UpdateBalanceAction updateBalanceAction = new UpdateBalanceAction(textViewBalance);
        sendRequestCallback = new SendRequestCallback();
        final UpdateTransactionsAction updateTransactionsAction = new UpdateTransactionsAction();
        final UpdateFitnessActivityPageAction updateFitnessActivityPageAction
                = new UpdateFitnessActivityPageAction();
        final AllActivitiesAction allActivitiesAction = new AllActivitiesAction();

        sixApi.getCurrentBalance(USER_TOKEN).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(updateBalanceAction);

        sixApi.getTransactions(USER_TOKEN).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(updateTransactionsAction);

        sixApi.getUserActivities(USER_TOKEN).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(allActivitiesAction);

        final SharedPreferencesCredentialStore credentialStore =
                new SharedPreferencesCredentialStore(Home.this,
                        "runKeeperStorage", new JacksonFactory());

        final OAuthManager oauth = getOAuthManager(credentialStore);
        final RunKeeperOauthCallback runKeeperOauthCallback = new RunKeeperOauthCallback(
                credentialStore,
                runKeeperApi, updateFitnessActivityPageAction);
        oauth.authorizeExplicitly(CURRENT_USER, runKeeperOauthCallback, new Handler());
    }

    private void createApis() {
        final ApiProvider apiProvider = new ApiProvider();
        sixApi = apiProvider.getSixApi();
        runKeeperApi = apiProvider.getRunKeeperApi();
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

    private AuthorizationFlow.Builder getAuthorisationFlowBuilder(
            SharedPreferencesCredentialStore credentialStore) {
        final AuthorizationFlow.Builder builder = new AuthorizationFlow.Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                AndroidHttp.newCompatibleTransport(),
                new JacksonFactory(),
                new GenericUrl(RunKeeperApi.accessTokenUrl),
                new ClientParametersAuthentication(RunKeeperApi.CLIENT_ID,
                        RunKeeperApi.CLIENT_SECRET),
                RunKeeperApi.CLIENT_ID,
                RunKeeperApi.authorisationUrl);
        builder.setCredentialStore(credentialStore);
        return builder;
    }

    private OAuthManager getOAuthManager(SharedPreferencesCredentialStore credentialStore) {
        final AuthorizationFlow flow = getAuthorisationFlowBuilder(credentialStore).build();
        final AuthorizationUIController controller =
                new DialogFragmentController(getFragmentManager()) {

                    @Override
                    public String getRedirectUri() throws IOException {
                        return "http://localhost/Callback";
                    }

                    @Override
                    public boolean isJavascriptEnabledForWebView() {
                        return true;
                    }

                };

        return new OAuthManager(flow, controller);
    }

    private RequestTransaction createRequestTransaction(final String amount, final String comment,
            final String number) {
        return new RequestTransaction()
                .setAmount(amount)
                .setComment(comment)
                .setPhoneNumber(number);
    }


}
