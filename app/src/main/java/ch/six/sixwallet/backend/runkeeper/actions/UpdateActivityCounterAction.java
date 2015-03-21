package ch.six.sixwallet.backend.runkeeper.actions;

import ch.six.sixwallet.PaymentController;
import ch.six.sixwallet.backend.runkeeper.models.FitnessActivityFeedPage;
import rx.functions.Action1;

public class UpdateActivityCounterAction implements Action1<FitnessActivityFeedPage> {


    final PaymentController mPaymentController;

    public UpdateActivityCounterAction(final PaymentController paymentController) {
        mPaymentController = paymentController;
    }

    @Override
    public void call(FitnessActivityFeedPage fitnessActivityFeedPage) {
        mPaymentController.incrementCurrentDistance(fitnessActivityFeedPage
                .getFitnessActivities().get(
                        0).getTotalDistance());
    }
}
