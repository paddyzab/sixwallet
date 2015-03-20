package ch.six.sixwallet.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import ch.six.sixwallet.Goal;
import ch.six.sixwallet.R;
import ch.six.sixwallet.utils.Utils.SharedPreferences;

public class InsertActivity extends Activity {

    static public final int CONTACT = 0;

    private EditText mPhoneNumber;
    private EditText mName;
    private Spinner mSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        mPhoneNumber = (EditText) findViewById(R.id.editText_phoneNumber);
        mName = (EditText) findViewById(R.id.editText_namegoal);

        findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, Contacts.Phones.CONTENT_URI);
                startActivityForResult(intent, CONTACT);
            }
        });


        mSpinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.api_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);


        findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || requestCode != CONTACT) return;
        Cursor c = managedQuery(data.getData(), null, null, null, null);
        if (c.moveToFirst()) {
            String phone = c.getString(c.getColumnIndexOrThrow(Contacts.Phones.NUMBER));
            mPhoneNumber.setText(phone);
        }
    }


    private void save() {
        Goal i = new Goal();
        i.setApi((String)mSpinner.getSelectedItem());
        i.setName(mName.getText().toString());
        i.setPhoneNumber(mPhoneNumber.getText().toString());
        ArrayList array = SharedPreferences.getSavedArray();
        array.add(i);
        SharedPreferences.saveArray(array);
    }

}
