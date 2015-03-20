package ch.six.sixwallet.backend.six_p2p.actions;

import android.util.Log;

import java.util.List;

import ch.six.sixwallet.Home;
import ch.six.sixwallet.backend.six_p2p.models.Transaction;
import rx.functions.Action1;

public class UpdateTransactionsAction implements Action1<List<Transaction>> {

    @Override
    public void call(final List<Transaction> transactions) {
        for (final Transaction transaction : transactions) {
            filterOnlyRequestTransactions(transaction);
        }
    }

    private void filterOnlyRequestTransactions(Transaction transaction) {
        if (transaction.getType() == 3) {
            Log.d(Home.class.getSimpleName(),
                    "amount: " + transaction.getAmount() + " from: " + transaction
                            .getPhoneNumber());
        } else {
            Log.d(Home.class.getSimpleName(), "No new transactions of given type.");
        }
    }
}
