package ch.six.sixwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ch.six.sixwallet.activities.InsertActivity;
import ch.six.sixwallet.activities.RegistrationActivity;
import ch.six.sixwallet.backend.ApiProvider;
import ch.six.sixwallet.backend.runkeeper.RunKeeperApi;
import ch.six.sixwallet.backend.runkeeper.actions.UpdateFitnessActivityPageAction;
import ch.six.sixwallet.backend.runkeeper.callbacks.RunKeeperOauthCallback;
import ch.six.sixwallet.backend.six_p2p.SixApi;
import ch.six.sixwallet.backend.six_p2p.actions.AllActivitiesAction;
import ch.six.sixwallet.backend.six_p2p.actions.UpdateBalanceAction;
import ch.six.sixwallet.backend.six_p2p.actions.UpdateTransactionsAction;
import ch.six.sixwallet.storage.SharedPreferencesKeyValueStorage;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Home extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeLayout;
    private GoalView mGoalView;

    public final static String USER_TOKEN = "aplhdffRBBqjljPLAyMVcFBh9jTbh85f";
    public final static String CURRENT_USER = "_currentUser";

    private SixApi sixApi;
    private RunKeeperApi runKeeperApi;
    private SharedPreferencesKeyValueStorage keyValueStorage;
    private PaymentController paymentController;

    @InjectView(R.id.textViewBalance)
    public TextView textViewBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ButterKnife.inject(this);
        createApis();

        final UpdateBalanceAction updateBalanceAction = new UpdateBalanceAction(textViewBalance);
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

        keyValueStorage =
                new SharedPreferencesKeyValueStorage(this,
                        SharedPreferencesKeyValueStorage.KV_STORAGE);

        final OAuthManager oauth = getOAuthManager(credentialStore);
        final RunKeeperOauthCallback runKeeperOauthCallback = new RunKeeperOauthCallback(
                credentialStore,
                runKeeperApi, updateFitnessActivityPageAction, keyValueStorage);
        oauth.authorizeExplicitly(CURRENT_USER, runKeeperOauthCallback, new Handler());

        paymentController = new PaymentController(sixApi, runKeeperApi,
                keyValueStorage);

        updateCounterController();
    }

    private void createApis() {
        final ApiProvider apiProvider = new ApiProvider();
        sixApi = apiProvider.getSixApi();
        runKeeperApi = apiProvider.getRunKeeperApi();

        mGoalView = (GoalView) findViewById(R.id.goalView);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeLayout.setOnRefreshListener(this);

        ((Button) findViewById(R.id.button_registration)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button_insertGoal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, InsertActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoalView.refresh();
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

    private void updateCounterController() {
        if (StringUtils.isNotEmpty(
                keyValueStorage.getString(SharedPreferencesKeyValueStorage.RUN_KEEPER_TOKEN_KEY))) {
            paymentController.updateDistanceCounter();
        }


    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 2000);

    }
}