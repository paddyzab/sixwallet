package ch.six.sixwallet;

import android.util.Log;

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
    private double currentDistance;

    private final UpdateActivityCounterAction mUpdateActivityCounterAction;

    public PaymentController(final SixApi sixApi, final RunKeeperApi runKeeperApi,
            SharedPreferencesKeyValueStorage sharedPreferencesKeyValueStorage) {
        mSixApi = sixApi;
        mRunKeeperApi = runKeeperApi;
        mSharedPreferencesKeyValueStorage = sharedPreferencesKeyValueStorage;
        sendRequestCallback = new SendRequestCallback();
        mUpdateActivityCounterAction = new UpdateActivityCounterAction(this);
    }

    public void updateDistanceCounter() {
        mRunKeeperApi.getFitnessActivityFeedPage(
                createBearerToken(mSharedPreferencesKeyValueStorage
                        .getString(SharedPreferencesKeyValueStorage.RUN_KEEPER_TOKEN_KEY)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mUpdateActivityCounterAction);
    }

    public String createBearerToken(final String token) {
        return "Bearer " + token;
    }

    public void incrementCurrentDistance(double currentDistance) {
        this.currentDistance += currentDistance;

        if (currentDistance >= PAYMENT_TRIGGER_TRESCHOLD) {
            Log.d(PaymentController.class.getSimpleName(), "We can launch payment!");
            mSixApi.createPaymentRequest(Home.USER_TOKEN, createRequestTransaction("100", "Give me reward please", "+41796845634"),
                    sendRequestCallback);
        } else {
            Log.d(PaymentController.class.getSimpleName(), "still missing some meters...");
        }
    }

    private RequestTransaction createRequestTransaction(final String amount, final String comment,
            final String number) {
        return new RequestTransaction()
                .setAmount(amount)
                .setComment(comment)
                .setPhoneNumber(number);
    }
}
