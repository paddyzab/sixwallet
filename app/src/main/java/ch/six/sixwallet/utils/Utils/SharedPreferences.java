package ch.six.sixwallet.utils.Utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by jm on 20/03/15.
 */
public class SharedPreferences {

    android.content.SharedPreferences mPref;
    Context mContext;

    SharedPreferences(Context context) {
        mContext = context;
        mPref = mContext.getSharedPreferences("ch.six.sixwallet", Context.MODE_PRIVATE);
    }

    public static void saveArray(ArrayList array) {

        try{
            FileOutputStream fos= new FileOutputStream("myfile");
            ObjectOutputStream oos= new ObjectOutputStream(fos);
            oos.writeObject(array);
            oos.close();
            fos.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

    }

    public static ArrayList getSavedArray() {
        ArrayList<String> arraylist= new ArrayList<String>();
        try
        {
            FileInputStream fis = new FileInputStream("myfile");
            ObjectInputStream ois = new ObjectInputStream(fis);
            arraylist = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
            return null;
        }catch(ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }

        return arraylist;
    }


}
