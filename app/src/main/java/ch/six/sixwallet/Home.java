package ch.six.sixwallet;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.jackson.JacksonFactory;

import com.pascalwelsch.holocircularprogressbar.HoloCircularProgressBar;
import com.wuman.android.auth.AuthorizationFlow;
import com.wuman.android.auth.AuthorizationUIController;
import com.wuman.android.auth.DialogFragmentController;
import com.wuman.android.auth.OAuthManager;
import com.wuman.android.auth.oauth2.store.SharedPreferencesCredentialStore;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ch.six.sixwallet.activities.InsertActivity;
import ch.six.sixwallet.activities.ListPaymentActivity;
import ch.six.sixwallet.activities.RegistrationActivity;
import ch.six.sixwallet.backend.ApiProvider;
import ch.six.sixwallet.backend.runkeeper.RunKeeperApi;
import ch.six.sixwallet.backend.runkeeper.actions.UpdateFitnessActivityPageAction;
import ch.six.sixwallet.backend.runkeeper.callbacks.RunkeeperOauthCallback;
import ch.six.sixwallet.backend.six_p2p.SixApi;
import ch.six.sixwallet.backend.six_p2p.actions.UpdateBalanceAction;
import ch.six.sixwallet.backend.six_p2p.actions.UpdateTransactionsAction;
import ch.six.sixwallet.backend.six_p2p.models.PaymentActivity;
import ch.six.sixwallet.storage.SharedPreferencesKeyValueStorage;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Home extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    public final static String USER_TOKEN = "aplhdffRBBqjljPLAyMVcFBh9jTbh85f";
    public final static String CURRENT_USER = "_currentUser";

    private SixApi sixApi;
    private RunKeeperApi runKeeperApi;
    private SharedPreferencesKeyValueStorage keyValueStorage;
    private PaymentController paymentController;

    private final int TWO_SECONDS = 2000; // in milis

    @InjectView(R.id.goalView)
    public GoalView mGoalView;

    @InjectView(R.id.textViewBalance)
    public TextView mTextViewBalance;


    @InjectView(R.id.today)
    public TextView mTextViewToday;

    @InjectView(R.id.toGoal)
    public TextView mTextViewToGoal;

    @InjectView(R.id.progressBar)
    public HoloCircularProgressBar mProgressBar;

    @InjectView(R.id.activity_main_swipe_refresh_layout)
    public SwipeRefreshLayout mSwipeLayout;

//    @OnClick(R.id.button_insertGoal)
//    public void insertGoal() {
//        Intent intent = new Intent(Home.this, InsertActivity.class);
//        startActivity(intent);
//    }


    @OnClick(R.id.progressBar)
    public void launchListPaymentActivity() {
        Intent intent = new Intent(Home.this, ListPaymentActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.inject(this);
        mSwipeLayout.setOnRefreshListener(this);

        createApis();

        final UpdateBalanceAction updateBalanceAction = new UpdateBalanceAction(mTextViewBalance);
        final UpdateTransactionsAction updateTransactionsAction = new UpdateTransactionsAction();
        final UpdateFitnessActivityPageAction updateFitnessActivityPageAction
                = new UpdateFitnessActivityPageAction();
        sixApi.getCurrentBalance(USER_TOKEN).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(updateBalanceAction);

        sixApi.getTransactions(USER_TOKEN).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(updateTransactionsAction);

        sixApi.getUserActivities(USER_TOKEN,
                new Callback<List<PaymentActivity>>() {
                    @Override
                    public void success(List<PaymentActivity> activities,
                            Response response) {
                        Log.d(Home.class.getSimpleName(), "success");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(Home.class.getSimpleName(), "failure");
                    }
                });

        final SharedPreferencesCredentialStore credentialStore =
                new SharedPreferencesCredentialStore(Home.this,
                        "runKeeperStorage", new JacksonFactory());

        keyValueStorage =
                new SharedPreferencesKeyValueStorage(this,
                        SharedPreferencesKeyValueStorage.KV_STORAGE);

        final OAuthManager oauth = getOAuthManager(credentialStore);
        final RunkeeperOauthCallback runKeeperOauthCallback = new RunkeeperOauthCallback(
                credentialStore,
                runKeeperApi, updateFitnessActivityPageAction, keyValueStorage);
        oauth.authorizeExplicitly(CURRENT_USER, runKeeperOauthCallback, new Handler());

        paymentController = new PaymentController(sixApi, runKeeperApi,
                keyValueStorage, mTextViewToday, mTextViewToGoal, mProgressBar);
    }

    private void createApis() {
        final ApiProvider apiProvider = new ApiProvider();
        sixApi = apiProvider.getSixApi();
        runKeeperApi = apiProvider.getRunKeeperApi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoalView.refresh();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            final Intent intent = new Intent(Home.this, RegistrationActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_insert) {
            final Intent intent = new Intent(Home.this, InsertActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateCounterController();
                mSwipeLayout.setRefreshing(false);
            }
        }, TWO_SECONDS);
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

    private void updateCounterController() {
        if (StringUtils.isNotEmpty(
                keyValueStorage.getString(SharedPreferencesKeyValueStorage.RUN_KEEPER_TOKEN_KEY))) {
            paymentController.updateDistanceCounter();
        }
    }
}