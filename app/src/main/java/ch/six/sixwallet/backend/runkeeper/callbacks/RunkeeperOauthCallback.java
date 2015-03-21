package ch.six.sixwallet.backend.runkeeper.callbacks;

import com.google.api.client.auth.oauth2.Credential;

import com.wuman.android.auth.OAuthManager;
import com.wuman.android.auth.oauth2.store.SharedPreferencesCredentialStore;

import android.util.Log;

import java.io.IOException;

import ch.six.sixwallet.Home;
import ch.six.sixwallet.backend.runkeeper.RunKeeperApi;
import ch.six.sixwallet.backend.runkeeper.actions.UpdateFitnessActivityPageAction;
import ch.six.sixwallet.storage.SharedPreferencesKeyValueStorage;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RunKeeperOauthCallback implements OAuthManager.OAuthCallback<Credential> {

    final SharedPreferencesCredentialStore mSharedPreferencesCredentialStore;
    final RunKeeperApi mRunKeeperApi;
    final UpdateFitnessActivityPageAction mUpdateFitnessActivityPageAction;
    final SharedPreferencesKeyValueStorage mSharedPreferencesKeyValueStorage;

    public RunKeeperOauthCallback(
            final SharedPreferencesCredentialStore sharedPreferencesCredentialStore,
            final RunKeeperApi runKeeperApi,
            final UpdateFitnessActivityPageAction updateFitnessActivityPageAction,
            final SharedPreferencesKeyValueStorage sharedPreferencesKeyValueStorage) {
        mSharedPreferencesCredentialStore = sharedPreferencesCredentialStore;
        mRunKeeperApi = runKeeperApi;
        mUpdateFitnessActivityPageAction = updateFitnessActivityPageAction;
        mSharedPreferencesKeyValueStorage = sharedPreferencesKeyValueStorage;
    }

    @Override
    public void run(OAuthManager.OAuthFuture<Credential> future) {
        try {
            final Credential credential = future.getResult();
            final String mRunKeeperAccessToken = credential.getAccessToken();
            mSharedPreferencesCredentialStore.store(Home.CURRENT_USER, credential);

            if (mRunKeeperAccessToken != null) {
                mRunKeeperApi.getFitnessActivityFeedPage(
                        createBearer(mRunKeeperAccessToken))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mUpdateFitnessActivityPageAction);

                mSharedPreferencesKeyValueStorage
                        .storeString(SharedPreferencesKeyValueStorage.RUN_KEEPER_TOKEN_KEY,
                                mRunKeeperAccessToken);
            } else {
                Log.w(RunKeeperOauthCallback.class.getSimpleName(),
                        "Token is empty, and it should not.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createBearer(final String token) {
        return "Bearer " + token;
    }

}
