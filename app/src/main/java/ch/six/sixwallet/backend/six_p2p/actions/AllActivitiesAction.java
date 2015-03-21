package ch.six.sixwallet.backend.six_p2p.actions;

import android.util.Log;

import java.util.List;

import ch.six.sixwallet.Home;
import ch.six.sixwallet.backend.six_p2p.models.PaymentActivity;
import rx.functions.Action1;

public class AllActivitiesAction implements Action1<List<PaymentActivity>> {



    @Override
    public void call(final List<PaymentActivity> activities) {
        for (final PaymentActivity paymentActivity : activities) {
            if (isPendingAndRequested(paymentActivity)) {
                Log.d(Home.class.getSimpleName(), "activities pending: " + paymentActivity.getTimestamp());
            }
        }

        for (final PaymentActivity paymentActivity : activities) {
            if (isDoneAndRequested(paymentActivity)) {
                Log.d(Home.class.getSimpleName(), "activity done: " + paymentActivity.getTimestamp());
            }
        }
    }

    public static boolean isPendingAndRequested(final PaymentActivity paymentActivity) {
        return (paymentActivity.getStatus() == PaymentActivity.PENDING) && (paymentActivity.getType() == PaymentActivity.REQUESTED);
    }

    public static boolean isDoneAndRequested(final PaymentActivity paymentActivity) {
        return (paymentActivity.getStatus() == PaymentActivity.DONE) || (paymentActivity.getStatus() == PaymentActivity.DONE_NEWUSER) &&
                (paymentActivity.getType() == PaymentActivity.REQUESTED);
    }
}
