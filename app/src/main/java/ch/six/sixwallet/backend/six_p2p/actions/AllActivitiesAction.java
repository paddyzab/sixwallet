package ch.six.sixwallet.backend.six_p2p.actions;

import android.util.Log;

import java.util.List;

import ch.six.sixwallet.Home;
import ch.six.sixwallet.backend.six_p2p.models.Activity;
import rx.functions.Action1;

public class AllActivitiesAction implements Action1<List<Activity>> {

    @Override
    public void call(final List<Activity> activities) {
        for (final Activity activity : activities) {
            if (isPendingAndRequested(activity)) {
                Log.d(Home.class.getSimpleName(), "activities pending: " + activity.getTimestamp());
            }
        }

        for (final Activity activity : activities) {
            if (isDoneAndRequested(activity)) {
                Log.d(Home.class.getSimpleName(), "activity done: " + activity.getTimestamp());
            }
        }
    }

    private boolean isPendingAndRequested(final Activity activity) {
        return (activity.getStatus() == Activity.PENDING) && (activity.getType() == Activity.REQUESTED);
    }

    private boolean isDoneAndRequested(final Activity activity) {
        return (activity.getStatus() == Activity.DONE) || (activity.getStatus() == Activity.DONE_NEWUSER) &&
                (activity.getType() == Activity.REQUESTED);
    }
}
