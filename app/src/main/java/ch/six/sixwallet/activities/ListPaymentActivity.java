package ch.six.sixwallet.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.LinkedHashMap;

import ch.six.sixwallet.R;

public class ListPaymentActivity extends Activity {

    private ListView mListView;
    private LinkedHashMap mapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_payment);

        mListView = (ListView) findViewById(R.id.listView);


        mapList = new LinkedHashMap();
        mapList.put("a", "r");
        mapList.put("t", "h");

        //ArrayAdapter ArrayAdapter

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_payment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
