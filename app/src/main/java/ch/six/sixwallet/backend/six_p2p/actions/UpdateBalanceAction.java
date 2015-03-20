package ch.six.sixwallet.backend.six_p2p.actions;

import android.widget.TextView;

import ch.six.sixwallet.backend.six_p2p.models.Balance;
import rx.functions.Action1;

public class UpdateBalanceAction implements Action1<Balance> {

    final TextView mTextView;

    public UpdateBalanceAction(final TextView textView) {
        mTextView = textView;
    }

    @Override
    public void call(Balance balance) {
        mTextView.setText(String.valueOf(balance.getBalance()));
    }
}
