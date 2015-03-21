package ch.six.sixwallet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by jm on 21/03/15.
 */
public class GoalView extends RelativeLayout {


    public GoalView(Context context) {
        super(context);
        refresh();
    }

    public GoalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        refresh();
    }

    public GoalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        refresh();
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
        if (g.load(getContext())) {
            child = lif.inflate(R.layout.layout_view_goal, null);
        } else {
            TextView tv = new TextView(getContext());
            tv.setText("No goal inserted");
            child = tv;
        }
        this.addView(child);
    }
}
