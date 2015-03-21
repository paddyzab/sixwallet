package ch.six.sixwallet.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.six.sixwallet.Home;
import ch.six.sixwallet.R;
import ch.six.sixwallet.backend.ApiProvider;
import ch.six.sixwallet.backend.six_p2p.SixApi;
import ch.six.sixwallet.backend.six_p2p.actions.AllActivitiesAction;
import ch.six.sixwallet.backend.six_p2p.models.PaymentActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListPaymentActivity extends ListActivity {


    ArrayList<PaymentActivity> mActivitiesList;
    private SixApi sixApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_payment);

        ApiProvider apiProvider = new ApiProvider();
        sixApi = apiProvider.getSixApi();

        mActivitiesList = new ArrayList<>();


        sixApi.getUserActivities(Home.USER_TOKEN,
                new Callback<List<PaymentActivity>>() {
                    @Override
                    public void success(List<PaymentActivity> activities,
                                        Response response) {
                        Log.d(Home.class.getSimpleName(), "success");
                        filterPaymentActivity(activities);
                        populateListView(activities);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(Home.class.getSimpleName(), "failure");
                        Toast.makeText(getApplicationContext(), "Error while getting the payment list, please try again", Toast.LENGTH_LONG).show();
                    }
                });

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void filterPaymentActivity(List<PaymentActivity> listPayment) {
        /*for (PaymentActivity p : listPayment) {
            if(!AllActivitiesAction.isPendingAndRequested(p)) {
                listPayment.remove(p);
            }
        }*/
        for (int i = 0; i < listPayment.size(); i++) {
            if(!AllActivitiesAction.isPendingAndRequested(listPayment.get(i))) {
                listPayment.remove(i);
            }
        }
    }
    private void populateListView(List<PaymentActivity> listPayment) {
        ArrayAdapter<PaymentActivity> itemsAdapter = new ArrayAdapter<PaymentActivity>(this, R.layout.simple_list_item_2, android.R.id.text1, listPayment) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                PaymentActivity item = getItem(position);
                String status = item.getAmount() + " .- CHF, ";


                if(item.getStatus() == PaymentActivity.PENDING) {
                    status += "Pending, ";
                }
                if(item.getType() == PaymentActivity.REQUESTED) {
                    status += "Requested";
                }

                ((TextView)v.findViewById(android.R.id.text1)).setText(getName(item.getPhoneNumber()) + " (" + item.getPhoneNumber() + ")");
                ((TextView)v.findViewById(android.R.id.text2)).setText(status);
                return v;
            }
        };

        setListAdapter(itemsAdapter);
    }


    private String getName(String phoneNumber) {
        if (phoneNumber.equals("+41796845634")) {
            return "Uncle sam";
        }
        return "";
    }

}
