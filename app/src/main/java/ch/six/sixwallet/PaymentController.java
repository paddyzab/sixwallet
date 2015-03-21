package ch.six.sixwallet;

import ch.six.sixwallet.backend.runkeeper.RunKeeperApi;
import ch.six.sixwallet.backend.runkeeper.actions.UpdateActivityCounterAction;
import ch.six.sixwallet.backend.six_p2p.SixApi;
import ch.six.sixwallet.backend.six_p2p.callbacks.SendRequestCallback;
import ch.six.sixwallet.backend.six_p2p.models.RequestTransaction;
import ch.six.sixwallet.storage.SharedPreferencesKeyValueStorage;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PaymentController {

    private final SixApi mSixApi;
    private final RunKeeperApi mRunKeeperApi;
    private final SharedPreferencesKeyValueStorage mSharedPreferencesKeyValueStorage;

    private final double PAYMENT_TRIGGER_TRESCHOLD = 10000;
    private final SendRequestCallback sendRequestCallback;
    private float mCurrentDistance;

    private final UpdateActivityCounterAction mUpdateActivityCounterAction;

    public PaymentController(final SixApi sixApi, final RunKeeperApi runKeeperApi,
            final SharedPreferencesKeyValueStorage sharedPreferencesKeyValueStorage) {
        mSixApi = sixApi;
        mRunKeeperApi = runKeeperApi;
        mSharedPreferencesKeyValueStorage = sharedPreferencesKeyValueStorage;
        sendRequestCallback = new SendRequestCallback();
        mUpdateActivityCounterAction = new UpdateActivityCounterAction(this);

        mCurrentDistance = mSharedPreferencesKeyValueStorage
                .getFloat(SharedPreferencesKeyValueStorage.DISTANCE_STORAGE_KEY);
    }

    public void updateDistanceCounter() {
        mRunKeeperApi.getFitnessActivityFeedPage(
                createBearerToken(mSharedPreferencesKeyValueStorage
                        .getString(SharedPreferencesKeyValueStorage.RUN_KEEPER_TOKEN_KEY)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mUpdateActivityCounterAction);
    }

    public void incrementCurrentDistance(float currentDistance) {
        this.mCurrentDistance += currentDistance;

        // Payment threshold reached!
        if (mCurrentDistance >= PAYMENT_TRIGGER_TRESCHOLD) {
            mSixApi.createPaymentRequest(Home.USER_TOKEN,
                    createRequestTransaction("100", "Give me reward please", "+41796845634"),
                    sendRequestCallback);

            //resetting KV storage distance count
            mSharedPreferencesKeyValueStorage.storeFloat(
                    SharedPreferencesKeyValueStorage.DISTANCE_STORAGE_KEY, 0);
            // Payment threshold not reached, increment the counter!
        } else {
            mSharedPreferencesKeyValueStorage.storeFloat(
                    SharedPreferencesKeyValueStorage.DISTANCE_STORAGE_KEY, mCurrentDistance);
        }
    }

    private RequestTransaction createRequestTransaction(final String amount, final String comment,
            final String number) {
        return new RequestTransaction()
                .setAmount(amount)
                .setComment(comment)
                .setPhoneNumber(number);
    }

    private String createBearerToken(final String token) {
        return "Bearer " + token;
    }
}
