package ch.six.sixwallet.backend.runkeeper.actions;

import android.util.Log;

import ch.six.sixwallet.Home;
import ch.six.sixwallet.backend.runkeeper.models.FitnessActivityFeedPage;
import rx.functions.Action1;

public class UpdateFitnessActivityPageAction implements Action1<FitnessActivityFeedPage> {

    @Override
    public void call(FitnessActivityFeedPage fitnessActivityFeedPage) {
        Log.d(Home.class.getSimpleName(),
                " fitness activities: " +
                        fitnessActivityFeedPage
                                .getFitnessActivities().get(
                                0).getTotalDistance());
    }
}
