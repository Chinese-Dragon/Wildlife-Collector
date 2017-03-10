package edu.drury.mcs.wildlife.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

import edu.drury.mcs.wildlife.DB.syncDBTask;

public class SplashScreen extends AppCompatActivity {
    private AlertDialog needInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread timeThread = new Thread(){
            public void run(){

                try {
                    sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {

                    while(needInternet != null && needInternet.isShowing()){
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        };

        try {
            if(!new syncDBTask(this).execute().get()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("For the best experience and the most up to date information, please start Dnav with an internet connection.");
                needInternet = builder.create();
                needInternet.show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        timeThread.start();
    }
}
