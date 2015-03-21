package ch.six.sixwallet;

import android.util.Log;

import ch.six.sixwallet.backend.runkeeper.RunKeeperApi;
import ch.six.sixwallet.backend.runkeeper.actions.UpdateFitnessActivityPageAction;
import ch.six.sixwallet.backend.six_p2p.SixApi;
import ch.six.sixwallet.storage.SharedPreferencesKeyValueStorage;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PaymentController {

    private final SixApi mSixApi;
    private final RunKeeperApi mRunKeeperApi;
    private final SharedPreferencesKeyValueStorage mSharedPreferencesKeyValueStorage;

    private final UpdateFitnessActivityPageAction updateFitnessActivityPageAction;

    private final double PAYMENT_TRIGGER_TRESCHOLD = 10;
    private double currentDistance;

    public PaymentController(final SixApi sixApi, final RunKeeperApi runKeeperApi,
            SharedPreferencesKeyValueStorage sharedPreferencesKeyValueStorage) {
        mSixApi = sixApi;
        mRunKeeperApi = runKeeperApi;
        mSharedPreferencesKeyValueStorage = sharedPreferencesKeyValueStorage;

        updateFitnessActivityPageAction = new UpdateFitnessActivityPageAction();

        Log.d(PaymentController.class.getSimpleName(), "current dist: " + currentDistance);
    }

    public void updateDistanceCounter() {
        mRunKeeperApi.getFitnessActivityFeedPage(
                createBearerToken(mSharedPreferencesKeyValueStorage
                        .getString(SharedPreferencesKeyValueStorage.RUN_KEEPER_TOKEN_KEY)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateFitnessActivityPageAction);
    }

    public String createBearerToken(final String token) {
        return "Bearer " + token;
    }

}
