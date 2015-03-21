package ch.six.sixwallet;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.six.sixwallet.activities.ListPaymentActivity;

/**
 * Created by jm on 21/03/15.
 */
public class GoalView extends RelativeLayout {


    public GoalView(Context context) {
        this(context, null);
    }

    public GoalView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoalView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public GoalView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        refresh();
    }


    public void refresh() {
        LayoutInflater lif = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        removeAllViewsInLayout();
        View child;
        Goal g = new Goal();
        if (!isInEditMode() && g.load(getContext())) {
            child = lif.inflate(R.layout.layout_view_goal, null);
            initView(child, g);
        } else {
            TextView tv = new TextView(getContext());
            tv.setText("No goal inserted");
            child = tv;
        }
        this.addView(child);
    }

    private void initView(View view, Goal goal) {

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListPaymentActivity.class);
                getContext().startActivity(intent);
            }
        });

        ((TextView)view.findViewById(R.id.textView_name)).setText(goal.getName());
        ((TextView)view.findViewById(R.id.textView_api)).setText(goal.getApi());
        ((TextView)view.findViewById(R.id.textView_phoneNumber)).setText(goal.getPhoneNumber());
    }

}
