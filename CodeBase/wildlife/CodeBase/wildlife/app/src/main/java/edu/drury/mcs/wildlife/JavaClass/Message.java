package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by mark93 on 12/3/2016.
 */

public class Message {
    public static void showMessage(Context context, String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
