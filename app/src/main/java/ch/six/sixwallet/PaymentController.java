package ch.six.sixwallet;


import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.pascalwelsch.holocircularprogressbar.HoloCircularProgressBar;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;

import android.widget.TextView;

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


    private final float PAYMENT_TRIGGER_THRESHOLD = 10000;

    private final SendRequestCallback sendRequestCallback;
    private float mCurrentDistance;

    private TextView mTextViewToday;
    private TextView mTextViewToGoal;

    private HoloCircularProgressBar mProgressBar;

    private ObjectAnimator mProgressBarAnimator;
    final NiftyDialogBuilder dialogBuilder;

    private final UpdateActivityCounterAction mUpdateActivityCounterAction;

    public PaymentController(final SixApi sixApi, final RunKeeperApi runKeeperApi,
            final SharedPreferencesKeyValueStorage sharedPreferencesKeyValueStorage,

            final TextView textViewToday, final TextView textViewToGoal,
            HoloCircularProgressBar progressBar) {

        mSixApi = sixApi;
        mRunKeeperApi = runKeeperApi;
        mSharedPreferencesKeyValueStorage = sharedPreferencesKeyValueStorage;
        sendRequestCallback = new SendRequestCallback();
        mUpdateActivityCounterAction = new UpdateActivityCounterAction(this);

        mCurrentDistance = mSharedPreferencesKeyValueStorage
                .getFloat(SharedPreferencesKeyValueStorage.DISTANCE_STORAGE_KEY);

        mTextViewToday = textViewToday;
        mTextViewToGoal = textViewToGoal;

        mProgressBar = progressBar;

        dialogBuilder = NiftyDialogBuilder.getInstance(textViewToday.getContext());
        dialogBuilder
                .withTitle("Congratulations!")
                .withMessage("You completed a lap. \nPayment request to sponsor was sent!")
                .withTitleColor("#FFFFFF")
                .withDividerColor("#ffff4444")
                .withMessageColor("#FFFFFFFF")
                .withDuration(700)
                .withEffect(Effectstype.RotateBottom)
                .isCancelableOnTouchOutside(true)
                .withDialogColor("#444444")
                .withButton1Text("OK")
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                });

        animate(mProgressBar, null, calculateProgress(mCurrentDistance), 1000);
        mTextViewToday.setText(buildDistanceString(mCurrentDistance, "Today"));
        mTextViewToGoal.setText(
                buildDistanceString((PAYMENT_TRIGGER_THRESHOLD - mCurrentDistance), "To Go!"));
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
        if (mCurrentDistance >= PAYMENT_TRIGGER_THRESHOLD) {
            mSixApi.createPaymentRequest(Home.USER_TOKEN,
                    createRequestTransaction("100", "Goal completed, I deserve a reward!",
                            "+41796845634"),
                    sendRequestCallback);

            dialogBuilder.show();

            //resetting KV storage distance count
            this.mCurrentDistance = 0;
            mSharedPreferencesKeyValueStorage.storeFloat(
                    SharedPreferencesKeyValueStorage.DISTANCE_STORAGE_KEY, mCurrentDistance);

            // Payment threshold not reached, increment the counter!
        } else {
            mSharedPreferencesKeyValueStorage.storeFloat(
                    SharedPreferencesKeyValueStorage.DISTANCE_STORAGE_KEY, mCurrentDistance);
        }

        animate(mProgressBar, null, calculateProgress(mCurrentDistance), 1000);

        mTextViewToday.setText(buildDistanceString(mCurrentDistance, "Today"));
        mTextViewToGoal.setText(
                buildDistanceString((PAYMENT_TRIGGER_THRESHOLD - mCurrentDistance), "To Go!"));
    }

    private float calculateProgress(float currentDistance) {
        return currentDistance / PAYMENT_TRIGGER_THRESHOLD;
    }


    private void animate(final HoloCircularProgressBar progressBar, final AnimatorListener listener,
            final float progress, final int duration) {

        mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress);
        mProgressBarAnimator.setDuration(duration);

        mProgressBarAnimator.addListener(new AnimatorListener() {

            @Override
            public void onAnimationCancel(final Animator animation) {
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                progressBar.setProgress(progress);
            }

            @Override
            public void onAnimationRepeat(final Animator animation) {
            }

            @Override
            public void onAnimationStart(final Animator animation) {
            }
        });
        if (listener != null) {
            mProgressBarAnimator.addListener(listener);
        }
        mProgressBarAnimator.reverse();
        mProgressBarAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                progressBar.setProgress((Float) animation.getAnimatedValue());
            }
        });
        progressBar.setMarkerProgress(progress);
        mProgressBarAnimator.start();
    }

    private String buildDistanceString(float currentDistance, String timeContext) {
        return String.valueOf((int) currentDistance) + "m " + timeContext;
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
