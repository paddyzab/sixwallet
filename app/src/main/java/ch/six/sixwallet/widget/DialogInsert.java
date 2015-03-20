package ch.six.sixwallet.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

/**
 * Created by jm on 20/03/15.
 */
public class DialogInsert extends AlertDialog.Builder{

    static public final int CONTACT = 0;

    private Activity mContext;

    public DialogInsert(Context context) {
        super(context);
        mContext = (Activity)context;

        setTitle("Title");
        // Set an EditText view to get user input
        final EditText input = new EditText(context);
        input.setHint("Phone Number");
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, Contacts.Phones.CONTENT_URI);
                mContext.startActivityForResult(intent, CONTACT);
            }
        });

        setView(input);


        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //String value = input.getText();
                // Do something with value!
            }
        });

        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

    }

    @NonNull
    @Override
    public AlertDialog show() {
        return super.show();
    }
}
