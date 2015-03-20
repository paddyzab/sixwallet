package ch.six.sixwallet;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ch.six.sixwallet.backend.ApiProvider;
import ch.six.sixwallet.backend.runkeeper.RunKeeperApi;
import ch.six.sixwallet.backend.runkeeper.models.FitnessActivityFeedPage;
import ch.six.sixwallet.backend.six_p2p.SixApi;
import ch.six.sixwallet.backend.six_p2p.actions.UpdateBalanceAction;
import ch.six.sixwallet.backend.six_p2p.actions.UpdateTransactionsAction;
import ch.six.sixwallet.backend.six_p2p.callbacks.SendRequestCallback;
import ch.six.sixwallet.backend.six_p2p.models.RequestTransaction;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class Home extends Activity {

    private final static String USER_TOKEN = "aplhdffRBBqjljPLAyMVcFBh9jTbh85f";
    private final static String CURRENT_USER = "_currentUser";

    private SixApi sixApi;
    private RunKeeperApi runKeeperApi;
    private SendRequestCallback sendRequestCallback;
    private String runKeeperAccessToken;

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
        runKeeperApi = apiProvider.getRunKeeperApi();

        ButterKnife.inject(this);

        final UpdateBalanceAction updateBalanceAction = new UpdateBalanceAction(textViewBalance);
        sendRequestCallback = new SendRequestCallback();
        final UpdateTransactionsAction updateTransactionsAction = new UpdateTransactionsAction();

        sixApi.getCurrentBalance(USER_TOKEN).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(updateBalanceAction);

        sixApi.getTransactions(USER_TOKEN).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(updateTransactionsAction);

        final SharedPreferencesCredentialStore credentialStore =
                new SharedPreferencesCredentialStore(Home.this,
                        "runKeeperStorage", new JacksonFactory());

        final AuthorizationFlow flow = getAutorisationFlowBuilder(credentialStore).build();

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

        final OAuthManager oauth = new OAuthManager(flow, controller);

        oauth.authorizeExplicitly(CURRENT_USER,
                new OAuthManager.OAuthCallback<Credential>() {
                    @Override
                    public void run(OAuthManager.OAuthFuture<Credential> future) {
                        try {
                            final Credential credential = future.getResult();
                            runKeeperAccessToken = credential.getAccessToken();
                            credentialStore.store(CURRENT_USER, credential);

                            if (runKeeperAccessToken != null) {
                                runKeeperApi.getFitnessActivityFeedPage(
                                        createBearer(runKeeperAccessToken))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                        new Action1<FitnessActivityFeedPage>() {
                                            @Override
                                            public void call(
                                                    FitnessActivityFeedPage fitnessActivityFeedPage) {
                                                Log.d(Home.class.getSimpleName(),
                                                        " fitness activities: " +
                                                                fitnessActivityFeedPage
                                                                        .getFitnessActivities().get(
                                                                        0).getTotalDistance());
                                            }
                                        });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Handler());
    }

    private String createBearer(final String token) {
        return "Bearer " + token;
    }

    private AuthorizationFlow.Builder getAutorisationFlowBuilder(
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
