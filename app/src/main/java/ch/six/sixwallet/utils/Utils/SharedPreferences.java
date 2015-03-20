package ch.six.sixwallet.utils.Utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
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
            FileOutputStream fos= new FileOutputStream(getFile());
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
            FileInputStream fis = new FileInputStream(getFile());
            ObjectInputStream ois = new ObjectInputStream(fis);
            arraylist = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }catch(ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
        }

        return arraylist;
    }


    public static File getFile() {
        String extr = Environment.getExternalStorageDirectory().toString();
        File mFolder = new File(extr + "myfile");
        if (!mFolder.exists()) {
            try {
                mFolder.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*String s = "myfile";

        File f = new File(mFolder.getAbsolutePath(), s);*/

        return mFolder;
    }

}
